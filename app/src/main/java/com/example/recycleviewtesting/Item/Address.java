package com.example.recycleviewtesting.Item;

public class Address {
    private String id;
    private String name;
    private String phone;
    private String region;
    private String street;
    private String unit;

    // Constructor
    public Address(String id, String name, String phone, String region, String street, String unit) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.region = region;
        this.street = street;
        this.unit = unit;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getRegion() {
        return region;
    }

    public String getStreet() {
        return street;
    }

    public String getUnit() {
        return unit;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
