package com.example.luxevistaapp;

// This class will hold the structured data for a single booking.
public class BookingDetails {
    private final String roomType;
    private final String checkInDate;
    private final String checkOutDate;

    public BookingDetails(String roomType, String checkInDate, String checkOutDate) {
        this.roomType = roomType;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }
}