package com.example.recycleviewtesting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MyAddressActivity extends AppCompatActivity {

    private LinearLayout addressContainer;
    private CheckBox useAddressCheckbox;
    private Button addEditAddressBtn;

    private SharedPreferences sharedPreferences;

    private boolean checkboxListenerActive = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);

        addressContainer = findViewById(R.id.address_container);
        useAddressCheckbox = findViewById(R.id.use_address_checkbox);
        addEditAddressBtn = findViewById(R.id.add_edit_address_btn);

        sharedPreferences = getSharedPreferences("address_pref", MODE_PRIVATE);

        loadAddressIntoLayout();

        addEditAddressBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MyAddressActivity.this, AddAddressActivity.class);
            startActivity(intent);
        });

        useAddressCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!checkboxListenerActive) return;

            if (isChecked) {
                Toast.makeText(MyAddressActivity.this, "Using saved address", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MyAddressActivity.this, "Please add an address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAddressIntoLayout() {
        String name = sharedPreferences.getString("name", "");
        String phone = sharedPreferences.getString("phone", "");
        String region = sharedPreferences.getString("region", "");
        String street = sharedPreferences.getString("street", "");
        String unit = sharedPreferences.getString("unit", "");

        if (!name.isEmpty() && !phone.isEmpty() && !region.isEmpty() && !street.isEmpty()) {
            View addressView = getLayoutInflater().inflate(R.layout.item_address, null);

            TextView nameText = addressView.findViewById(R.id.item_name);
            TextView phoneText = addressView.findViewById(R.id.item_phone);
            TextView regionText = addressView.findViewById(R.id.item_region);
            TextView streetText = addressView.findViewById(R.id.item_street);
            TextView unitText = addressView.findViewById(R.id.item_unit);
            RadioButton radioButton = addressView.findViewById(R.id.radio_button);

            nameText.setText("Name: " + name);
            phoneText.setText("Phone: " + phone);
            regionText.setText("Region: " + region);
            streetText.setText("Street: " + street);
            unitText.setText("Unit: " + (unit != null ? unit : ""));

            radioButton.setChecked(true);

            addressContainer.removeAllViews();
            addressContainer.addView(addressView);

            // Show container and checkbox
            addressContainer.setVisibility(View.VISIBLE);
            useAddressCheckbox.setVisibility(View.VISIBLE);

            checkboxListenerActive = false;
            useAddressCheckbox.setChecked(true);
            checkboxListenerActive = true;

        } else {
            addressContainer.setVisibility(View.GONE);
            useAddressCheckbox.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAddressIntoLayout(); // Refresh updated address
    }
}
