package com.example.recycleviewtesting.Activities;

import android.content.Intent;
/* import android.content.SharedPreferences; */ // 🔴 Old code commented
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycleviewtesting.Adapter.CartAdapter;
import com.example.recycleviewtesting.Address.MyAddressActivity;
import com.example.recycleviewtesting.Item.CartItem;
import com.example.recycleviewtesting.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.NumberFormat;
import java.util.*;

public class Cart_Activity extends AppCompatActivity implements CartAdapter.OnCartItemChangeListener {

    private RecyclerView cartRecyclerView;
    private View emptyCartView;
    private Button continueShoppingBtn, checkoutButton;
    private TextView subtotalTextView, deliveryFeeTextView, discountTextView, totalTextView;

    private List<CartItem> cartItems = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference cartRef;
    private ValueEventListener cartListener;

    private CartAdapter cartAdapter;

    private static final double DELIVERY_FEE = 0.0;

    /* 🔴 Old SharedPreferences setup
    private static final String PREFS_NAME = "address_prefs";
    private static final String KEY_NAME = "name";
    private static final String KEY_USE_ADDRESS = "use_address";
    private SharedPreferences sharedPreferences;
    */

    private DatabaseReference addressRef; // ✅ Firebase address reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initializeViews();
        initializeFirebase();

        // sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE); // 🔴 Old code

        loadCartItems();

        findViewById(R.id.backButton).setOnClickListener(v -> onBackPressed());
        continueShoppingBtn.setOnClickListener(v -> onBackPressed());

        checkoutButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            addressRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        showNoAddressDialog();
                        return;
                    }

                    String name = snapshot.child("name").getValue(String.class);
                    String phone = snapshot.child("phone").getValue(String.class);
                    String region = snapshot.child("region").getValue(String.class);
                    String street = snapshot.child("street").getValue(String.class);

                    if (name != null && phone != null && region != null && street != null) {
                        showCheckoutBottomSheet(name);
                    } else {
                        showNoAddressDialog(); // Incomplete fields
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Cart_Activity.this, "Failed to fetch address: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // 🔴 OLD SharedPreferences logic
            /*
            String savedName = sharedPreferences.getString(KEY_NAME, null);
            boolean useAddress = sharedPreferences.getBoolean(KEY_USE_ADDRESS, true);

            if (savedName == null) {
                Toast.makeText(this, "Please add an address before checkout.", Toast.LENGTH_LONG).show();
                return;
            }

            if (!useAddress) {
                Toast.makeText(this, "Please select the address checkbox to use your saved address.", Toast.LENGTH_LONG).show();
                return;
            }

            showCheckoutBottomSheet();
            */
        });
    }

    private void initializeViews() {
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        emptyCartView = findViewById(R.id.emptyCartView);
        continueShoppingBtn = findViewById(R.id.continueShoppingBtn);
        checkoutButton = findViewById(R.id.checkoutButton);
        subtotalTextView = findViewById(R.id.subtotalTextView);
        deliveryFeeTextView = findViewById(R.id.deliveryFeeTextView);
        discountTextView = findViewById(R.id.discountTextView);
        totalTextView = findViewById(R.id.totalTextView);
    }

    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();
            cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userId);
            addressRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("address");
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadCartItems() {
        cartListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItems.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    CartItem item = snap.getValue(CartItem.class);
                    if (item != null) {
                        item.setId(snap.getKey());
                        cartItems.add(item);
                    }
                }

                updateUI();
                calculateTotals();
                setupRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Cart_Activity.this, "Error loading cart: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        cartRef.addValueEventListener(cartListener);
    }

    private void setupRecyclerView() {
        if (cartAdapter == null) {
            cartAdapter = new CartAdapter(this, cartItems, this);
            cartRecyclerView.setAdapter(cartAdapter);
        } else {
            cartAdapter.notifyDataSetChanged();
        }
    }

    private void updateUI() {
        boolean isEmpty = cartItems.isEmpty();
        cartRecyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        emptyCartView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        checkoutButton.setEnabled(!isEmpty);
    }

    private void calculateTotals() {
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getTotalPrice();
        }

        double discount = 0;
        double total = subtotal + DELIVERY_FEE - discount;

        Locale localePH = new Locale("en", "PH");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(localePH);
        currencyFormat.setCurrency(Currency.getInstance("PHP"));

        subtotalTextView.setText(currencyFormat.format(subtotal));
        deliveryFeeTextView.setText(currencyFormat.format(DELIVERY_FEE));
        discountTextView.setText(currencyFormat.format(discount));
        totalTextView.setText(currencyFormat.format(total));
    }

    private void showCheckoutBottomSheet(String name) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_checkout, null);
        bottomSheetDialog.setContentView(view);

        TextView confirmNameText = view.findViewById(R.id.confirmNameText);
        Button confirmBtn = view.findViewById(R.id.confirmCheckoutBtn);
        Button changeAddressBtn = view.findViewById(R.id.changeAddressBtn);

        confirmNameText.setText("Shipping to: " + name);

        confirmBtn.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();

            // 🔴 Old checkout transition
            /*
            Toast.makeText(this, "Checkout confirmed! Proceeding...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Cart_Activity.this, OrderConfirmationActivity.class);
            startActivity(intent);
            finish();
            */

            // ✅ Save to Firestore and clear cart
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Map<String, Object> purchaseData = new HashMap<>();
            purchaseData.put("userId", userId);
            purchaseData.put("purchaseDate", new Date());
            purchaseData.put("items", cartItems);

            firestore.collection("Purchases")
                    .add(purchaseData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(Cart_Activity.this, "Purchase saved successfully!", Toast.LENGTH_SHORT).show();
                        FirebaseDatabase.getInstance().getReference("carts").child(userId).removeValue(); // ✅ clear cart
                        startActivity(new Intent(Cart_Activity.this, OrderConfirmationActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Cart_Activity.this, "Error saving purchase: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        changeAddressBtn.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            startActivity(new Intent(Cart_Activity.this, MyAddressActivity.class));
        });

        bottomSheetDialog.show();
    }

    private void showNoAddressDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.no_address_dialog, null);
        dialog.setContentView(view);

        Button addAddressBtn = view.findViewById(R.id.addAddressBtn);
        addAddressBtn.setOnClickListener(v -> {
            dialog.dismiss();
            startActivity(new Intent(Cart_Activity.this, com.example.recycleviewtesting.Address.AddAddressActivity.class));
        });

        dialog.show();
    }

    @Override
    public void onCartChanged() {
        calculateTotals();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cartRef != null && cartListener != null) {
            cartRef.removeEventListener(cartListener);
        }
    }
}
