package com.example.recycleviewtesting.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.recycleviewtesting.Item.Purchase;
import com.example.recycleviewtesting.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder> {

    private final List<Purchase> purchases;

    public PurchaseAdapter(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    @NonNull
    @Override
    public PurchaseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_purchase_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseAdapter.ViewHolder holder, int position) {
        Purchase purchase = purchases.get(position);

        // Format and set the purchase date
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
        holder.dateText.setText("Purchased on: " + sdf.format(purchase.getPurchaseDate()));

        // Build item details string with null safety
        StringBuilder itemDetails = new StringBuilder();

        List<Map<String, Object>> items = purchase.getItems();
        if (items != null) {
            for (Map<String, Object> item : items) {
                String name = item.get("productName") != null ? item.get("productName").toString() : null;
                Number quantityNum = (Number) item.get("quantity");
                Number priceNum = (Number) item.get("price");

                if (name == null || quantityNum == null || priceNum == null) {
                    Log.w("PurchaseAdapter", "Skipping invalid item: " + item.toString());
                    continue;
                }

                long quantity = quantityNum.longValue();
                double price = priceNum.doubleValue();

                itemDetails.append("- ").append(name)
                        .append(" (x").append(quantity)
                        .append(") @ ₱").append(String.format("%.2f", price))
                        .append("\n");
            }
        } else {
            itemDetails.append("No item data available.");
        }

        holder.itemsText.setText(itemDetails.toString().trim());
    }

    @Override
    public int getItemCount() {
        return purchases.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateText, itemsText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.purchaseDateText);
            itemsText = itemView.findViewById(R.id.purchaseItemsText);
        }
    }
}
