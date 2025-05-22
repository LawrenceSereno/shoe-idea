package com.example.recycleviewtesting.Activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycleviewtesting.Item.Purchase;
import com.example.recycleviewtesting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;
import java.text.SimpleDateFormat;
import java.util.*;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PurchaseHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Purchase> purchaseList = new ArrayList<>();
    private PurchaseAdapter adapter;

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);

        recyclerView = findViewById(R.id.purchaseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PurchaseAdapter(purchaseList);
        recyclerView.setAdapter(adapter);

        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        fetchPurchases();
    }

    private void fetchPurchases() {
        String userId = mAuth.getCurrentUser().getUid();

        firestore.collection("Purchases")
                .whereEqualTo("userId", userId)
                .orderBy("purchaseDate", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    purchaseList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Purchase purchase = doc.toObject(Purchase.class);
                        purchaseList.add(purchase);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching purchases: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    static class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder> {
        private final List<Purchase> purchaseList;

        public PurchaseAdapter(List<Purchase> purchaseList) {
            this.purchaseList = purchaseList;
        }

        @NonNull
        @Override
        public PurchaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_purchase_history, parent, false);
            return new PurchaseViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PurchaseViewHolder holder, int position) {
            Purchase purchase = purchaseList.get(position);
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
            holder.dateText.setText("Date: " + sdf.format(purchase.getPurchaseDate()));

            StringBuilder itemsBuilder = new StringBuilder();
            for (Map<String, Object> item : purchase.getItems()) {
                String name = (String) item.get("productName");
                Long quantity = ((Number) item.get("quantity")).longValue();
                Double price = ((Number) item.get("price")).doubleValue();
                itemsBuilder.append(name).append(" - Qty: ").append(quantity)
                        .append(" - ₱").append(price).append("\n");
            }
            holder.itemsText.setText(itemsBuilder.toString().trim());
        }

        @Override
        public int getItemCount() {
            return purchaseList.size();
        }

        static class PurchaseViewHolder extends RecyclerView.ViewHolder {
            TextView dateText, itemsText;

            public PurchaseViewHolder(@NonNull View itemView) {
                super(itemView);
                dateText = itemView.findViewById(R.id.purchaseDateText);
                itemsText = itemView.findViewById(R.id.purchaseItemsText);
            }
        }
    }
}
