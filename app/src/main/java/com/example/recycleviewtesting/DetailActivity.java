package com.example.recycleviewtesting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private Button selectedSizeButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView productImage = findViewById(R.id.productImage);
        TextView productTitle = findViewById(R.id.productTitle);
        TextView productDesc = findViewById(R.id.productDesc);
        Button addToCartBtn = findViewById(R.id.addToCartBtn);

        // Get data from the intent
        String title = getIntent().getStringExtra("PRODUCT_TITLE");
        String price = getIntent().getStringExtra("PRODUCT_PRICE");
        int imageResource = getIntent().getIntExtra("PRODUCT_IMAGE", R.drawable.casualshoes2);

        // Set the data to views
        if (title != null) {
            productTitle.setText(title);
        }
        productImage.setImageResource(imageResource);

        // You can customize this description based on the product
        productDesc.setText("In love with the classic look of '80s basketball but have a thing for today's game? "
                + "Made from at least 20% recycled materials by weight, with crisp upper and stitched overlays inspired by retro style.\n\n"
                + "• Colour: White/Black\n• Style: DH3158-101\n• Country of Origin: India, Vietnam");

        int[] sizeButtonIds = {
                R.id.size39, R.id.size40, R.id.size41, R.id.size42, R.id.size43, R.id.size44
        };

        for (int id : sizeButtonIds) {
            Button sizeButton = findViewById(id);
            sizeButton.setOnClickListener(view -> {
                if (selectedSizeButton != null) {
                    selectedSizeButton.setBackgroundColor(getResources().getColor(android.R.color.white));
                    selectedSizeButton.setTextColor(getResources().getColor(android.R.color.black));
                }
                selectedSizeButton = sizeButton;
                selectedSizeButton.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));
                selectedSizeButton.setTextColor(getResources().getColor(android.R.color.white));
            });
        }

        addToCartBtn.setOnClickListener(v -> {
            if (selectedSizeButton != null) {
                String size = selectedSizeButton.getText().toString();
                Toast.makeText(this, "Added size " + size + " to cart", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please select a size", Toast.LENGTH_SHORT).show();
            }
        });
    }
}