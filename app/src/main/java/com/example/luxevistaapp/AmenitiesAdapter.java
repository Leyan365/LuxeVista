package com.example.luxevistaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AmenitiesAdapter extends RecyclerView.Adapter<AmenitiesAdapter.AmenitiesViewHolder> {
    private List<Amenity> amenities;

    public AmenitiesAdapter(List<Amenity> amenities) {
        this.amenities = amenities;
    }

    @NonNull
    @Override
    public AmenitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_amenity, parent, false);
        return new AmenitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmenitiesViewHolder holder, int position) {
        Amenity amenity = amenities.get(position);
        holder.amenityName.setText(amenity.getName());
        holder.amenityImage.setImageResource(amenity.getImageResource());
    }

    @Override
    public int getItemCount() {
        return amenities.size();
    }

    static class AmenitiesViewHolder extends RecyclerView.ViewHolder {
        TextView amenityName;
        ImageView amenityImage;

        public AmenitiesViewHolder(@NonNull View itemView) {
            super(itemView);
            amenityName = itemView.findViewById(R.id.amenityName);
            amenityImage = itemView.findViewById(R.id.amenityImage);
        }
    }
}
