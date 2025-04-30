package com.example.recycleviewtesting.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.recycleviewtesting.ChatMessage;
import com.example.recycleviewtesting.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends ArrayAdapter<ChatMessage> {

    private Context context;
    private List<ChatMessage> messages;
    private String buyerId;

    // Constructor
    public ChatAdapter(Context context, List<ChatMessage> messages, String buyerId) {
        super(context, 0, messages);
        this.context = context;
        this.messages = messages;
        this.buyerId = buyerId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage message = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.chat_message_item, parent, false);
        }

        ImageView profileImageView = convertView.findViewById(R.id.message_sender_image);
        TextView senderTextView = convertView.findViewById(R.id.message_sender_name);
        TextView messageTextView = convertView.findViewById(R.id.message_text);
        TextView timestampTextView = convertView.findViewById(R.id.message_timestamp);
        LinearLayout messageBubbleLayout = convertView.findViewById(R.id.message_bubble_layout);

        // Set the message sender's name and the message text
        senderTextView.setText(message.getSender());
        messageTextView.setText(message.getText());

        // Format timestamp
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String formattedTime = sdf.format(message.getTimestamp());
        timestampTextView.setText(formattedTime);

        // Set profile image (for demonstration, using a placeholder)
        Glide.with(context)
                .load("https://example.com/profile_image_url") // Replace with actual image URL logic
                .into(profileImageView);

        // Dynamically change gravity based on the sender (left for buyer, right for seller)
        if (message.getSender().equals(buyerId)) {
            // If the message sender is the buyer, align the message to the left
            messageBubbleLayout.setGravity(Gravity.START);
            profileImageView.setVisibility(View.VISIBLE);  // Show profile image for the buyer
        } else {
            // If the message sender is the seller, align the message to the right
            messageBubbleLayout.setGravity(Gravity.END);
            profileImageView.setVisibility(View.VISIBLE);  // Show profile image for the seller
        }

        return convertView;
    }
}
