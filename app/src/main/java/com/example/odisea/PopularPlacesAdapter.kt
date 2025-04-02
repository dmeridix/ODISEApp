package com.example.odisea;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PopularPlacesAdapter extends RecyclerView.Adapter<PopularPlacesAdapter.PopularPlacesViewHolder> {

    private List<String> placeNames;

    public PopularPlacesAdapter(List<String> placeNames) {
        this.placeNames = placeNames;
    }

    @NonNull
    @Override
    public PopularPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular_place, parent, false);
        return new PopularPlacesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularPlacesViewHolder holder, int position) {
        int index = position * 2;
        if (index < placeNames.size()) {
            holder.placeName.setText(placeNames.get(index));
        } else {
            holder.placeName.setVisibility(View.GONE);
        }
        if (index + 1 < placeNames.size()) {
            holder.placeName2.setText(placeNames.get(index + 1));
        } else {
            holder.placeName2.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil(placeNames.size() / 2.0);
    }

    static class PopularPlacesViewHolder extends RecyclerView.ViewHolder {
        TextView placeName, placeName2;

        PopularPlacesViewHolder(View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.place_name);
            placeName2 = itemView.findViewById(R.id.place_name2);
        }
    }
}