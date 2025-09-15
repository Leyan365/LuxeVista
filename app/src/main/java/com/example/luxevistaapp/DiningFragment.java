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

public class DiningFragment extends Fragment {
    private RecyclerView recyclerView;
    private DiningAdapter diningAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dining, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Load dining data
        List<DiningOption> diningOptions = new ArrayList<>();
        diningOptions.add(new DiningOption("Italian Restaurant", R.drawable.italian_restaurant));
        diningOptions.add(new DiningOption("Buffet", R.drawable.buffet));
        diningOptions.add(new DiningOption("Seafood Grill", R.drawable.seafood_grill));
        diningOptions.add(new DiningOption("Coffee Shop", R.drawable.coffee_shop));

        // Set adapter
        diningAdapter = new DiningAdapter(diningOptions);
        recyclerView.setAdapter(diningAdapter);

        return view;
    }
}
