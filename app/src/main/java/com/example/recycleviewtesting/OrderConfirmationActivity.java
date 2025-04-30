package com.example.recycleviewtesting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
// OrderConfirmationActivity.java

public class OrderConfirmationActivity extends AppCompatActivity {

    private TextView confirmationText;
    private Button chatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        confirmationText = findViewById(R.id.confirmation_text);
        chatButton = findViewById(R.id.chat_button);

        Intent intent = getIntent();
        UserAddress userAddress = (UserAddress) intent.getSerializableExtra("userAddress");
        String sellerId = intent.getStringExtra("sellerId");  // Make sure sellerId is passed

        if (userAddress != null) {
            String confirmationMessage = "Order Confirmed!\n\n" +
                    "Name: " + userAddress.getName() + "\n" +
                    "Phone: " + userAddress.getPhone() + "\n" +
                    "Address: " + userAddress.getRegion() + ", " + userAddress.getStreet() + " " + userAddress.getUnit();
            confirmationText.setText(confirmationMessage);
        }

        // Handle chat button click
        chatButton.setOnClickListener(view -> {
            Intent chatIntent = new Intent(OrderConfirmationActivity.this, Chats.class);
            chatIntent.putExtra("sellerId", sellerId);  // Pass sellerId to chat activity
            startActivity(chatIntent);
        });
    }
}
