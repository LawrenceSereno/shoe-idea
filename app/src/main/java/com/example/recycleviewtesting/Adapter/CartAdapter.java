package com.example.recycleviewtesting.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recycleviewtesting.R;
import com.example.recycleviewtesting.Item.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private OnCartItemChangeListener listener;

    // Firebase references
    private DatabaseReference cartRef;
    private String userId;

    public interface OnCartItemChangeListener {
        void onCartChanged();
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartItemChangeListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;

        // Initialize Firebase references
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userId);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        // Load product image
        Glide.with(context)
                .load(item.getImageUrl())
                .into(holder.productImage);

        // Set text fields
        holder.productName.setText(item.getName());
        holder.productSize.setText("Size: " + item.getSize());

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        holder.productPrice.setText(currencyFormat.format(item.getPrice()));
        holder.quantityTextView.setText(String.valueOf(item.getQuantity()));

        // Set click listeners
        holder.decreaseQuantityButton.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                updateItemQuantity(item, item.getQuantity() - 1);
            }
        });

        holder.increaseQuantityButton.setOnClickListener(v -> {
            updateItemQuantity(item, item.getQuantity() + 1);
        });

        holder.deleteButton.setOnClickListener(v -> {
            removeItem(item);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    private void updateItemQuantity(CartItem item, int newQuantity) {
        // Update in Firebase
        DatabaseReference itemRef = cartRef.child(item.getId());
        itemRef.child("quantity").setValue(newQuantity)
                .addOnSuccessListener(aVoid -> {
                    // Update local item
                    item.setQuantity(newQuantity);
                    notifyDataSetChanged();
                    if (listener != null) {
                        listener.onCartChanged();
                    }
                });
    }

    private void removeItem(CartItem item) {
        // Remove from Firebase
        DatabaseReference itemRef = cartRef.child(item.getId());
        itemRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Remove from local list
                    int position = cartItems.indexOf(item);
                    if (position != -1) {
                        cartItems.remove(position);
                        notifyItemRemoved(position);
                        if (listener != null) {
                            listener.onCartChanged();
                        }
                    }
                });
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productSize, productPrice, quantityTextView;
        ImageButton decreaseQuantityButton, increaseQuantityButton, deleteButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productSize = itemView.findViewById(R.id.productSize);
            productPrice = itemView.findViewById(R.id.productPrice);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
