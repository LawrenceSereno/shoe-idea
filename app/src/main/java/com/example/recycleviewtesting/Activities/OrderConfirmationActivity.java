package com.example.recycleviewtesting.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recycleviewtesting.Item.CartItem;
import com.example.recycleviewtesting.Item.Purchase;
import com.example.recycleviewtesting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.*;

public class OrderConfirmationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private Button chatButton, backButton; // ✅ Add button references

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // ✅ Find buttons by ID
        chatButton = findViewById(R.id.chat_button);
        backButton = findViewById(R.id.back_button);

        // ✅ Set chat button to open a link
        chatButton.setOnClickListener(v -> {
            String url = "https://www.facebook.com/profile.php?id=61576859391334"; // 🔁 Replace with your desired URL
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        });

        // ✅ Set back button to go to MainActivity
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(OrderConfirmationActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        ArrayList<CartItem> cartItems = getIntent().getParcelableArrayListExtra("cartItems");
        if (cartItems != null && !cartItems.isEmpty()) {
            savePurchaseToFirestore(cartItems);
        } else {
            Toast.makeText(this, "No items to save", Toast.LENGTH_SHORT).show();
        }
    }

    private void savePurchaseToFirestore(List<CartItem> cartItems) {
        String userId = mAuth.getCurrentUser().getUid();

        List<Map<String, Object>> itemList = new ArrayList<>();
        for (CartItem item : cartItems) {
            Map<String, Object> map = new HashMap<>();
            map.put("productId", item.getProductId());
            map.put("name", item.getName());
            map.put("imageUrl", item.getImageUrl());
            map.put("price", item.getPrice());
            map.put("quantity", item.getQuantity());
            map.put("size", item.getSize());
            itemList.add(map);
        }

        Purchase purchase = new Purchase(new Date(), itemList, userId); // ✅ Add userId if your model supports it

        firestore.collection("Purchases")
                .add(purchase)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(this, "Purchase saved!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to save purchase: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
