package com.example.recycleviewtesting;

public class Product {
    private String id;
    private String name;
    private Long price; // Storing price as Long
    private String description;
    private String imageUrl;

    // ✅ True empty constructor for Firebase
    public Product() {
        // Required empty constructor for Firebase
    }

    // ✅ Main constructor with correct parameter types
    public Product(String name, Long price, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // ✅ Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
