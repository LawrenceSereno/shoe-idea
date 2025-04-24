package com.example.recycleviewtesting.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recycleviewtesting.DetailActivity;
import com.example.recycleviewtesting.Item.MyItem;
import com.example.recycleviewtesting.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<MyItem> itemList;
    private Context context;

    public MyAdapter(List<MyItem> itemList) {
        this.itemList = itemList;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView titleText, priceText;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.itemImage);
            titleText = itemView.findViewById(R.id.titleText);
            priceText = itemView.findViewById(R.id.priceText);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext(); // Store context for later use
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyItem item = itemList.get(position);
        holder.itemImage.setImageResource(item.getImageResId());
        holder.titleText.setText(item.getTitle());
        holder.priceText.setText(item.getPrice());

        // Add click listener to the item view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to open DetailActivity
                Intent intent = new Intent(context, DetailActivity.class);

                // Pass data to DetailActivity
                intent.putExtra("PRODUCT_TITLE", item.getTitle());
                intent.putExtra("PRODUCT_PRICE", item.getPrice());
                intent.putExtra("PRODUCT_IMAGE", item.getImageResId());

                // Start the DetailActivity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}