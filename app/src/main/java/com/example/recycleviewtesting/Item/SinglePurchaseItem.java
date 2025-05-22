package com.example.recycleviewtesting.Item;


public class SinglePurchaseItem {
    private String productName;
    private String productImage;
    private double price;
    private int quantity;
    private String userId;
    private String orderId;
    private long timestamp;

    public SinglePurchaseItem() {
        // Required empty constructor
    }

    public SinglePurchaseItem(String productName, String productImage, double price, int quantity,
                              String userId, String orderId, long timestamp) {
        this.productName = productName;
        this.productImage = productImage;
        this.price = price;
        this.quantity = quantity;
        this.userId = userId;
        this.orderId = orderId;
        this.timestamp = timestamp;
    }

    // Add getters and setters for Firestore if needed
}
