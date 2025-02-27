package com.example.odisea;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Cargar ReservesHotel por defecto si es la primera vez que se abre la app
        if (savedInstanceState == null) {
            loadFragment(new ReservesHotel());
        }

        // Referencia al BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.menu.setGroupCheckable(0, true, false);

        // Configurar el listener para manejar la navegación por fragments
        bottomNavigationView.setOnItemSelectedListener(item -> {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.nav_search:
                    transaction.replace(R.id.fragment_container, new SearchFragment());
                    break;
                case R.id.nav_home:
                    transaction.replace(R.id.fragment_container, new HomeFragment());
                    break;
                case R.id.nav_saved:
                    transaction.replace(R.id.fragment_container, new SavedFragment());
                    break;
                case R.id.nav_reserves:
                    transaction.replace(R.id.fragment_container, new ReservesHotel());
                    break;
            }
            transaction.commit();
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}