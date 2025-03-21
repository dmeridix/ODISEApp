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
import com.example.odisea.api.HotelApiService;
import com.example.odisea.models.Hotel;
import com.example.odisea.adapters.RulesAdapter;
import kotlinx.coroutines.launch;

public class DetailHotelFragment extends Fragment {

    private HotelApiService hotelApiService;
    private int hotelId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hotelApiService = HotelApiService.getInstance();
        if (getArguments() != null) {
            hotelId = getArguments().getInt("ARG_HOTEL_ID");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_hotel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Referencias a los elementos del layout
        ImageView hotelMainPicture = view.findViewById(R.id.hotel_main_picture);
        TextView hotelName = view.findViewById(R.id.hotel_name);
        TextView hotelLocation = view.findViewById(R.id.hotel_location);
        TextView hotelRating = view.findViewById(R.id.hotel_rating);
        TextView hotelDescription = view.findViewById(R.id.hotel_description);
        Button btnReserve = view.findViewById(R.id.btn_reserve);
        RecyclerView rvRules = view.findViewById(R.id.rv_rules);

        // Configuración del RecyclerView para mostrar las normas
        rvRules.setLayoutManager(new LinearLayoutManager(requireContext()));
        RulesAdapter rulesAdapter = new RulesAdapter(new String[]{});
        rvRules.setAdapter(rulesAdapter);

        // Cargar los datos del hotel desde la API
        lifecycleScope.launch(() -> {
            try {
                Hotel hotel = hotelApiService.getHotelById(hotelId);
                if (hotel != null) {
                    // Mostrar la imagen del hotel
                    Glide.with(this).load(hotel.getImageUrl()).into(hotelMainPicture);
                    // Mostrar el nombre del hotel
                    hotelName.setText(hotel.getName());
                    // Mostrar la ubicación del hotel
                    hotelLocation.setText(hotel.getLocation());
                    // Mostrar la calificación del hotel
                    hotelRating.setText(String.valueOf(hotel.getRating()));
                    // Mostrar la descripción del hotel
                    hotelDescription.setText(hotel.getDescription());
                    // Mostrar las normas del hotel
                    rulesAdapter.updateData(hotel.getRules());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Manejo del botón de reserva
        btnReserve.setOnClickListener(v -> {
            // Lógica para reservar el hotel
            // Por ejemplo, navegar a la pantalla de reserva
        });
    }
}