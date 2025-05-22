package com.example.recycleviewtesting.Activities;

import android.os.Bundle;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

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

        Purchase purchase = new Purchase(new Date(), itemList);

        firestore.collection("Purchases")
                .add(purchase)
                .addOnSuccessListener(documentReference ->
                        Toast.makeText(this, "Purchase saved!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to save purchase: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
