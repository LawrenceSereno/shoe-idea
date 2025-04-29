package com.example.recycleviewtesting;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycleviewtesting.Adapter.MyAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<Product> productList;
    private DatabaseReference mDatabase;

    // ProgressBar for loading indicator
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));  // This sets 2 columns

        productList = new ArrayList<>();
        adapter = new MyAdapter(productList, this);
        recyclerView.setAdapter(adapter);

        // Initialize Firebase database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("products");

        // Initialize ProgressBar (for loading indicator)
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.INVISIBLE);  // Set to invisible by default

        // Load products from Firebase
        loadProducts();
    }

    private void loadProducts() {
        // Show loading indicator
        showLoading();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the list first to avoid leftover data
                productList.clear();

                // Loop through the snapshot and add valid products to the list
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    // Get the values
                    String name = itemSnapshot.child("name").getValue(String.class);
                    String description = itemSnapshot.child("description").getValue(String.class);
                    String imageUrl = itemSnapshot.child("imageUrl").getValue(String.class);

                    // Retrieve the price as Long
                    Long price = itemSnapshot.child("price").getValue(Long.class);  // Ensure the price is Long

                    // Log to check if data is being retrieved correctly
                    Log.d("MainActivity", "Product name: " + name + ", Price: " + price);

                    // Only add valid (non-null) products
                    if (name != null && price != null) {
                        Product product = new Product(name, price, description, imageUrl);  // Pass Long for price
                        productList.add(product);
                    }
                }

                // Notify adapter about the data change
                adapter.notifyDataSetChanged();

                // Hide the progress bar once data is loaded
                hideLoading();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors
                Toast.makeText(MainActivity.this, "Failed to load products.", Toast.LENGTH_SHORT).show();
                hideLoading();
            }
        });
    }

    // Show a loading indicator (this could be a progress bar or other UI element)
    private void showLoading() {
        progressBar.setVisibility(ProgressBar.VISIBLE);  // Show the progress bar
    }

    // Hide the loading indicator
    private void hideLoading() {
        progressBar.setVisibility(ProgressBar.INVISIBLE);  // Hide the progress bar
    }
}
