package com.example.recycleviewtesting;

import java.io.Serializable;

public class UserAddress implements Serializable {
    private String name, phone, region, street, unit;

    public UserAddress(String name, String phone, String region, String street, String unit) {
        this.name = name;
        this.phone = phone;
        this.region = region;
        this.street = street;
        this.unit = unit;
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
}
