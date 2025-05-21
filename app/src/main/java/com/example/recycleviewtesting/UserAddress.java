package com.example.recycleviewtesting;

public class UserAddress {
    private String name;
    private String phone;
    private String region;
    private String street;
    private String unit;

    // Default constructor (required for Firebase)
    public UserAddress() {}

    // Constructor to initialize address
    public UserAddress(String name, String phone, String region, String street, String unit) {
        this.name = name;
        this.phone = phone;
        this.region = region;
        this.street = street;
        this.unit = unit;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
