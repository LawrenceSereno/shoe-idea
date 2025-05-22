package com.example.recycleviewtesting.Address;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recycleviewtesting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

public class MyAddressActivity extends AppCompatActivity {

    private LinearLayout addressContainer;
    private CheckBox useAddressCheckbox;
    private Button addEditAddressBtn, addressBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference addressRef;
    private boolean checkboxListenerActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);

        addressContainer = findViewById(R.id.address_container);
        useAddressCheckbox = findViewById(R.id.use_address_checkbox);
        addEditAddressBtn = findViewById(R.id.add_edit_address_btn);
        addressBtn = findViewById(R.id.addressbtn);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        addressRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("address");

        loadAddressFromFirebase();

        addEditAddressBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddAddressActivity.class);
            startActivity(intent);
        });

        addressBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddAddressActivity.class);
            startActivity(intent);
        });

        // OLD: useAddressCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
        //     if (!checkboxListenerActive) return;
        //     if (isChecked) {
        //         Toast.makeText(this, "Using saved address", Toast.LENGTH_SHORT).show();
        //     } else {
        //         Toast.makeText(this, "Unchecked", Toast.LENGTH_SHORT).show();
        //     }
        // });

        // ✅ NEW: Save checkbox state directly to Firebase
        useAddressCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!checkboxListenerActive) return;

            Map<String, Object> updates = new HashMap<>();
            updates.put("useAddress", isChecked);

            addressRef.updateChildren(updates).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, isChecked ? "Using saved address" : "Unchecked", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to update address preference", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void loadAddressFromFirebase() {
        addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                UserAddress address = snapshot.getValue(UserAddress.class);
                if (address != null) {
                    displayAddress(address);

                    // OLD: use sharedPreferences
                    // sharedPreferences.edit().putBoolean(KEY_USE_ADDRESS, true).apply();

                    checkboxListenerActive = false;
                    useAddressCheckbox.setChecked(address.isUseAddress()); // ✅ Use Firebase flag
                    checkboxListenerActive = true;
                } else {
                    addressContainer.setVisibility(View.GONE);
                    useAddressCheckbox.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MyAddressActivity.this, "Failed to load address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayAddress(UserAddress address) {
        ((TextView) findViewById(R.id.user_name)).setText("Name: " + address.getName());
        ((TextView) findViewById(R.id.user_phone)).setText("Phone: " + address.getPhone());
        ((TextView) findViewById(R.id.user_region)).setText("Region: " + address.getRegion());
        ((TextView) findViewById(R.id.user_street)).setText("Street: " + address.getStreet());
        ((TextView) findViewById(R.id.user_unit)).setText("Unit: " + address.getUnit());

        addressContainer.setVisibility(View.VISIBLE);
        useAddressCheckbox.setVisibility(View.VISIBLE);
    }
}
