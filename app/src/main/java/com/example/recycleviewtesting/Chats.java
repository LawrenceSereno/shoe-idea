package com.example.recycleviewtesting;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.recycleviewtesting.Adapter.ChatAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import java.util.ArrayList;
// Chats.java

public class Chats extends AppCompatActivity {

    private ListView chatListView;
    private TextView chatTitle;
    private String sellerId;
    private String buyerId;

    private DatabaseReference chatRef;
    private ArrayList<ChatMessage> messages;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        chatListView = findViewById(R.id.chat_list_view);
        chatTitle = findViewById(R.id.chat_title);

        // Get the sellerId passed from OrderConfirmationActivity
        sellerId = getIntent().getStringExtra("sellerId");

        // Get the buyerId (current user's ID)
        buyerId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Check if sellerId is passed correctly
        if (sellerId == null || sellerId.isEmpty()) {
            Toast.makeText(this, "Seller ID is missing", Toast.LENGTH_SHORT).show();
            finish(); // Close this activity if sellerId is missing
            return;
        }

        // Update the chat title to show the seller's information
        chatTitle.setText("Chat with Seller: " + sellerId);

        // Set up Firebase Database reference to the correct chat path
        chatRef = FirebaseDatabase.getInstance()
                .getReference("chats")
                .child(getChatId(buyerId, sellerId));

        // Initialize the messages list and chat adapter
        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, messages, buyerId);
        chatListView.setAdapter(chatAdapter);

        // Load the chat messages from Firebase
        loadMessages();
    }

    // Function to load chat messages from Firebase
    private void loadMessages() {
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    ChatMessage msg = data.getValue(ChatMessage.class);
                    messages.add(msg);
                }
                chatAdapter.notifyDataSetChanged();
                chatListView.setSelection(chatAdapter.getCount() - 1);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Chats.this, "Failed to load chat", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper function to get a unique chat ID between buyer and seller
    private String getChatId(String user1, String user2) {
        return user1.compareTo(user2) < 0 ? user1 + "_" + user2 : user2 + "_" + user1;
    }
}
