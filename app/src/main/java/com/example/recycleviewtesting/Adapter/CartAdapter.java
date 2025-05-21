package com.example.recycleviewtesting.Adapter;

import android.app.AlertDialog;
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
import com.example.recycleviewtesting.Item.CartItem;
import com.example.recycleviewtesting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final Context context;
    private final List<CartItem> cartItemList;
    private final OnCartItemChangeListener cartChangeListener;

    public interface OnCartItemChangeListener {
        void onCartChanged();
    }

    public CartAdapter(Context context, List<CartItem> cartItemList, OnCartItemChangeListener listener) {
        this.context = context;
        this.cartItemList = cartItemList;
        this.cartChangeListener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);

        Glide.with(context).load(item.getImageUrl()).into(holder.imageView);
        holder.nameTextView.setText(item.getName());
        holder.sizeTextView.setText("Size: " + item.getSize());
        holder.quantityTextView.setText(String.valueOf(item.getQuantity()));

        Locale localePH = new Locale("en", "PH");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(localePH);
        currencyFormat.setCurrency(Currency.getInstance("PHP"));

        double totalPrice = item.getPrice() * item.getQuantity();
        holder.priceTextView.setText(currencyFormat.format(totalPrice));

        // Disable subtract if quantity is 1
        holder.subtractQuantityButton.setEnabled(item.getQuantity() > 1);

        holder.addQuantityButton.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            // updateItemQuantity(item, newQuantity); // 🔴 OLD
            updateItemQuantity(item, newQuantity, holder.getAdapterPosition()); // ✅ NEW
        });

        holder.subtractQuantityButton.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() - 1;
            if (newQuantity >= 1) {
                // updateItemQuantity(item, newQuantity); // 🔴 OLD
                updateItemQuantity(item, newQuantity, holder.getAdapterPosition()); // ✅ NEW
            }
        });

        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Remove Item")
                    .setMessage("Are you sure you want to remove this item from your cart?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // deleteItem(item); // 🔴 OLD
                        deleteItem(item, holder.getAdapterPosition()); // ✅ NEW
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    // 🔴 OLD
    /*
    private void updateItemQuantity(CartItem item, int newQuantity) {
        item.setQuantity(newQuantity);
        notifyDataSetChanged();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("carts")
                .child(userId)
                .child(item.getId())
                .setValue(item);

        if (cartChangeListener != null) cartChangeListener.onCartChanged();
    }
    */

    // ✅ NEW
    private void updateItemQuantity(CartItem item, int newQuantity, int position) {
        item.setQuantity(newQuantity);
        notifyItemChanged(position); // More efficient

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        FirebaseDatabase.getInstance().getReference("carts")
                .child(currentUser.getUid())
                .child(item.getId())
                .setValue(item);

        if (cartChangeListener != null) cartChangeListener.onCartChanged();
    }

    // 🔴 OLD
    /*
    private void deleteItem(CartItem item) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("carts")
                .child(userId)
                .child(item.getId())
                .removeValue();

        cartItemList.remove(item);
        notifyDataSetChanged();

        if (cartChangeListener != null) cartChangeListener.onCartChanged();
    }
    */

    // ✅ NEW
    private void deleteItem(CartItem item, int position) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        FirebaseDatabase.getInstance().getReference("carts")
                .child(currentUser.getUid())
                .child(item.getId())
                .removeValue();

        cartItemList.remove(position); // Safer and more accurate than remove(item)
        notifyItemRemoved(position);

        if (cartChangeListener != null) cartChangeListener.onCartChanged();
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView nameTextView, sizeTextView, priceTextView, quantityTextView;
        ImageButton addQuantityButton, subtractQuantityButton, deleteButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.productImage);
            nameTextView = itemView.findViewById(R.id.productName);
            sizeTextView = itemView.findViewById(R.id.productSize);
            priceTextView = itemView.findViewById(R.id.productPrice);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            addQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            subtractQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public void updateList(List<CartItem> newList) {
        cartItemList.clear();
        cartItemList.addAll(newList);
        notifyDataSetChanged();
    }
}
