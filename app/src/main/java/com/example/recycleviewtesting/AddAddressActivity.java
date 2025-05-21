package com.example.recycleviewtesting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddAddressActivity extends AppCompatActivity {

    private EditText nameInput, phoneInput, streetInput, unitInput;
    private Spinner regionSpinner;
    private Button saveBtn;

    private SharedPreferences sharedPreferences;

    private static final String[] REGIONS = {"Region 1", "Region 2", "Region 3", "Region 4"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        nameInput = findViewById(R.id.name_input);
        phoneInput = findViewById(R.id.phone_input);
        streetInput = findViewById(R.id.street_input);
        unitInput = findViewById(R.id.unit_input);
        regionSpinner = findViewById(R.id.region_spinner);
        saveBtn = findViewById(R.id.save_btn);

        sharedPreferences = getSharedPreferences("address_pref", MODE_PRIVATE);

        // Setup spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, REGIONS);
        regionSpinner.setAdapter(adapter);

        loadExistingData();

        saveBtn.setOnClickListener(v -> saveAddress());
    }

    private void loadExistingData() {
        String name = sharedPreferences.getString("name", "");
        String phone = sharedPreferences.getString("phone", "");
        String region = sharedPreferences.getString("region", "");
        String street = sharedPreferences.getString("street", "");
        String unit = sharedPreferences.getString("unit", "");

        nameInput.setText(name);
        phoneInput.setText(phone);
        streetInput.setText(street);
        unitInput.setText(unit);

        if (!region.isEmpty()) {
            int spinnerPosition = ((ArrayAdapter) regionSpinner.getAdapter()).getPosition(region);
            regionSpinner.setSelection(spinnerPosition);
        }
    }

    private void saveAddress() {
        String name = nameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String region = regionSpinner.getSelectedItem().toString();
        String street = streetInput.getText().toString().trim();
        String unit = unitInput.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || street.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("phone", phone);
        editor.putString("region", region);
        editor.putString("street", street);
        editor.putString("unit", unit);
        editor.apply();

        Toast.makeText(this, "Address saved!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
