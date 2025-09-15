package com.example.luxevistaapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luxevistaapp.databinding.ServiceItemBinding;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private final List<Service> services;

    public ServiceAdapter(List<Service> services) {
        this.services = services;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ServiceItemBinding binding = ServiceItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ServiceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service currentService = services.get(position);
        holder.bind(currentService);
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    static class ServiceViewHolder extends RecyclerView.ViewHolder {
        private final ServiceItemBinding binding;

        public ServiceViewHolder(ServiceItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Service service) {
            binding.serviceName.setText(service.getName());

            // Set the correct image based on the service name
            switch (service.getName()) {
                case "Fine Dining":
                    binding.serviceImage.setImageResource(R.drawable.fine_dining);
                    break;
                case "Snorkeling":
                    binding.serviceImage.setImageResource(R.drawable.snorkeling);
                    break;
                case "Boat Ride":
                    binding.serviceImage.setImageResource(R.drawable.boat_ride);
                    break;
                case "Yoga Classes":
                    binding.serviceImage.setImageResource(R.drawable.yoga_classes);
                    break;
                case "Cocktail Making":
                    binding.serviceImage.setImageResource(R.drawable.cocktail_making);
                    break;
                case "Kids Club":
                    binding.serviceImage.setImageResource(R.drawable.kids_club);
                    break;
                default:
                    binding.serviceImage.setImageResource(R.drawable.service); // Default image
                    break;
            }
        }
    }
}