package com.example.recycleviewtesting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddAddressActivity extends AppCompatActivity {

    private EditText nameEdit, phoneEdit, streetEdit, unitEdit;
    private Spinner regionSpinner;
    private Button saveAddressBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference addressRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        // Bind UI elements
        nameEdit = findViewById(R.id.name_input);
        phoneEdit = findViewById(R.id.phone_input);
        regionSpinner = findViewById(R.id.region_spinner);
        streetEdit = findViewById(R.id.street_input);
        unitEdit = findViewById(R.id.unit_input);
        saveAddressBtn = findViewById(R.id.save_btn);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        addressRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("address");

        saveAddressBtn.setOnClickListener(v -> {
            String name = nameEdit.getText().toString();
            String phone = phoneEdit.getText().toString();
            String region = regionSpinner.getSelectedItem().toString();  // Getting selected region
            String street = streetEdit.getText().toString();
            String unit = unitEdit.getText().toString();

            if (name.isEmpty() || phone.isEmpty() || region.isEmpty() || street.isEmpty()) {
                Toast.makeText(AddAddressActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create the UserAddress object to store in Firebase
            UserAddress newAddress = new UserAddress(name, phone, region, street, unit, true);

            addressRef.setValue(newAddress)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddAddressActivity.this, "Address added successfully", Toast.LENGTH_SHORT).show();
                            finish();  // Go back to MyAddressActivity
                        } else {
                            Toast.makeText(AddAddressActivity.this, "Failed to add address", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
