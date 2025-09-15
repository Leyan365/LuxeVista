package com.example.luxevistaapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class AmenitiesFragment extends Fragment {
    private RecyclerView recyclerView;
    private AmenitiesAdapter amenitiesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amenities, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Load amenities data
        List<Amenity> amenities = new ArrayList<>();
        amenities.add(new Amenity("Swimming Pool", R.drawable.swimming_pool));
        amenities.add(new Amenity("Spa & Wellness", R.drawable.spa));
        amenities.add(new Amenity("Gym", R.drawable.gym));
        amenities.add(new Amenity("Free Wi-Fi", R.drawable.wifi));
        amenities.add(new Amenity("Conference Room", R.drawable.conference_room));

        // Set adapter
        amenitiesAdapter = new AmenitiesAdapter(amenities);
        recyclerView.setAdapter(amenitiesAdapter);

        return view;
    }
}
