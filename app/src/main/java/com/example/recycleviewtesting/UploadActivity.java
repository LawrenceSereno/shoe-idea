package com.example.recycleviewtesting;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadActivity extends AppCompatActivity {

    private EditText nameInput, priceInput, imageUrlInput, descriptionInput;
    private Button uploadBtn;

    private DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        // Bind UI components
        nameInput = findViewById(R.id.nameInput);
        priceInput = findViewById(R.id.priceInput);
        imageUrlInput = findViewById(R.id.imageUrlInput);
        descriptionInput = findViewById(R.id.descriptionInput); // Bind description input
        uploadBtn = findViewById(R.id.uploadBtn);

        // Firebase reference to products node
        databaseReference = FirebaseDatabase.getInstance().getReference("products");

        // Upload button click listener
        uploadBtn.setOnClickListener(v -> {
            // Get user input
            String name = nameInput.getText().toString().trim();
            String priceStr = priceInput.getText().toString().trim(); // Read as String
            String imageUrl = imageUrlInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim(); // Get description input

            // Validate input
            if (name.isEmpty() || priceStr.isEmpty() || imageUrl.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Convert price from String to Long
           Double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Price must be a valid number", Toast.LENGTH_SHORT).show();
                return;
            }

            // Generate unique ID for the product
            String id = databaseReference.push().getKey();

            // Create a Product object with the entered data
            Product product = new Product(name, price, description, imageUrl); // Now uses Long price

            // Save the product to Firebase
            if (id != null) {
                databaseReference.child(id).setValue(product)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(UploadActivity.this, "Product Uploaded!", Toast.LENGTH_SHORT).show();
                            finish();  // Close the activity after upload
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(UploadActivity.this, "Failed to upload product: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}
