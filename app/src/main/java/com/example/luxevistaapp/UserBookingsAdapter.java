package com.example.luxevistaapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luxevistaapp.databinding.ItemUserBookingBinding;
import java.util.List;

public class UserBookingsAdapter extends RecyclerView.Adapter<UserBookingsAdapter.BookingViewHolder> {

    private final List<BookingDetails> bookings;

    public UserBookingsAdapter(List<BookingDetails> bookings) {
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserBookingBinding binding = ItemUserBookingBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new BookingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        holder.bind(bookings.get(position));
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserBookingBinding binding;

        public BookingViewHolder(ItemUserBookingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(BookingDetails booking) {
            binding.roomTypeText.setText(booking.getRoomType());
            String dateRange = booking.getCheckInDate() + " - " + booking.getCheckOutDate();
            binding.bookingDatesText.setText(dateRange);
        }
    }
}