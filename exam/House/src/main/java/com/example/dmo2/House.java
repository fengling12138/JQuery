package com.example.dmo2;

import java.util.List;

public class House {
    private int id;
    private String address;
    private double price;
    private String contact;
    private List<String> imageUrls;

    public House(int id, String address, double price, String contact, List<String> imageUrls) {
        this.id = id;
        this.address = address;
        this.price = price;
        this.contact = contact;
        this.imageUrls = imageUrls;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public double getPrice() {
        return price;
    }

    public String getContact() {
        return contact;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }
}
