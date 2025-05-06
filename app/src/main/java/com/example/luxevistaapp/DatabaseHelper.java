package com.example.luxevistaapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Name and Version
    private static final String DATABASE_NAME = "LuxeVista.db";
    private static final int DATABASE_VERSION = 1;

    // Users Table
    public static final String TABLE_USERS = "Users";
    public static final String COLUMN_USER_ID = "UserID";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_EMAIL = "Email";
    public static final String COLUMN_PASSWORD = "Password";

    // Create Users Table SQL
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_ROOMS);
        db.execSQL(CREATE_TABLE_SERVICES);
        db.execSQL(CREATE_TABLE_BOOKINGS);
        db.execSQL(CREATE_TABLE_RESERVATIONS);

        // Add Rooms
        db.execSQL("INSERT INTO Rooms (roomType, price, availability, features) VALUES " +
                "('Single Deluxe', 120.0, 1, 'Garden View'), " +
                "('Double Deluxe', 200.0, 1, 'Mountain View, Balcony'), " +
                "('Honeymoon Suite', 350.0, 1, 'Ocean View, Jacuzzi'), " +
                "('Family Suite', 300.0, 1, 'Two Bedrooms, Living Area, Kitchenette'), " +
                "('Presidential Suite', 500.0, 1, 'Private Pool, Personal Butler')");

        // Add Services
        db.execSQL("INSERT INTO Services (Name, Price, Schedule) VALUES " +
                "('Fine Dining', 70.0, '6 PM - 10 PM'), " +
                "('Snorkeling', 40.0, '8 AM - 4 PM'), " +
                "('Boat Ride', 60.0, '9 AM - 6 PM'), " +
                "('Yoga Classes', 50.0, '6 AM - 8 AM'), " +
                "('Cocktail Making', 35.0, '5 PM - 6 PM'), " +
                "('Kids Club', 20.0, '9 AM - 5 PM')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // Drop additional tables here if upgrading
        onCreate(db);
    }

    public long addUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);

        return db.insert(TABLE_USERS, null, values);
    }

    public int validateUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE " +
                COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?";
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, new String[]{email, password});
            Log.d("validateUser", "Executing query: " + query);
            if (cursor != null && cursor.moveToFirst()) {
                int userID = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
                Log.d("validateUser", "User found: " + userID);
                return userID;
            } else {
                Log.d("validateUser", "No user found with the provided credentials.");
            }
        } catch (Exception e) {
            Log.e("DatabaseError", "Error during user validation", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return -1; // User not found
    }


    public List<Room> getAvailableRooms() {
        List<Room> rooms = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Rooms WHERE Availability = 1";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int roomID = cursor.getInt(cursor.getColumnIndex("roomID")); // lowercase "roomID"
                @SuppressLint("Range") String roomType = cursor.getString(cursor.getColumnIndex("roomType")); // lowercase
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price")); // lowercase
                @SuppressLint("Range") boolean availability = cursor.getInt(cursor.getColumnIndex("availability")) == 1; // lowercase
                @SuppressLint("Range") String features = cursor.getString(cursor.getColumnIndex("features")); // lowercase

                Room room = new Room(roomID, roomType, price, availability, features);
                rooms.add(room);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return rooms;
    }

    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Services";
        Cursor cursor = db.rawQuery(query, null);
        Log.d("DatabaseHelper", "Columns: " + Arrays.toString(cursor.getColumnNames()));


        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int serviceID = cursor.getInt(cursor.getColumnIndex("serviceID"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") double price = cursor.getDouble(cursor.getColumnIndex("price"));
                @SuppressLint("Range") String schedule = cursor.getString(cursor.getColumnIndex("schedule"));

                Service service = new Service(serviceID, name, price, schedule);
                services.add(service);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return services;
    }

    public long addBooking(int userID, int roomID, String checkInDate, String checkOutDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserID", userID);
        values.put("RoomID", roomID);
        values.put("CheckInDate", checkInDate);
        values.put("CheckOutDate", checkOutDate);

        return db.insert("Bookings", null, values);
    }

    public List<String> getAllBookings() {
        List<String> bookings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT b.BookingID, u.Name, r.roomType, b.CheckInDate, b.CheckOutDate " +
                "FROM Bookings b " +
                "JOIN Users u ON b.UserID = u.UserID " +
                "JOIN Rooms r ON b.RoomID = r.roomID";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String bookingDetails = "Booking ID: " + cursor.getInt(cursor.getColumnIndex("BookingID")) +
                        ", User: " + cursor.getString(cursor.getColumnIndex("Name")) +
                        ", Room: " + cursor.getString(cursor.getColumnIndex("roomType")) +
                        ", Check-In: " + cursor.getString(cursor.getColumnIndex("CheckInDate")) +
                        ", Check-Out: " + cursor.getString(cursor.getColumnIndex("CheckOutDate"));
                bookings.add(bookingDetails);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return bookings;
    }

    public List<String> getBookingsByUser(int userID) {
        List<String> bookings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // SQL query with WHERE clause
        String query = "SELECT b.BookingID, r.roomType, b.CheckInDate, b.CheckOutDate " +
                "FROM Bookings b " +
                "JOIN Rooms r ON b.RoomID = r.roomID " +
                "WHERE b.UserID = ?";

        Log.d("getBookingsByUser", "Executing query: " + query + " with userID: " + userID);

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userID)});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String bookingDetails = "Booking ID: " + cursor.getInt(cursor.getColumnIndex("BookingID")) +
                        ", Room: " + cursor.getString(cursor.getColumnIndex("roomType")) +
                        ", Check-In: " + cursor.getString(cursor.getColumnIndex("CheckInDate")) +
                        ", Check-Out: " + cursor.getString(cursor.getColumnIndex("CheckOutDate"));

                Log.d("getBookingsByUser", "Found booking: " + bookingDetails); // Log each booking
                bookings.add(bookingDetails);
            } while (cursor.moveToNext());
        } else {
            Log.d("getBookingsByUser", "No bookings found for userID: " + userID);
        }

        cursor.close();
        return bookings;
    }

    public long addReservation(int userID, int serviceID, String date, String timeSlot) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("UserID", userID);
        values.put("ServiceID", serviceID);
        values.put("Date", date);
        values.put("TimeSlot", timeSlot);

        return db.insert("Reservations", null, values);
    }

    public List<String> getReservationsByUser(int userID) {
        List<String> reservations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // SQL query to get reservations for a specific user
        String query = "SELECT r.ReservationID, s.Name, r.Date, r.TimeSlot " +
                "FROM Reservations r " +
                "JOIN Services s ON r.ServiceID = s.ServiceID " +
                "WHERE r.UserID = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userID)});

        // Log the columns to verify if the correct columns exist
        Log.d("DatabaseHelper", "Columns: " + Arrays.toString(cursor.getColumnNames()));

        if (cursor.moveToFirst()) {
            do {
                try {
                    // Check the column index and read the data properly
                    @SuppressLint("Range") int reservationID = cursor.getInt(cursor.getColumnIndex("ReservationID"));
                    @SuppressLint("Range") String serviceName = cursor.getString(cursor.getColumnIndex("Name"));
                    @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("Date"));
                    @SuppressLint("Range") String timeSlot = cursor.getString(cursor.getColumnIndex("TimeSlot"));

                    // Add reservation details to the list
                    String reservationDetails = "Reservation ID: " + reservationID +
                            ", Service: " + serviceName +
                            ", Date: " + date +
                            ", Time Slot: " + timeSlot;
                    reservations.add(reservationDetails);
                } catch (Exception e) {
                    Log.e("DatabaseError", "Error accessing columns", e);
                }
            } while (cursor.moveToNext());
        } else {
            Log.d("DatabaseHelper", "No reservations found for user: " + userID);
        }

        cursor.close();
        return reservations;
    }

    public List<String> getReservedServicesByUser(int userID) {
        List<String> reservedServices = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT s.name, r.date, r.timeSlot FROM Reservations r " +
                "JOIN Services s ON r.serviceID = s.serviceID " +
                "WHERE r.userID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userID)});

        if (cursor.moveToFirst()) {
            do {
                String serviceName = cursor.getString(0);
                String reservationDate = cursor.getString(1);
                String timeSlot = cursor.getString(2);
                reservedServices.add(serviceName + " - Date: " + reservationDate + ", Time: " + timeSlot);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return reservedServices;
    }


    private static final String CREATE_TABLE_BOOKINGS =
            "CREATE TABLE Bookings (" +
                    "BookingID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "UserID INTEGER NOT NULL, " +
                    "RoomID INTEGER NOT NULL, " +
                    "CheckInDate TEXT NOT NULL, " +
                    "CheckOutDate TEXT NOT NULL, " +
                    "FOREIGN KEY(UserID) REFERENCES Users(UserID), " +
                    "FOREIGN KEY(RoomID) REFERENCES Rooms(RoomID));";


    //  create the Rooms table
    private static final String CREATE_TABLE_ROOMS =
            "CREATE TABLE Rooms (" +
                    "roomID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "roomType TEXT NOT NULL, " +
                    "price REAL NOT NULL, " +
                    "availability INTEGER NOT NULL, " +
                    "features TEXT NOT NULL);";


    private static final String CREATE_TABLE_SERVICES =
            "CREATE TABLE Services (" +
                    "serviceID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "price REAL NOT NULL, " +
                    "schedule TEXT NOT NULL);";

    private static final String CREATE_TABLE_RESERVATIONS =
            "CREATE TABLE Reservations (" +
                    "ReservationID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "UserID INTEGER NOT NULL, " +
                    "ServiceID INTEGER NOT NULL, " +
                    "Date TEXT NOT NULL, " +
                    "TimeSlot TEXT NOT NULL, " +
                    "FOREIGN KEY(UserID) REFERENCES Users(UserID), " +
                    "FOREIGN KEY(ServiceID) REFERENCES Services(ServiceID));";


}
