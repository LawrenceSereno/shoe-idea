package com.example.recycleviewtesting.Item;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.Objects;

public class CartItem implements Parcelable {
    // 🔹 Your original fields
    private String id;
    private String productId;
    private String name;
    private String imageUrl;
    private String size;
    private double price;
    private int quantity;

    // 🔹 Your original default constructor
    public CartItem() {
    }

    // 🔹 Your original parameterized constructor
    public CartItem(String productId, String name, String imageUrl, String size, double price, int quantity) {
        this.productId = productId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.size = size;
        this.price = price;
        this.quantity = quantity;
    }

    // 🔹 Your original getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // 🔹 Your original total price method, now marked with @Exclude for Firebase
    @Exclude
    public double getTotalPrice() {
        return price * quantity;
    }

    // ✅ Added: Parcelable support so you can send CartItem through intents
    protected CartItem(Parcel in) {
        id = in.readString();
        productId = in.readString();
        name = in.readString();
        imageUrl = in.readString();
        size = in.readString();
        price = in.readDouble();
        quantity = in.readInt();
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(productId);
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeString(size);
        dest.writeDouble(price);
        dest.writeInt(quantity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // ✅ Added: equals() and hashCode() to compare items based on productId and size
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartItem)) return false;
        CartItem that = (CartItem) o;
        return Objects.equals(productId, that.productId) &&
                Objects.equals(size, that.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, size);
    }

    // ✅ Added: toString() for easy debugging
    @Override
    public String toString() {
        return "CartItem{" +
                "id='" + id + '\'' +
                ", productId='" + productId + '\'' +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
