package com.example.luxevistaapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ServiceAdapter extends BaseAdapter {
    private Context context;
    private List<Service> services;

    public ServiceAdapter(Context context, List<Service> services) {
        this.context = context;
        this.services = services;
    }

    @Override
    public int getCount() {
        return services.size();
    }

    @Override
    public Object getItem(int position) {
        return services.get(position);
    }

    @Override
    public long getItemId(int position) {
        return services.get(position).getServiceID();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Service service = services.get(position);

        convertView = null;

        if (convertView == null) {
            switch (service.getName()) {
                case "Fine Dining":
                    convertView = LayoutInflater.from(context).inflate(R.layout.service_item_fine_dining, parent, false);
                    break;
                case "Snorkeling":
                    convertView = LayoutInflater.from(context).inflate(R.layout.service_item_snorkeling, parent, false);
                    break;
                case "Boat Ride":
                    convertView = LayoutInflater.from(context).inflate(R.layout.service_item_boat_ride, parent, false);
                    break;
                case "Yoga Classes":
                    convertView = LayoutInflater.from(context).inflate(R.layout.service_item_yoga_classes, parent, false);
                    break;
                case "Cocktail Making":
                    convertView = LayoutInflater.from(context).inflate(R.layout.service_item_cocktail_making, parent, false);
                    break;
                case "Kids Club":
                    convertView = LayoutInflater.from(context).inflate(R.layout.service_item_kids_club, parent, false);
                    break;

            }
        }

        TextView nameTextView = convertView.findViewById(R.id.serviceName);
        TextView priceTextView = convertView.findViewById(R.id.servicePrice);
        TextView scheduleTextView = convertView.findViewById(R.id.serviceSchedule);

        nameTextView.setText(service.getName());
        priceTextView.setText(String.format("$%.2f", service.getPrice()));
        scheduleTextView.setText(service.getSchedule());

        return convertView;
    }

}
