package com.example.odisea.structure;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.odisea.HomeFragment;
import com.example.odisea.PreferencesFragment; // ⬅️ Importa el fragment de preferències
import com.example.odisea.R;

public class HeaderFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HeaderFragment() {
        // Required empty public constructor
    }

    public static HeaderFragment newInstance(String param1, String param2) {
        HeaderFragment fragment = new HeaderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);

        ImageView logoImageView = view.findViewById(R.id.logoImageView);
        ImageView profileIconImageView = view.findViewById(R.id.profileIconImageView); // 🔥 Icono de perfil

        // Navegar a HomeFragment quan es fa clic al logo
        logoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHomeFragment();
            }
        });

        // Navegar a PreferencesFragment quan es fa clic a l'icona de perfil
        profileIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToPreferencesFragment(); // 🔁 Nova funció per anar a Preferències
            }
        });

        return view;
    }

    private void navigateToHomeFragment() {
        HomeFragment homeFragment = new HomeFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .addToBackStack(null)
                .commit();
    }

    // 🔁 Mètode per navegar a PreferencesFragment
    private void navigateToPreferencesFragment() {
        PreferencesFragment preferencesFragment = new PreferencesFragment();
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, preferencesFragment)
                .addToBackStack(null)
                .commit();
    }
}