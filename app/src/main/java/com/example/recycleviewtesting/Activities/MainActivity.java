package com.example.recycleviewtesting.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycleviewtesting.Adapter.BrandAdapter;
import com.example.recycleviewtesting.Adapter.CategoryAdapter;
import com.example.recycleviewtesting.Adapter.MyAdapter;
import com.example.recycleviewtesting.Item.BrandModel;
import com.example.recycleviewtesting.Item.CategoryModel;
import com.example.recycleviewtesting.Item.Product;
import com.example.recycleviewtesting.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.OnCategoryClickListener {

    private RecyclerView recyclerView, brandsRecyclerView, categoriesRecyclerView;
    private MyAdapter adapter;
    private BrandAdapter brandAdapter;
    private CategoryAdapter categoryAdapter;

    private List<Product> productList;
    private List<Product> allProducts;
    private List<CategoryModel> categoryList;
    private List<BrandModel> brandList;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        brandsRecyclerView = findViewById(R.id.brandsRecyclerView);
        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);

        // Setup product grid
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productList = new ArrayList<>();
        allProducts = new ArrayList<>();
        adapter = new MyAdapter(productList, this);
        recyclerView.setAdapter(adapter);

        // Setup brand list
        brandsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        brandList = new ArrayList<>();
        brandAdapter = new BrandAdapter(brandList, this);
        brandsRecyclerView.setAdapter(brandAdapter);
        loadBrandsFromFirebase();

        // Setup categories
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryList = getDummyCategories();
        categoryAdapter = new CategoryAdapter(categoryList, this);
        categoriesRecyclerView.setAdapter(categoryAdapter);

        // Load products from Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("products");
        loadProducts();

        // Setup bottom navigation
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Optional: set the default selected item
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

           if (itemId == R.id.nav_shop) {
                startActivity(new Intent(MainActivity.this, Cart_Activity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(MainActivity.this, Profile.class));
                return true;
            } else {
                return false;
            }
        });
    }

    private void loadProducts() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                allProducts.clear();

                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    Product product = itemSnapshot.getValue(Product.class);
                    if (product != null) {
                        if (product.getCategory() == null) {
                            product.setCategory("Uncategorized");
                        }
                        productList.add(product);
                        allProducts.add(product);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load products.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBrandsFromFirebase() {
        DatabaseReference brandRef = FirebaseDatabase.getInstance().getReference("brands");
        brandRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                brandList.clear();
                for (DataSnapshot brandSnapshot : snapshot.getChildren()) {
                    BrandModel brand = brandSnapshot.getValue(BrandModel.class);
                    if (brand != null) {
                        brandList.add(brand);
                    }
                }
                brandAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load brands.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<CategoryModel> getDummyCategories() {
        List<CategoryModel> list = new ArrayList<>();
        list.add(new CategoryModel("All"));
        list.add(new CategoryModel("Air Jordan"));
        list.add(new CategoryModel("Women's"));
        list.add(new CategoryModel("Men's"));
        list.add(new CategoryModel("Running"));
        list.add(new CategoryModel("Sports"));
        return list;
    }

    @Override
    public void onCategoryClick(String categoryName) {
        if (categoryName.equalsIgnoreCase("All")) {
            productList.clear();
            productList.addAll(allProducts);
        } else {
            List<Product> filteredList = new ArrayList<>();
            for (Product product : allProducts) {
                if (product.getCategory() != null && product.getCategory().equalsIgnoreCase(categoryName)) {
                    filteredList.add(product);
                }
            }
            productList.clear();
            productList.addAll(filteredList);
        }
        adapter.notifyDataSetChanged();
    }
}
