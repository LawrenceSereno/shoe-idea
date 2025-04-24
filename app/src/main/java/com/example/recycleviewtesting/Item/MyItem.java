package com.example.recycleviewtesting.Item;

public class MyItem {
    private String title;
    private String price;
    private int imageResId;

    public MyItem(String title, String price, int imageResId) {
        this.title = title;
        this.price = price;
        this.imageResId = imageResId;
    }

    public String getTitle() { return title; }

    public String getPrice() { return price; }

    public int getImageResId() { return imageResId; }
}
