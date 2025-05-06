package com.example.luxevistaapp;

public class Room {
    private int roomID;
    private String roomType;
    private double price;
    private boolean availability;
    private String features;

    // Constructor, getters, and setters
    public Room(int roomID, String roomType, double price, boolean availability, String features) {
        this.roomID = roomID;
        this.roomType = roomType;
        this.price = price;
        this.availability = availability;
        this.features = features;
    }

    // Getters and Setters
    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }
}
