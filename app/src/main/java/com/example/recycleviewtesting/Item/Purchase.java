package com.example.recycleviewtesting.Item;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Purchase {
    private Date purchaseDate;
    private List<Map<String, Object>> items;

    public Purchase() {
        // Required empty constructor
    }

    public Purchase(Date purchaseDate, List<Map<String, Object>> items) {
        this.purchaseDate = purchaseDate;
        this.items = items;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public List<Map<String, Object>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, Object>> items) {
        this.items = items;
    }
}
