package com.example.odisea;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar la vista del fragmento
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Configurar el texto de bienvenida
        TextView welcomeText = view.findViewById(R.id.text_welcome);
        welcomeText.setText("Hola, Nombre");

        // Configurar RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_places);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
