package com.example.luxevistaapp;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ServiceReservationActivity extends AppCompatActivity {

    private ListView serviceListView;
    private ServiceAdapter serviceAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_reservation);

        dbHelper = new DatabaseHelper(this);
        serviceListView = findViewById(R.id.serviceListView);

        List<Service> services = dbHelper.getAllServices();

        serviceAdapter = new ServiceAdapter(this, services);
        serviceListView.setAdapter(serviceAdapter);

        serviceListView.setOnItemClickListener((parent, view, position, id) -> {
            Service selectedService = services.get(position);
            Toast.makeText(this, "Reserved: " + selectedService.getName(), Toast.LENGTH_SHORT).show();
        });
    }
}
