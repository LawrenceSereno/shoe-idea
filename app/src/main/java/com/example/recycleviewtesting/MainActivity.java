package com.example.recycleviewtesting;

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
    private List<Product> allProducts;  // For filtering
    private List<CategoryModel> categoryList;
    private List<BrandModel> brandList; // Changed to BrandModel list

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Your layout must have these RecyclerViews

        recyclerView = findViewById(R.id.recyclerView);
        brandsRecyclerView = findViewById(R.id.brandsRecyclerView);
        categoriesRecyclerView = findViewById(R.id.categoriesRecyclerView);

        // Setup products RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productList = new ArrayList<>();
        allProducts = new ArrayList<>();
        adapter = new MyAdapter(productList, this);
        recyclerView.setAdapter(adapter);

        // Setup brands RecyclerView
        brandsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        brandList = new ArrayList<>();
        brandAdapter = new BrandAdapter(brandList, this);
        brandsRecyclerView.setAdapter(brandAdapter);
        loadBrandsFromFirebase();

        // Setup categories RecyclerView
        categoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryList = getDummyCategories();
        categoryAdapter = new CategoryAdapter(categoryList, this);
        categoriesRecyclerView.setAdapter(categoryAdapter);

        // Firebase reference for products
        mDatabase = FirebaseDatabase.getInstance().getReference("products");

        loadProducts();
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
        list.add(new CategoryModel("Sneakers"));
        list.add(new CategoryModel("Running"));
        list.add(new CategoryModel("Casual"));
        list.add(new CategoryModel("Formal"));
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
