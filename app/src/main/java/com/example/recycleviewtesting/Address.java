package com.example.recycleviewtesting;

public class Address {
    public String id;  // UUID string for uniqueness
    public String name;
    public String phone;
    public String region;
    public String street;
    public String unit;

    public Address(String id, String name, String phone, String region, String street, String unit) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.region = region;
        this.street = street;
        this.unit = unit;
    }
}
