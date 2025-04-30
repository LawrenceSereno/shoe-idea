package com.example.recycleviewtesting;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MyAddressActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button addAddressBtn = findViewById(R.id.add_address_btn);
        addAddressBtn.setOnClickListener(v -> startActivity(new Intent(this, AddAddressActivity.class)));
    }
}