package com.example.recycleviewtesting.Item;

public class BrandModel {
    private String image; // or whatever your brand holds (name, logo URL, etc.)

    public BrandModel() { } // empty constructor required by Firebase

    public BrandModel(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
