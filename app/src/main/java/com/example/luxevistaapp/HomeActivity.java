package com.example.luxevistaapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout roomContainer, serviceContainer,bookingContainer;;
    private DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        dbHelper = new DatabaseHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        int userID = sharedPreferences.getInt("userID", -1);

        if (userID == -1) {
            Toast.makeText(this, "No user session found. Redirecting to login.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        Button reserveServiceButton = findViewById(R.id.reserveServiceButton);
        reserveServiceButton.setOnClickListener(v -> showReservationDialog(userID));
        displayReservations(userID);

        Button viewBookingsButton = findViewById(R.id.viewBookingsButton);
        viewBookingsButton.setOnClickListener(v -> displayBookings(userID));

        Button viewReservedServicesButton = findViewById(R.id.viewReservedServicesButton);
        viewReservedServicesButton.setOnClickListener(v -> displayReservedServices(userID));

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Remove all stored session data
            editor.apply();

            Toast.makeText(HomeActivity.this, "Logged out successfully", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        roomContainer = findViewById(R.id.roomContainer);
        serviceContainer = findViewById(R.id.serviceContainer);

        List<Room> rooms = dbHelper.getAvailableRooms();
        List<Service> services = dbHelper.getAllServices();

        for (Room room : rooms) {
            View roomItemView;

            switch (room.getRoomType()) {
                case "Single Deluxe":
                    roomItemView = LayoutInflater.from(this).inflate(R.layout.room_item_single, roomContainer, false);
                    break;
                case "Double Deluxe":
                    roomItemView = LayoutInflater.from(this).inflate(R.layout.room_item_double, roomContainer, false);
                    break;
                case "Honeymoon Suite":
                    roomItemView = LayoutInflater.from(this).inflate(R.layout.room_item_honeymoon, roomContainer, false);
                    break;
                case "Family Suite":
                    roomItemView = LayoutInflater.from(this).inflate(R.layout.room_item_family, roomContainer, false);
                    break;
                case "Presidential Suite":
                    roomItemView = LayoutInflater.from(this).inflate(R.layout.room_item_presidential, roomContainer, false);
                    break;
                default:
                    roomItemView = LayoutInflater.from(this).inflate(R.layout.room_item, roomContainer, false);
                    break;
            }

            ImageView roomImage = roomItemView.findViewById(R.id.roomImage);
            TextView roomType = roomItemView.findViewById(R.id.roomType);
            TextView roomPrice = roomItemView.findViewById(R.id.price);
            TextView roomFeatures = roomItemView.findViewById(R.id.features);
            Button bookNowButton = roomItemView.findViewById(R.id.bookNowButton);

            roomType.setText(room.getRoomType());
            roomPrice.setText(String.format("$%.2f", room.getPrice()));
            roomFeatures.setText(room.getFeatures());

            switch (room.getRoomType()) {
                case "Single Deluxe":
                    roomImage.setImageResource(R.drawable.single_deluxe);
                    break;
                case "Double Deluxe":
                    roomImage.setImageResource(R.drawable.double_deluxe);
                    break;
                case "Honeymoon Suite":
                    roomImage.setImageResource(R.drawable.honeymoon_suite);
                    break;
                case "Family Suite":
                    roomImage.setImageResource(R.drawable.family_suite);
                    break;
                case "Presidential Suite":
                    roomImage.setImageResource(R.drawable.presidential_suite);
                    break;
            }

            bookNowButton.setOnClickListener(v -> {
                showBookingDialog(room, userID);
            });

            roomContainer.addView(roomItemView);
        }

        for (Service service : services) {
            View serviceItemView;

            switch (service.getName()) {
                case "Fine Dining":
                    serviceItemView = LayoutInflater.from(this).inflate(R.layout.service_item_fine_dining, serviceContainer, false);
                    break;
                case "Snorkeling":
                    serviceItemView = LayoutInflater.from(this).inflate(R.layout.service_item_snorkeling, serviceContainer, false);
                    break;
                case "Boat Ride":
                    serviceItemView = LayoutInflater.from(this).inflate(R.layout.service_item_boat_ride, serviceContainer, false);
                    break;
                case "Yoga Classes":
                    serviceItemView = LayoutInflater.from(this).inflate(R.layout.service_item_yoga_classes, serviceContainer, false);
                    break;
                case "Cocktail Making":
                    serviceItemView = LayoutInflater.from(this).inflate(R.layout.service_item_cocktail_making, serviceContainer, false);
                    break;
                case "Kids Club":
                    serviceItemView = LayoutInflater.from(this).inflate(R.layout.service_item_kids_club, serviceContainer, false);
                    break;
                default:
                    serviceItemView = LayoutInflater.from(this).inflate(R.layout.service_item, serviceContainer, false);
                    break;
            }

            ImageView serviceImage = serviceItemView.findViewById(R.id.serviceImage);
            TextView serviceName = serviceItemView.findViewById(R.id.serviceName);
            TextView servicePrice = serviceItemView.findViewById(R.id.servicePrice);
            TextView serviceSchedule = serviceItemView.findViewById(R.id.serviceSchedule);

            serviceName.setText(service.getName());
            servicePrice.setText(String.format("$%.2f", service.getPrice()));
            serviceSchedule.setText(service.getSchedule());

            switch (service.getName()) {
                case "Fine Dining":
                    serviceImage.setImageResource(R.drawable.fine_dining);
                    break;
                case "Snorkeling":
                    serviceImage.setImageResource(R.drawable.snorkeling);
                    break;
                case "Boat Ride":
                    serviceImage.setImageResource(R.drawable.boat_ride);
                    break;
                case "Yoga Classes":
                    serviceImage.setImageResource(R.drawable.yoga_classes);
                    break;
                case "Cocktail Making":
                    serviceImage.setImageResource(R.drawable.cocktail_making);
                    break;
                case "Kids Club":
                    serviceImage.setImageResource(R.drawable.kids_club);
                    break;
            }
            serviceContainer.addView(serviceItemView);
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(HomeActivity.this, "Logged out successfully", Toast.LENGTH_LONG).show();


        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void showBookingDialog(Room room, int userID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_booking, null);
        builder.setView(dialogView);

        EditText checkInDateField = dialogView.findViewById(R.id.checkInDate);
        EditText checkOutDateField = dialogView.findViewById(R.id.checkOutDate);

        checkInDateField.setOnClickListener(v -> {
            showDatePickerDialog((selectedDate) -> {
                checkInDateField.setText(selectedDate);
            });
        });

        checkOutDateField.setOnClickListener(v -> {
            showDatePickerDialog((selectedDate) -> {
                checkOutDateField.setText(selectedDate);
            });
        });

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String checkInDate = checkInDateField.getText().toString();
            String checkOutDate = checkOutDateField.getText().toString();

            if (checkInDate.isEmpty() || checkOutDate.isEmpty()) {
                Toast.makeText(this, "Please select valid dates.", Toast.LENGTH_SHORT).show();
                return;
            }

            long result = dbHelper.addBooking(userID, room.getRoomID(), checkInDate, checkOutDate);

            if (result > 0) {
                Toast.makeText(this, "Room booked successfully!", Toast.LENGTH_SHORT).show();
                displayBookings(userID);
            } else {
                Toast.makeText(this, "Booking failed. Try again.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDatePickerDialog(DatePickerCallback callback) {

        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            String selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
            callback.onDateSelected(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }
    interface DatePickerCallback
    {
        void onDateSelected(String selectedDate);
    }

    private void displayBookings(int userID) {
        List<String> bookings = dbHelper.getBookingsByUser(userID);

        LinearLayout bookingContainer = findViewById(R.id.bookingContainer);
        bookingContainer.removeAllViews();

        if (!bookings.isEmpty()) {
            for (String booking : bookings) {

                TextView bookingTextView = new TextView(this);
                bookingTextView.setText(booking);
                bookingTextView.setPadding(8, 8, 8, 8);
                bookingTextView.setTextSize(16);

                bookingContainer.addView(bookingTextView);
            }
            Log.d("displayBookings", "Displayed bookings for userID: " + userID);
        } else
        {
            TextView noBookingsTextView = new TextView(this);
            noBookingsTextView.setText("No bookings found.");
            noBookingsTextView.setPadding(8, 8, 8, 8);
            noBookingsTextView.setTextSize(16);

            bookingContainer.addView(noBookingsTextView);
        }
    }
    private void logBookings() {
        List<String> bookings = dbHelper.getAllBookings();
        for (String booking : bookings) {
            Log.d("Bookings", booking);
        }
    }

    private void showReservationDialog(int userID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_reservation, null);
        builder.setView(dialogView);

        Spinner serviceSpinner = dialogView.findViewById(R.id.serviceSpinner);
        EditText dateField = dialogView.findViewById(R.id.dateField);
        EditText timeSlotField = dialogView.findViewById(R.id.timeSlotField);

        List<Service> services = dbHelper.getAllServices();
        ArrayAdapter<Service> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, services);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceSpinner.setAdapter(adapter);

        dateField.setOnClickListener(v -> {
            showDatePickerDialog((selectedDate) -> {
                dateField.setText(selectedDate);
            });
        });

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            Service selectedService = (Service) serviceSpinner.getSelectedItem();
            String date = dateField.getText().toString();
            String timeSlot = timeSlotField.getText().toString();

            if (date.isEmpty() || timeSlot.isEmpty()) {
                Toast.makeText(this, "Please enter valid details.", Toast.LENGTH_LONG).show();
                return;
            }

            long result = dbHelper.addReservation(userID, selectedService.getServiceID(), date, timeSlot);

            if (result > 0) {
                Toast.makeText(this, "Service reserved successfully!", Toast.LENGTH_LONG).show();
                displayReservations(userID);
            } else {
                Toast.makeText(this, "Reservation failed. Try again.", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void displayReservations(int userID) {
        LinearLayout reservationsContainer = findViewById(R.id.reservationsContainer);
        reservationsContainer.removeAllViews();

        List<String> reservations = dbHelper.getReservationsByUser(userID);
        for (String reservation : reservations) {
            TextView reservationTextView = new TextView(this);
            reservationTextView.setText(reservation);
            reservationTextView.setPadding(8, 8, 8, 8);
            reservationsContainer.addView(reservationTextView);
        }
    }

    private void displayReservedServices(int userID) {
        LinearLayout reservationsContainer = findViewById(R.id.reservationsContainer);

        reservationsContainer.removeAllViews();

        List<String> reservedServices = dbHelper.getReservedServicesByUser(userID);

        if (!reservedServices.isEmpty()) {
            for (String service : reservedServices) {

                TextView serviceTextView = new TextView(this);
                serviceTextView.setText(service);
                serviceTextView.setTextSize(16);
                serviceTextView.setPadding(8, 8, 8, 8);

                reservationsContainer.addView(serviceTextView);
            }
        } else {
            TextView noServicesTextView = new TextView(this);
            noServicesTextView.setText("No services reserved.");
            noServicesTextView.setTextSize(16);
            noServicesTextView.setPadding(8, 8, 8, 8);

            reservationsContainer.addView(noServicesTextView);
        }
    }
}
