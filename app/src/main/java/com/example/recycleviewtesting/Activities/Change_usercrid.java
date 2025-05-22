package com.example.recycleviewtesting.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recycleviewtesting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Change_usercrid extends AppCompatActivity {

    private TextView emailText, nameText;
    private Button changeNameBtn, changePassBtn, logoutBtn;

    private FirebaseAuth auth;
    private FirebaseUser user;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        // Initialize views
        emailText = findViewById(R.id.emailText);
        nameText = findViewById(R.id.nameText);
        changeNameBtn = findViewById(R.id.changeNameBtn);
        changePassBtn = findViewById(R.id.changePasswordBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        // Firebase auth
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        // Load current user data
        if (user != null) {
            emailText.setText("Email: " + user.getEmail());
            nameText.setText("Name: " + (user.getDisplayName() != null ? user.getDisplayName() : "Not Set"));
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish(); // kill activity if no user
            return;
        }

        // Change Name
        changeNameBtn.setOnClickListener(v -> {
            EditText input = new EditText(this);
            input.setHint("Enter new name");

            new AlertDialog.Builder(this)
                    .setTitle("Change Name")
                    .setView(input)
                    .setPositiveButton("Update", (dialog, which) -> {
                        String newName = input.getText().toString().trim();
                        if (newName.isEmpty()) {
                            Toast.makeText(this, "Name can't be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(newName)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        nameText.setText("Name: " + newName);
                                        Toast.makeText(this, "Name updated!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(this, "Failed to update name", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // Change Password
        changePassBtn.setOnClickListener(v -> {
            EditText input = new EditText(this);
            input.setHint("Enter new password (6+ chars)");
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            new AlertDialog.Builder(this)
                    .setTitle("Change Password")
                    .setView(input)
                    .setPositiveButton("Update", (dialog, which) -> {
                        String newPass = input.getText().toString().trim();

                        if (newPass.length() < 6) {
                            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        user.updatePassword(newPass)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(this, "Password updated!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        // Logout
        logoutBtn.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(Change_usercrid.this, LoginActivity.class));
            finish();
        });
    }
}
