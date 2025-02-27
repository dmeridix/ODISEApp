package com.example.odisea;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflar el layout del fragmento que contiene el BottomNavigationView
        View view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false);

        // Referencia al BottomNavigationView
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().setGroupCheckable(0, true, false);

        // Establecer el ítem seleccionado según el fragmento actual
        when (parentFragmentManager.findFragmentById(R.id.fragment_container)) {
            is HomeFragment -> bottomNavigationView.selectedItemId = R.id.nav_home
            is SearchFragment -> bottomNavigationView.selectedItemId = R.id.nav_search
            is SavedFragment -> bottomNavigationView.selectedItemId = R.id.nav_saved
            is ReservesFragment -> bottomNavigationView.selectedItemId = R.id.nav_reserves
            else -> bottomNavigationView.selectedItemId = View.NO_ID
        }

        // Configurar el listener para manejar la navegación por fragments
        bottomNavigationView.setOnItemSelectedListener { item ->
                val selectedFragment = when (item.itemId) {
            R.id.nav_search -> SearchFragment()
            R.id.nav_home -> HomeFragment()
            R.id.nav_saved -> SavedFragment()
            R.id.nav_reserves -> ReservesFragment()
                else -> return@setOnItemSelectedListener false
        }
            // Reemplazar el fragmento actual con el seleccionado
            parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit()
            true
        }

        return view;
    }
}