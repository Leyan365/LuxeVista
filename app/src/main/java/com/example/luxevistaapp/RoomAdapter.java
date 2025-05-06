package com.example.luxevistaapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RoomAdapter extends BaseAdapter {
    private Context context;
    private List<Room> rooms;

    private BookingListener bookingListener;

    public RoomAdapter(Context context, List<Room> rooms) {
        this.context = context;
        this.rooms = rooms;
    }

    @Override
    public int getCount() {
        return rooms.size();
    }

    @Override
    public Object getItem(int position) {
        return rooms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rooms.get(position).getRoomID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Room room = rooms.get(position);

        convertView = null;

        if (convertView == null) {
            switch (room.getRoomType()) {
                case "Single Deluxe":
                    convertView = LayoutInflater.from(context).inflate(R.layout.room_item_single, parent, false);
                    break;
                case "Double Deluxe":
                    convertView = LayoutInflater.from(context).inflate(R.layout.room_item_double, parent, false);
                    break;
                case "Honeymoon Suite":
                    convertView = LayoutInflater.from(context).inflate(R.layout.room_item_honeymoon, parent, false);
                    break;
                case "Family Suite":
                    convertView = LayoutInflater.from(context).inflate(R.layout.room_item_family, parent, false);
                    break;
                case "Presidential Suite":
                    convertView = LayoutInflater.from(context).inflate(R.layout.room_item_presidential, parent, false);
                    break;
            }
        }

        TextView roomTypeTextView = convertView.findViewById(R.id.roomType);
        TextView priceTextView = convertView.findViewById(R.id.price);
        TextView featuresTextView = convertView.findViewById(R.id.features);
        Button bookNowButton = convertView.findViewById(R.id.bookNowButton);

        roomTypeTextView.setText(room.getRoomType());
        priceTextView.setText(String.format("$%.2f", room.getPrice()));
        featuresTextView.setText(room.getFeatures());

        bookNowButton.setOnClickListener(v -> {
            if (bookingListener != null) {
                bookingListener.onBookRoom(room);
            }
        });

        return convertView;
    }


    // Set the listener in the adapter
    public void setBookingListener(BookingListener listener) {
        this.bookingListener = listener;
    }

    // Interface definition for booking
    public interface BookingListener {
        void onBookRoom(Room room);
    }
}
