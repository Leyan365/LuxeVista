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

public class FoodsFragment extends Fragment {
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_foods, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Load food data
        List<Food> foodList = new ArrayList<>();
        foodList.add(new Food("Pizza", R.drawable.pizza));
        foodList.add(new Food("Pasta", R.drawable.pasta));
        foodList.add(new Food("Burger", R.drawable.burger));
        foodList.add(new Food("Sushi", R.drawable.sushi));

        // Set adapter
        foodAdapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(foodAdapter);

        return view;
    }
}
