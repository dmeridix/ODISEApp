package com.example.odisea;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false);

        // Referencia al BottomNavigationView
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottom_navigation);

        // Obtener el fragmento actual
        FragmentManager fragmentManager = getParentFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentById(R.id.fragment_container);

        // Establecer el ítem seleccionado según el fragmento actual
        if (currentFragment instanceof HomeFragment) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        } else if (currentFragment instanceof SearchFragment) {
            bottomNavigationView.setSelectedItemId(R.id.nav_search);
        } else if (currentFragment instanceof SavedFragment) {
            bottomNavigationView.setSelectedItemId(R.id.nav_saved);
        } else if (currentFragment instanceof ReservesFragment) {
            bottomNavigationView.setSelectedItemId(R.id.nav_reserves);
        } else {
            bottomNavigationView.setSelectedItemId(View.NO_ID);
        }

        // Configurar el listener para manejar la navegación por fragments
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_search:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_saved:
                    selectedFragment = new SavedFragment();
                    break;
                case R.id.nav_reserves:
                    selectedFragment = new ReservesFragment();
                    break;
                default:
                    return false;
            }

            // Reemplazar el fragmento actual con el seleccionado
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, selectedFragment);
            transaction.commit();

            return true;
        });

        return view;
    }
}
