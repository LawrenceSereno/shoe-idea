package com.example.recycleviewtesting.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recycleviewtesting.Activities.DetailActivity;
import com.example.recycleviewtesting.Item.Product;
import com.example.recycleviewtesting.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Product> productList;
    private Context context;

    public MyAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.titleText.setText(product.getName());

        if (product.getPrice() != null) {
            String priceFormatted = String.format("₱%,.0f", product.getPrice());
            holder.priceText.setText(priceFormatted);
        } else {
            holder.priceText.setText("Price N/A");
        }

        // ❌ Removed description binding
        // holder.descriptionText.setText(product.getDescription() != null ? product.getDescription() : "No description");

        Glide.with(context)
                .load(product.getImageUrl())
                .placeholder(R.drawable.check)
                .error(R.drawable.close)
                .into(holder.itemImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("name", product.getName());
            intent.putExtra("price", product.getPrice());
            intent.putExtra("description", product.getDescription()); // optional
            intent.putExtra("imageUrl", product.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, priceText;
        ImageView itemImage, arrowIcon;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            priceText = itemView.findViewById(R.id.priceText);

            // ❌ Commented out since it's removed from XML
            // descriptionText = itemView.findViewById(R.id.productDescription);

            itemImage = itemView.findViewById(R.id.itemImage);
            arrowIcon = itemView.findViewById(R.id.arrowIcon);
        }
    }
}
