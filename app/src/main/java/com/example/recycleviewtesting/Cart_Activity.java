package com.example.recycleviewtesting;

import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class Cart_Activity extends AppCompatActivity implements CartAdapter.OnCartItemChangeListener {

    private RecyclerView cartRecyclerView;
    private View emptyCartView;
    private Button continueShoppingBtn, checkoutButton,editAddressButton;
    private TextView subtotalTextView, deliveryFeeTextView, discountTextView, totalTextView;

    private List<CartItem> cartItems = new ArrayList<>();
    private FirebaseAuth mAuth;
    private DatabaseReference cartRef;
    private ValueEventListener cartListener;

    private CartAdapter cartAdapter;

    private static final double DELIVERY_FEE = 0.0;

    // 🔐 Address preference keys (used in MyAddressActivity)
    private static final String PREFS_NAME = "address_prefs";
    private static final String KEY_NAME = "name";
    private static final String KEY_USE_ADDRESS = "use_address";

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize views and Firebase connections
        initializeViews();
        initializeFirebase();

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        loadCartItems();

        // Back button and continue shopping just go back
        findViewById(R.id.backButton).setOnClickListener(v -> onBackPressed());
        continueShoppingBtn.setOnClickListener(v -> onBackPressed());


        // Checkout button logic with validation
        checkoutButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }

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

            Toast.makeText(this, "Proceeding to checkout...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Cart_Activity.this, MyAddressActivity.class));
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

        double discount = 0; // Coupon or promo logic can go here later
        double total = subtotal + DELIVERY_FEE - discount;

        Locale localePH = new Locale("en", "PH");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(localePH);
        currencyFormat.setCurrency(Currency.getInstance("PHP"));

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
        if (cartRef != null && cartListener != null) {
            cartRef.removeEventListener(cartListener);
        }
    }
}

/* --------------- Your original code (commented out) ---------------

package com.example.recycleviewtesting;

import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Currency;
import java.util.List;
import java.util.Locale;

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

    // 🔐 Address preference keys (used in MyAddressActivity)
    private static final String PREFS_NAME = "address_prefs";
    private static final String KEY_NAME = "name";
    private static final String KEY_USE_ADDRESS = "use_address";

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // ✅ 1. Initialize views and Firebase
        initializeViews();
        initializeFirebase();

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // ✅ 2. Load all cart items from Firebase
        loadCartItems();

        // 🧠 Back button and continue shopping both just go back to the previous activity
        findViewById(R.id.backButton).setOnClickListener(v -> onBackPressed());
        continueShoppingBtn.setOnClickListener(v -> onBackPressed());

        // ✅ 3. Checkout logic
        checkoutButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // ✅ Validate if address is available and selected
            String savedName = sharedPreferences.getString(KEY_NAME, null);
            boolean useAddress = sharedPreferences.getBoolean(KEY_USE_ADDRESS, true); // default to true

            if (savedName == null) {
                Toast.makeText(this, "Please add an address before checkout.", Toast.LENGTH_LONG).show();
                return;
            }

            if (!useAddress) {
                Toast.makeText(this, "Please select the address checkbox to use your saved address.", Toast.LENGTH_LONG).show();
                return;
            }

            // ✅ Everything checks out
            Toast.makeText(this, "Proceeding to checkout...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Cart_Activity.this, MyAddressActivity.class));
        });
    }

    // ✅ Breaks down setup of all the views cleanly
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

    // ✅ Only allows Firebase access if the user is authenticated
    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();
            cartRef = FirebaseDatabase.getInstance().getReference("carts").child(userId);
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            finish(); // 🔐 Avoid illegal access if no user is logged in
        }
    }

    // 🔄 Loads items from Firebase and listens for real-time updates
    private void loadCartItems() {
        cartListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartItems.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    CartItem item = snap.getValue(CartItem.class);
                    if (item != null) {
                        item.setId(snap.getKey()); // 📌 Save Firebase key for item reference
                        cartItems.add(item);
                    }
                }

                updateUI();           // 📊 Show empty state or list
                calculateTotals();    // 💵 Calculate price, delivery, etc.
                setupRecyclerView();  // 🔁 Hook up adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Cart_Activity.this, "Error loading cart: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        cartRef.addValueEventListener(cartListener);
    }

    // ⚙️ Setup or refresh RecyclerView adapter
    private void setupRecyclerView() {
        if (cartAdapter == null) {
            cartAdapter = new CartAdapter(this, cartItems, this);
            cartRecyclerView.setAdapter(cartAdapter);
        } else {
            cartAdapter.notifyDataSetChanged();
        }
    }

    // 🔄 Updates UI state depending on if cart is empty
    private void updateUI() {
        boolean isEmpty = cartItems.isEmpty();
        cartRecyclerView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        emptyCartView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        checkoutButton.setEnabled(!isEmpty);
    }

    // 💰 Core price calculation
    private void calculateTotals() {
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getTotalPrice(); // 🧮 Uses price * quantity from each CartItem
        }

        double discount = 0; // 🤑 (Optional: implement coupon logic here later)
        double total = subtotal + DELIVERY_FEE - discount;

        // 💸 Format everything in PHP currency
        Locale localePH = new Locale("en", "PH");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(localePH);
        currencyFormat.setCurrency(Currency.getInstance("PHP"));

        subtotalTextView.setText(currencyFormat.format(subtotal));
        deliveryFeeTextView.setText(currencyFormat.format(DELIVERY_FEE));
        discountTextView.setText(currencyFormat.format(discount));
        totalTextView.setText(currencyFormat.format(total));
    }

    // 🔁 Called by CartAdapter when items are changed
    @Override
    public void onCartChanged() {
        calculateTotals();
    }

    // 🔚 Properly removes listener to avoid memory leaks
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cartRef != null && cartListener != null) {
            cartRef.removeEventListener(cartListener);
        }
    }
}
------------------------------------------------------------------ */
