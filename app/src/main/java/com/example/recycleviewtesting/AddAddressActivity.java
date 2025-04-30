package com.example.recycleviewtesting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddAddressActivity extends AppCompatActivity {

    private EditText nameInput, phoneInput, regionInput, streetInput, unitInput;
    private Button saveAddressBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize views
        nameInput = findViewById(R.id.name_input);
        phoneInput = findViewById(R.id.phone_input);
        regionInput = findViewById(R.id.region_input);
        streetInput = findViewById(R.id.street_input);
        unitInput = findViewById(R.id.unit_input);
        saveAddressBtn = findViewById(R.id.save_btn);

        // Set onClickListener for Save Address button
        saveAddressBtn.setOnClickListener(v -> {
            // Capture address details from the input fields
            String name = nameInput.getText().toString().trim();
            String phone = phoneInput.getText().toString().trim();
            String region = regionInput.getText().toString().trim();
            String street = streetInput.getText().toString().trim();
            String unit = unitInput.getText().toString().trim();

            // Basic validation (optional)
            if (name.isEmpty() || phone.isEmpty() || region.isEmpty() || street.isEmpty()) {
                Toast.makeText(AddAddressActivity.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new UserAddress object
            UserAddress userAddress = new UserAddress(name, phone, region, street, unit);

            // Save the address to Firebase under the user's unique ID
            String userId = mAuth.getCurrentUser().getUid(); // Get current user ID
            mDatabase.child("user_addresses").child(userId).setValue(userAddress)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddAddressActivity.this, "Address saved successfully", Toast.LENGTH_SHORT).show();
                            // Proceed to Order Confirmation activity
                            Intent intent = new Intent(AddAddressActivity.this, OrderConfirmationActivity.class);
                            intent.putExtra("userAddress", userAddress);  // Pass the address data
                            startActivity(intent);
                        } else {
                            Toast.makeText(AddAddressActivity.this, "Failed to save address", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
