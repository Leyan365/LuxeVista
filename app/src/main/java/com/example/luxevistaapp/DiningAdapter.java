package com.example.luxevistaapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DiningAdapter extends RecyclerView.Adapter<DiningAdapter.DiningViewHolder> {
    private List<DiningOption> diningOptions;

    public DiningAdapter(List<DiningOption> diningOptions) {
        this.diningOptions = diningOptions;
    }

    @NonNull
    @Override
    public DiningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dining, parent, false);
        return new DiningViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiningViewHolder holder, int position) {
        DiningOption diningOption = diningOptions.get(position);
        holder.diningName.setText(diningOption.getName());
        holder.diningImage.setImageResource(diningOption.getImageResource());
    }

    @Override
    public int getItemCount() {
        return diningOptions.size();
    }

    static class DiningViewHolder extends RecyclerView.ViewHolder {
        TextView diningName;
        ImageView diningImage;

        public DiningViewHolder(@NonNull View itemView) {
            super(itemView);
            diningName = itemView.findViewById(R.id.diningName);
            diningImage = itemView.findViewById(R.id.diningImage);
        }
    }
}
