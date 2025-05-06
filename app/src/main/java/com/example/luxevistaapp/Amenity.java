package com.example.luxevistaapp;

public class Amenity {
    private String name;
    private int imageResource;

    public Amenity(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }
}
