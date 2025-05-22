package com.example.recycleviewtesting.Item;

public class Product {
    private String name;
    private Double price;
    private String description;
    private String imageUrl;
    private String category;

    // Default constructor needed for Firebase
    public Product() {
    }

    // Full constructor with category
    public Product(String name, Double price, String description, String imageUrl, String category) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    // Optional: constructor without category (if you want)
    public Product(String name, Double price, String description, String imageUrl) {
        this(name, price, description, imageUrl, "Uncategorized");
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
