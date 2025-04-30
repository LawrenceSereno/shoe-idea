package com.example.recycleviewtesting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycleviewtesting.Adapter.CartAdapter;
import com.example.recycleviewtesting.Item.CartItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart_Activity extends AppCompatActivity implements CartAdapter.OnCartItemChangeListener {

    private RecyclerView cartRecyclerView;
    private View emptyCartView;
    private Button continueShoppingBtn;
    private Button checkoutButton;
    private TextView subtotalTextView, deliveryFeeTextView, discountTextView, totalTextView;

    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference cartRef;
    private ValueEventListener cartListener;

    // Constants
    private static final double DELIVERY_FEE = 0.00; // Free delivery in this example

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initializeViews();
        initializeFirebase();
        loadCartItems();

        // Set up onClick listeners
        findViewById(R.id.backButton).setOnClickListener(v -> onBackPressed());

        continueShoppingBtn.setOnClickListener(v -> {
            // Go back to shopping activity
            onBackPressed();
        });

        checkoutButton.setOnClickListener(v -> {
            // Handle checkout process
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Cart_Activity.this, AddAddressActivity.class);
                startActivity(intent);  // Start the AddAddressActivity
                // Implement checkout process here
                Toast.makeText(this, "Proceeding to checkout...", Toast.LENGTH_SHORT).show();
                // You can start a new activity for checkout process
            }
        });
    }

    private void initializeViews() {
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        emptyCartView = findViewById(R.id.emptyCartView);
        continueShoppingBtn = findViewById(R.id.continueShoppingBtn);
        checkoutButton = findViewById(R.id.checkoutButton);
        subtotalTextView = findViewById(R.id.subtotalTextView);
        deliveryFeeTextView = findViewById(R.id.deliveryFeeTextView);
        discountTextView = findViewById(R.id.discountTextView);
        totalTextView = findViewById(R.id.totalTextView);

        // Set up RecyclerView
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItems = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartItems, this);
        cartRecyclerView.setAdapter(cartAdapter);
    }

    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userId);
    }

    private void loadCartItems() {
        cartListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartItems.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    CartItem item = snapshot.getValue(CartItem.class);
                    if (item != null) {
                        item.setId(snapshot.getKey());
                        cartItems.add(item);
                    }
                }

                cartAdapter.notifyDataSetChanged();
                updateUI();
                calculateTotals();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Cart_Activity.this, "Failed to load cart: " + databaseError.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        };

        cartRef.addValueEventListener(cartListener);
    }

    private void updateUI() {
        if (cartItems.isEmpty()) {
            cartRecyclerView.setVisibility(View.GONE);
            emptyCartView.setVisibility(View.VISIBLE);
        } else {
            cartRecyclerView.setVisibility(View.VISIBLE);
            emptyCartView.setVisibility(View.GONE);
        }
    }

    private void calculateTotals() {
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getTotalPrice();
        }

        // Calculate other values (in this example, no discount)
        double discount = 0;
        double total = subtotal + DELIVERY_FEE - discount;

        // Format as currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        subtotalTextView.setText(currencyFormat.format(subtotal));
        deliveryFeeTextView.setText(currencyFormat.format(DELIVERY_FEE));
        discountTextView.setText(currencyFormat.format(discount));
        totalTextView.setText(currencyFormat.format(total));
    }

    @Override
    public void onCartChanged() {
        calculateTotals();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Remove Firebase listener to avoid memory leaks
        if (cartRef != null && cartListener != null) {
            cartRef.removeEventListener(cartListener);
        }
    }
}
