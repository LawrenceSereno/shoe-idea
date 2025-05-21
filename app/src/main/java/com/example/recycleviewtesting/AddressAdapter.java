package com.example.recycleviewtesting;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.name.setText(address.name);
        holder.phone.setText(address.phone);
        holder.region.setText(address.region);
        holder.street.setText(address.street);
        holder.unit.setText(address.unit);
        holder.radioButton.setChecked(address.id.equals(selectedAddressId));

        holder.radioButton.setOnClickListener(v -> {
            selectedAddressId = address.id;
            notifyDataSetChanged(); // refresh to uncheck others
            listener.onAddressSelected(address);
        });

        // Optional: clicking the whole item also selects the address
        holder.itemView.setOnClickListener(v -> {
            selectedAddressId = address.id;
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
}
