package com.example.luxevistaapp;

public class Food {
    private String name;
    private int imageResource;

    public Food(String name, int imageResource) {
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
