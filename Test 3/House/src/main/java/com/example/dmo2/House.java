package com.example.dmo2;

public class House {//房子类
    private int id;
    private String address;
    private double price;
    private String contact;//联系方式

    public House(int id, String address, double price, String contact) {
        this.id = id;
        this.address = address;
        this.price = price;
        this.contact = contact;
    }

    // Getters
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
}
