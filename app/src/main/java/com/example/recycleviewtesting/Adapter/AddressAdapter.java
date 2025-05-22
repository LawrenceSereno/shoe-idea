package com.example.recycleviewtesting.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycleviewtesting.Item.Address;
import com.example.recycleviewtesting.Item.WalkthroughItem;
import com.example.recycleviewtesting.R;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {

    private List<Address> addressList;
    private Context context;
    private String selectedAddressId;
    private OnAddressSelectedListener listener;

    public interface OnAddressSelectedListener {
        void onAddressSelected(Address address);
    }

    public AddressAdapter(Context context, List<Address> addresses, String selectedAddressId, OnAddressSelectedListener listener) {
        this.context = context;
        this.addressList = addresses;
        this.selectedAddressId = selectedAddressId;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addressList.get(position);
        holder.name.setText(address.getName());
        holder.phone.setText(address.getPhone());
        holder.region.setText(address.getRegion());
        holder.street.setText(address.getStreet());
        holder.unit.setText(address.getUnit());
        holder.radioButton.setChecked(address.getId().equals(selectedAddressId));

        holder.radioButton.setOnClickListener(v -> {
            selectedAddressId = address.getId();
            notifyDataSetChanged();
            listener.onAddressSelected(address);
        });

        holder.itemView.setOnClickListener(v -> {
            selectedAddressId = address.getId();
            notifyDataSetChanged();
            listener.onAddressSelected(address);
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, region, street, unit;
        RadioButton radioButton;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            phone = itemView.findViewById(R.id.item_phone);
            region = itemView.findViewById(R.id.item_region);
            street = itemView.findViewById(R.id.item_street);
            unit = itemView.findViewById(R.id.item_unit);
            radioButton = itemView.findViewById(R.id.radio_button);
        }
    }

    public static class WalkthroughAdapter extends RecyclerView.Adapter<WalkthroughAdapter.WalkthroughViewHolder> {

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
}
