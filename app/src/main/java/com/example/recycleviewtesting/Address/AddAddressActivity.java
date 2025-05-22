package com.example.recycleviewtesting.Address;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recycleviewtesting.R;
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

        // Populate spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.region_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSpinner.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();

        // ✅ NEW: Use singular "address" to match MyAddressActivity
        addressRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("address");

        saveAddressBtn.setOnClickListener(v -> {
            String name = nameEdit.getText().toString();
            String phone = phoneEdit.getText().toString();
            String region = regionSpinner.getSelectedItem().toString();
            String street = streetEdit.getText().toString();
            String unit = unitEdit.getText().toString();

            if (name.isEmpty() || phone.isEmpty() || region.isEmpty() || street.isEmpty()) {
                Toast.makeText(this, "All fields except unit are required", Toast.LENGTH_SHORT).show();
                return;
            }

            UserAddress newAddress = new UserAddress(name, phone, region, street, unit, true); // ✅ true = use this address

            // ✅ NEW: Overwrite the address directly (single address per user)
            addressRef.setValue(newAddress).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Address added", Toast.LENGTH_SHORT).show();
                    finish(); // Go back to MyAddressActivity
                } else {
                    Toast.makeText(this, "Failed to save address", Toast.LENGTH_SHORT).show();
                }
            });

            // 🔁 OLD CODE (Commented out: was saving multiple addresses using push ID)
/*
            // OLD: Used a new ID for each address (multiple address support)
            addressRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("addresses");

            String addressId = addressRef.push().getKey();
            if (addressId != null) {
                addressRef.child(addressId).setValue(newAddress).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Address added", Toast.LENGTH_SHORT).show();
                        finish(); // Return to MyAddressActivity
                    } else {
                        Toast.makeText(this, "Failed to save address", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(this, "Could not generate address ID", Toast.LENGTH_SHORT).show();
            }
*/
        });
    }
}
