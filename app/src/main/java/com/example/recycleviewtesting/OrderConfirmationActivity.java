package com.example.recycleviewtesting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.*;

public class OrderConfirmationActivity extends AppCompatActivity {

    private TextView confirmationText;
    private Button chatButton;
    private String sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        confirmationText = findViewById(R.id.confirmation_text);
        chatButton = findViewById(R.id.chat_button);

        Intent intent = getIntent();
        UserAddress userAddress = (UserAddress) intent.getSerializableExtra("userAddress");

        if (userAddress != null) {
            String confirmationMessage = "Order Confirmed!\n\n" +
                    "Name: " + userAddress.getName() + "\n" +
                    "Phone: " + userAddress.getPhone() + "\n" +
                    "Address: " + userAddress.getRegion() + ", " + userAddress.getStreet() + " " + userAddress.getUnit();
            confirmationText.setText(confirmationMessage);
        }

        // 🔥 Automatically get the latest product's sellerId from Firebase
        DatabaseReference productsRef = FirebaseDatabase.getInstance("https://agan-9f3e2-default-rtdb.firebaseio.com/")
                .getReference("products");

        productsRef.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                    sellerId = productSnapshot.child("sellerId").getValue(String.class);

                    // 💬 Setup chat button after we get sellerId
                    chatButton.setOnClickListener(view -> {
                        Intent chatIntent = new Intent(OrderConfirmationActivity.this, Chats.class);
                        chatIntent.putExtra("sellerId", sellerId);
                        startActivity(chatIntent);
                    });
                }

                if (sellerId == null || sellerId.isEmpty()) {
                    Toast.makeText(OrderConfirmationActivity.this, "Seller ID not found in database.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(OrderConfirmationActivity.this, "Failed to fetch seller ID", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
