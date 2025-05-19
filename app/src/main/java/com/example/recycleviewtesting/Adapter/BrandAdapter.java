package com.example.recycleviewtesting.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.recycleviewtesting.BrandModel;
import com.example.recycleviewtesting.R;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.BrandViewHolder> {

    private List<BrandModel> brandList;
    private Context context;

    public BrandAdapter(List<BrandModel> brandList, Context context) {
        this.brandList = brandList;
        this.context = context;
    }

    @NonNull
    @Override
    public BrandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_brand, parent, false);
        return new BrandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandViewHolder holder, int position) {
        BrandModel brand = brandList.get(position);
        Glide.with(context).load(brand.getImage()).into(holder.brandImage);
    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }

    public static class BrandViewHolder extends RecyclerView.ViewHolder {
        ImageView brandImage;

        public BrandViewHolder(@NonNull View itemView) {
            super(itemView);
            brandImage = itemView.findViewById(R.id.brandImage);
        }
    }
}
