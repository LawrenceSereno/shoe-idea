package com.example.recycleviewtesting.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recycleviewtesting.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.signupButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()
            val fullName = binding.fullNameEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()

            if (validateInputs(email, password, confirmPassword, fullName, phone)) {
                registerUser(email, password, fullName, phone)
            }
        }

        binding.loginRedirectText.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun validateInputs(
        email: String,
        password: String,
        confirmPassword: String,
        fullName: String,
        phone: String
    ): Boolean {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun registerUser(email: String, password: String, fullName: String, phone: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser

                    // ✅ NEW: Update Firebase Auth display name with full name
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                // ✅ After setting name, send email verification
                                user.sendEmailVerification()
                                    .addOnCompleteListener { verifyTask ->
                                        if (verifyTask.isSuccessful) {

                                            // ✅ Save user details in Firestore
                                            val userData = hashMapOf(
                                                "userId" to user.uid,
                                                "email" to email,
                                                "fullName" to fullName,
                                                "phone" to phone,
                                                "createdAt" to System.currentTimeMillis()
                                            )

                                            db.collection("users").document(user.uid)
                                                .set(userData)
                                                .addOnSuccessListener {
                                                    Toast.makeText(
                                                        this,
                                                        "Registration successful! Please verify your email before logging in.",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                    firebaseAuth.signOut()
                                                    startActivity(Intent(this, LoginActivity::class.java))
                                                    finish()
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(
                                                        this,
                                                        "Error saving user data: ${e.message}",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "Verification email failed: ${verifyTask.exception?.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }

                            } else {
                                Toast.makeText(
                                    this,
                                    "Failed to set display name: ${updateTask.exception?.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    /* ❌ OLD CODE - NO DISPLAY NAME SET
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { verifyTask ->
                            if (verifyTask.isSuccessful) {
                                val userData = hashMapOf(
                                    "userId" to user.uid,
                                    "email" to email,
                                    "fullName" to fullName,
                                    "phone" to phone,
                                    "createdAt" to System.currentTimeMillis()
                                )

                                db.collection("users").document(user.uid)
                                    .set(userData)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this,
                                            "Registration successful! Please verify your email before logging in.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        firebaseAuth.signOut()
                                        startActivity(Intent(this, LoginActivity::class.java))
                                        finish()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            this,
                                            "Error saving user data: ${e.message}",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                            } else {
                                Toast.makeText(
                                    this,
                                    "Verification email failed: ${verifyTask.exception?.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    */
                } else {
                    Log.e("SignUp", "Registration failed", task.exception)
                    Toast.makeText(
                        this,
                        "Registration failed: ${task.exception?.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
