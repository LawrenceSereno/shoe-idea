package com.example.recycleviewtesting;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.recycleviewtesting.Item.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.UUID;

public class DetailActivity extends AppCompatActivity {

    private Button[] sizeButtons;
    private ImageView productImage;
    private TextView productTitle, productPrice, productDescription;
    private Button addToCartBtn;
    private Button selectedButton;

    private FirebaseAuth mAuth;
    private DatabaseReference cartRef;

    private String productId;
    private String imageUrl;
    private String name;
    private double price;
    private String description;
    private String selectedSize = "";
    private String sellerId;  // Add this for the seller's ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userId);

        // Views
        productImage = findViewById(R.id.productImage);
        productTitle = findViewById(R.id.productTitle);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDesc);
        addToCartBtn = findViewById(R.id.addToCartBtn);

        // Get data
        productId = getIntent().getStringExtra("productId");
        imageUrl = getIntent().getStringExtra("imageUrl");
        name = getIntent().getStringExtra("name");
        price = getIntent().getDoubleExtra("price", 0.0);
        description = getIntent().getStringExtra("description");
        sellerId = getIntent().getStringExtra("sellerId");  // Get the seller's ID from the Intent

        // Safe fallback
        if (imageUrl == null || name == null || description == null) {
            Toast.makeText(this, "Missing product details.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (productId == null || productId.isEmpty()) {
            productId = UUID.randomUUID().toString(); // fallback generate new ID
        }

        // Load UI
        Glide.with(this).load(imageUrl).into(productImage);
        productTitle.setText(name);
        productPrice.setText("₱" + price);
        productDescription.setText(description);

        // Size buttons
        sizeButtons = new Button[]{
                findViewById(R.id.size39),
                findViewById(R.id.size40),
                findViewById(R.id.size41),
                findViewById(R.id.size42),
                findViewById(R.id.size43),
                findViewById(R.id.size44)
        };

        for (Button btn : sizeButtons) {
            btn.setOnClickListener(v -> {
                for (Button b : sizeButtons) {
                    b.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#E0E0E0")));
//                    btn.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
                }
                btn.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));
                btn.setTextColor(Color.WHITE);
                selectedButton = btn;
                selectedSize = btn.getText().toString();
            });
        }

        // Default selection
        if (sizeButtons.length > 0 && selectedSize.isEmpty()) {
            for (Button btn : sizeButtons) {
                btn.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                btn.setTextColor(Color.WHITE);
            }
            selectedButton = sizeButtons[0]; // Optional: still set a default selected one
            selectedSize = selectedButton.getText().toString();
        }


        addToCartBtn.setOnClickListener(v -> {
            if (selectedSize.isEmpty()) {
                Toast.makeText(this, "Please select a size", Toast.LENGTH_SHORT).show();
                return;
            }
            addToCart();
        });
    }

    private void addToCart() {
        CartItem cartItem = new CartItem(
                productId,
                name,
                imageUrl,
                selectedSize,
                price,
                1
        );

        String cartItemId = cartRef.push().getKey();
        cartItem.setId(cartItemId);

        cartRef.child(cartItemId).setValue(cartItem)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, Cart_Activity.class));
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add to cart: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}