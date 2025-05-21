package com.example.recycleviewtesting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MyAddressActivity extends AppCompatActivity {

    private LinearLayout addressContainer;
    private CheckBox useAddressCheckbox;
    private Button addEditAddressBtn, addressBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference addressRef;

    private boolean checkboxListenerActive = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);

        // Initialize views
        addressContainer = findViewById(R.id.address_container);
        useAddressCheckbox = findViewById(R.id.use_address_checkbox);
        addEditAddressBtn = findViewById(R.id.add_edit_address_btn);
        addressBtn = findViewById(R.id.addressbtn);

        // Initialize FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        addressRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("address");

        // Load the address from Firebase
        loadAddressFromFirebase();

        // Click listener for Add/Edit Address button
        addEditAddressBtn.setOnClickListener(v -> {
            // Go to the Add/Edit Address screen
            Intent intent = new Intent(MyAddressActivity.this, AddAddressActivity.class);
            startActivity(intent);
        });

        // Click listener for Add Address button
        addressBtn.setOnClickListener(v -> {
            // Go to Add Address Activity if no address is available
            Intent intent = new Intent(MyAddressActivity.this, AddAddressActivity.class);
            startActivity(intent);
        });

        // Checkbox listener for selecting the address
        useAddressCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!checkboxListenerActive) return;

            if (isChecked) {
                Toast.makeText(MyAddressActivity.this, "Using saved address", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MyAddressActivity.this, "Please add an address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAddressFromFirebase() {
        addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserAddress address = dataSnapshot.getValue(UserAddress.class);
                if (address != null) {
                    displayAddress(address);
                    addressContainer.setVisibility(View.VISIBLE);
                    useAddressCheckbox.setVisibility(View.VISIBLE);

                    checkboxListenerActive = false;
                    useAddressCheckbox.setChecked(true);
                    checkboxListenerActive = true;
                } else {
                    addressContainer.setVisibility(View.GONE);
                    useAddressCheckbox.setVisibility(View.GONE);
                    Toast.makeText(MyAddressActivity.this, "No address found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MyAddressActivity.this, "Failed to load address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayAddress(UserAddress address) {
        TextView nameText = findViewById(R.id.user_name);
        TextView phoneText = findViewById(R.id.user_phone);
        TextView regionText = findViewById(R.id.user_region);
        TextView streetText = findViewById(R.id.user_street);
        TextView unitText = findViewById(R.id.user_unit);

        nameText.setText("Name: " + address.getName());
        phoneText.setText("Phone: " + address.getPhone());
        regionText.setText("Region: " + address.getRegion());
        streetText.setText("Street: " + address.getStreet());
        unitText.setText("Unit: " + (address.getUnit() != null ? address.getUnit() : ""));

        addressContainer.setVisibility(View.VISIBLE);
        useAddressCheckbox.setVisibility(View.VISIBLE);
    }
}
