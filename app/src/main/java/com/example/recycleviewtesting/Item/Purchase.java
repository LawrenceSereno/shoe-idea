package com.example.recycleviewtesting.Item;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class Purchase {
    private Date purchaseDate;
    private List<Map<String, Object>> items;
    private String userId;

    // 🔧 Required empty constructor for Firebase
    public Purchase() {
    }

    // 🔧 Existing constructor without userId
    public Purchase(Date purchaseDate, List<Map<String, Object>> items) {
        this.purchaseDate = purchaseDate;
        this.items = items;
    }

    // ✅ New constructor WITH userId
    public Purchase(Date purchaseDate, List<Map<String, Object>> items, String userId) {
        this.purchaseDate = purchaseDate;
        this.items = items;
        this.userId = userId;
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

    public String getUserId() {
        return userId;
    }

    // ✅ Missing setter added
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
