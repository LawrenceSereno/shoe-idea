package com.example.recycleviewtesting;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recycleviewtesting.Adapter.MyAdapter;
import com.example.recycleviewtesting.Item.MyItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private List<MyItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        GridLayoutManager layoutManager= new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        itemList = new ArrayList<>();
        itemList.add(new MyItem("Nike Air Zoom", "₱5,499", R.drawable.casualshoes2));
        itemList.add(new MyItem("Adidas Ultraboost", "₱6,299", R.drawable.casualshoes3));
        itemList.add(new MyItem("Vans Classic", "₱3,199", R.drawable.casualshoes2));
        itemList.add(new MyItem("Vans Classic", "₱3,199", R.drawable.casualshoes2));

        adapter = new MyAdapter(itemList);
        recyclerView.setAdapter(adapter);
    }
}
