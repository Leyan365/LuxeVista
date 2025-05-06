package com.example.luxevistaapp;

public class DiningOption {
    private String name;
    private int imageResource;

    public DiningOption(String name, int imageResource) {
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
