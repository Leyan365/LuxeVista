package com.example.luxevistaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import com.example.luxevistaapp.databinding.ActivityHomeBinding;
import com.example.luxevistaapp.databinding.DialogBookingBinding;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements RoomAdapter.BookingListener {

    private ActivityHomeBinding binding;
    private DatabaseHelper dbHelper;
    private int currentUserID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentUserID = sharedPreferences.getInt("userID", -1);

        if (currentUserID == -1) {
            handleUserNotLoggedIn();
            return;
        }

        setupUI();
        loadData();
    }

    private void setupUI() {
        String userName = dbHelper.getUserName(currentUserID);
        binding.welcomeText.setText("Welcome, " + userName + "!");
        binding.logoutButton.setOnClickListener(v -> logout());
    }

    // ✅ FIXED: This is the single, correct loadData() method
    private void loadData() {
        // Load and display rooms
        List<Room> rooms = dbHelper.getAvailableRooms();
        RoomAdapter roomAdapter = new RoomAdapter(rooms, this);
        binding.roomsRecyclerView.setAdapter(roomAdapter);

        // Load and display services
        List<Service> services = dbHelper.getAllServices();
        ServiceAdapter serviceAdapter = new ServiceAdapter(services);
        binding.servicesRecyclerView.setAdapter(serviceAdapter);

        // Load and display the user's current bookings
        displayBookings(currentUserID);
    }

    // ✅ FIXED: This is the single, correct displayBookings() method
    private void displayBookings(int userID) {
        List<BookingDetails> bookings = dbHelper.getBookingsByUser(userID);
        if (bookings.isEmpty()) {
            binding.myBookingsTitle.setVisibility(View.GONE);
            binding.userBookingsRecyclerView.setVisibility(View.GONE);
        } else {
            binding.myBookingsTitle.setVisibility(View.VISIBLE);
            binding.userBookingsRecyclerView.setVisibility(View.VISIBLE);
            UserBookingsAdapter adapter = new UserBookingsAdapter(bookings);
            binding.userBookingsRecyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void onBookRoom(Room room) {
        showBookingDialog(room);
    }

    private void showBookingDialog(Room room) {
        DialogBookingBinding dialogBinding = DialogBookingBinding.inflate(getLayoutInflater());
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setView(dialogBinding.getRoot());
        dialogBinding.dialogTitle.setText("Book: " + room.getRoomType());

        final AlertDialog dialog = builder.create();
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", (d, which) -> {});
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (d, which) -> dialog.dismiss());

        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setEnabled(false);

            dialogBinding.selectDatesButton.setOnClickListener(v -> {
                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                constraintsBuilder.setStart(MaterialDatePicker.todayInUtcMilliseconds());

                MaterialDatePicker<Pair<Long, Long>> datePicker = MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Select Booking Dates")
                        .setCalendarConstraints(constraintsBuilder.build())
                        .build();

                datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

                datePicker.addOnPositiveButtonClickListener(selection -> {
                    Long startDate = selection.first;
                    Long endDate = selection.second;
                    long nights = (endDate - startDate) / (1000 * 60 * 60 * 24);

                    if (nights <= 0) {
                        Toast.makeText(this, "You must book at least 1 night.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    double totalPrice = room.getPrice() * nights;
                    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                    String checkInDateStr = sdf.format(new Date(startDate));
                    String checkOutDateStr = sdf.format(new Date(endDate));

                    dialogBinding.dateSummaryText.setText("Selected: " + checkInDateStr + " - " + checkOutDateStr);
                    dialogBinding.priceSummaryText.setText(
                            String.format(Locale.getDefault(), "$%.2f x %d nights = $%.2f", room.getPrice(), nights, totalPrice)
                    );
                    dialogBinding.dateSummaryText.setVisibility(View.VISIBLE);
                    dialogBinding.priceSummaryText.setVisibility(View.VISIBLE);
                    dialogBinding.divider.setVisibility(View.VISIBLE);

                    positiveButton.setEnabled(true);
                    positiveButton.setOnClickListener(view -> {
                        // Use the formatted strings that match what's displayed and stored previously
                        SimpleDateFormat dbSdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                        String dbCheckIn = dbSdf.format(new Date(startDate));
                        String dbCheckOut = dbSdf.format(new Date(endDate));

                        long result = dbHelper.addBooking(currentUserID, room.getRoomID(), dbCheckIn, dbCheckOut);

                        if (result > 0) {
                            Toast.makeText(this, "Room booked successfully!", Toast.LENGTH_SHORT).show();
                            displayBookings(currentUserID); // ✅ FIXED: Refresh the bookings list
                        } else {
                            Toast.makeText(this, "Booking failed. Try again.", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    });
                });
            });
        });
        dialog.show();
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void handleUserNotLoggedIn() {
        Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_LONG).show();
        logout();
    }

    // ✅ FIXED: Simplified onBackPressed
    @Override
    public void onBackPressed() {
        logout();
    }
}