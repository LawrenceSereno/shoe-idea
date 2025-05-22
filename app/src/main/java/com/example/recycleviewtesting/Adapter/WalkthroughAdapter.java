package com.example.recycleviewtesting.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycleviewtesting.Item.WalkthroughItem;
import com.example.recycleviewtesting.R;

import java.util.List;

public class WalkthroughAdapter extends RecyclerView.Adapter<WalkthroughAdapter.WalkthroughViewHolder> {

    private List<WalkthroughItem> walkthroughItems;

    public WalkthroughAdapter(List<WalkthroughItem> walkthroughItems) {
        this.walkthroughItems = walkthroughItems;
    }

    @NonNull
    @Override
    public WalkthroughViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WalkthroughViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_walkthrough,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull WalkthroughViewHolder holder, int position) {
        holder.setWalkthroughData(walkthroughItems.get(position));
    }

    @Override
    public int getItemCount() {
        return walkthroughItems.size();
    }

    class WalkthroughViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageSlide;
        private TextView textTitle;

        WalkthroughViewHolder(@NonNull View itemView) {
            super(itemView);
            imageSlide = itemView.findViewById(R.id.imageSlide);
            textTitle = itemView.findViewById(R.id.textTitle);
        }

        void setWalkthroughData(WalkthroughItem walkthroughItem) {
            imageSlide.setImageResource(walkthroughItem.getImage());
            textTitle.setText(walkthroughItem.getTitle());
        }
    }
}