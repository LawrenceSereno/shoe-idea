package com.example.recycleviewtesting.Address;

public class UserAddress {

    private String name;
    private String phone;
    private String region;
    private String street;
    private String unit;
    private boolean useAddress;

    // Default constructor required for Firebase
    public UserAddress() {
    }

    // Constructor with all fields
    public UserAddress(String name, String phone, String region, String street, String unit, boolean useAddress) {
        this.name = name;
        this.phone = phone;
        this.region = region;
        this.street = street;
        this.unit = unit;
        this.useAddress = useAddress;
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

    public boolean isUseAddress() {
        return useAddress;
    }

    public void setUseAddress(boolean useAddress) {
        this.useAddress = useAddress;
    }
}
