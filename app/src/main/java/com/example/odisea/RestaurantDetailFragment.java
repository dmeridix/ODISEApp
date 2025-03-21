package com.example.odisea;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.lifecycleScope;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.odisea.api.RestaurantApiService;
import com.example.odisea.models.Restaurant;
import com.example.odisea.adapters.FeaturesAdapter;
import kotlinx.coroutines.launch;

public class RestaurantDetailFragment extends Fragment {

    private RestaurantApiService restaurantApiService;
    private int restaurantId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        restaurantApiService = RestaurantApiService.getInstance();
        if (getArguments() != null) {
            restaurantId = getArguments().getInt("ARG_RESTAURANT_ID");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_restaurant_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Referencias a los elementos del layout
        ImageView restaurantMainPicture = view.findViewById(R.id.restaurant_main_picture);
        TextView restaurantName = view.findViewById(R.id.restaurant_name);
        TextView restaurantLocation = view.findViewById(R.id.restaurant_location);
        TextView restaurantRating = view.findViewById(R.id.restaurant_rating);
        TextView restaurantCuisineType = view.findViewById(R.id.restaurant_cuisine_type);
        TextView restaurantOpenFor = view.findViewById(R.id.restaurant_open_for);
        TextView restaurantAmbiance = view.findViewById(R.id.restaurant_ambiance);
        TextView restaurantDescription = view.findViewById(R.id.restaurant_description);
        Button btnReserve = view.findViewById(R.id.btn_reserve);
        RecyclerView rvFeatures = view.findViewById(R.id.rv_features);

        // Configuración del RecyclerView para mostrar las características
        rvFeatures.setLayoutManager(new LinearLayoutManager(requireContext()));
        FeaturesAdapter featuresAdapter = new FeaturesAdapter(new String[]{});
        rvFeatures.setAdapter(featuresAdapter);

        // Cargar los datos del restaurante desde la API
        lifecycleScope.launch(() -> {
            try {
                Restaurant restaurant = restaurantApiService.getRestaurantById(restaurantId);
                if (restaurant != null) {
                    // Mostrar la imagen del restaurante
                    Glide.with(this).load(restaurant.getImageUrl()).into(restaurantMainPicture);
                    // Mostrar el nombre del restaurante
                    restaurantName.setText(restaurant.getName());
                    // Mostrar la ubicación del restaurante
                    restaurantLocation.setText(restaurant.getLocation());
                    // Mostrar la calificación del restaurante
                    restaurantRating.setText(String.valueOf(restaurant.getRating()));
                    // Mostrar el tipo de cocina
                    restaurantCuisineType.setText(restaurant.getCuisineType());
                    // Mostrar el horario de apertura
                    restaurantOpenFor.setText(restaurant.getOpenFor());
                    // Mostrar el ambiente
                    restaurantAmbiance.setText(restaurant.getAmbiance());
                    // Mostrar la descripción del restaurante
                    restaurantDescription.setText(restaurant.getDescription());
                    // Mostrar las características del restaurante
                    featuresAdapter.updateData(restaurant.getFeatures());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Manejo del botón de reserva
        btnReserve.setOnClickListener(v -> {
            // Lógica para reservar una mesa
            // Por ejemplo, navegar a la pantalla de reserva
        });
    }
}