package com.example.recycleviewtesting.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.recycleviewtesting.DetailActivity;
import com.example.recycleviewtesting.Product;
import com.example.recycleviewtesting.R;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<Product> productList;
    private Context context;

    public MyAdapter(List<Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product currentProduct = productList.get(position);

        // Set product name
        holder.titleText.setText(currentProduct.getName());

        // Set price
        if (currentProduct.getPrice() != null) {
            holder.priceText.setText("₱" + currentProduct.getPrice());
        } else {
            holder.priceText.setText("Price Unavailable");
        }

        // Set description
        if (currentProduct.getDescription() != null && !currentProduct.getDescription().isEmpty()) {
            holder.productDescription.setText(currentProduct.getDescription());
        } else {
            holder.productDescription.setText("No description available.");
        }

        // Load image
        Glide.with(context)
                .load(currentProduct.getImageUrl())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.casualshoes2)
                        .error(R.drawable.ic_menu_remove))
                .into(holder.itemImage);

        // Set click listener to go to DetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("productId", currentProduct.getId() != null ? currentProduct.getId() : "");
            intent.putExtra("name", currentProduct.getName());
            intent.putExtra("price", currentProduct.getPrice());
            intent.putExtra("description", currentProduct.getDescription());
            intent.putExtra("imageUrl", currentProduct.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titleText, priceText, productDescription;
        public ImageView itemImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            priceText = itemView.findViewById(R.id.priceText);
            productDescription = itemView.findViewById(R.id.productDescription);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }
}
