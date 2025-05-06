package com.example.luxevistaapp;

public class Service {
    private int serviceID;
    private String name;
    private double price;
    private String schedule;

    // Constructor
    public Service(int serviceID, String name, double price, String schedule) {
        this.serviceID = serviceID;
        this.name = name;
        this.price = price;
        this.schedule = schedule;
    }

    // Getters and Setters
    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    @Override
    public String toString() {
        return name;
    }
}
