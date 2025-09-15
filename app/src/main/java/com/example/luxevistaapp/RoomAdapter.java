package com.example.luxevistaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luxevistaapp.databinding.RoomItemBinding; // Import ViewBinding

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    private List<Room> rooms;
    private BookingListener bookingListener;

    // The listener interface
    public interface BookingListener {
        void onBookRoom(Room room);
    }

    public RoomAdapter(List<Room> rooms, BookingListener listener) {
        this.rooms = rooms;
        this.bookingListener = listener;
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the single, reusable layout file using ViewBinding
        RoomItemBinding binding = RoomItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new RoomViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        // Get the data for the current position
        Room currentRoom = rooms.get(position);
        // Bind the data to the views
        holder.bind(currentRoom, bookingListener);
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    // The ViewHolder holds the views for a single item
    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        private final RoomItemBinding binding; // Use ViewBinding

        public RoomViewHolder(RoomItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // A method to bind the data to the views
        public void bind(final Room room, final BookingListener listener) {
            binding.roomType.setText(room.getRoomType());
            binding.price.setText(String.format("$%.2f", room.getPrice()));
            binding.features.setText(room.getFeatures());

            // Set the correct image based on the room type
            switch (room.getRoomType()) {
                case "Single Deluxe":
                    binding.roomImage.setImageResource(R.drawable.single_deluxe);
                    break;
                case "Double Deluxe":
                    binding.roomImage.setImageResource(R.drawable.double_deluxe);
                    break;
                case "Honeymoon Suite":
                    binding.roomImage.setImageResource(R.drawable.honeymoon_suite);
                    break;
                case "Family Suite":
                    binding.roomImage.setImageResource(R.drawable.family_suite);
                    break;
                case "Presidential Suite":
                    binding.roomImage.setImageResource(R.drawable.presidential_suite);
                    break;
                default:
                    binding.roomImage.setImageResource(R.drawable.resort); // A default image
                    break;
            }

            // Set the click listener for the button
            binding.bookNowButton.setOnClickListener(v -> listener.onBookRoom(room));
        }
    }
}