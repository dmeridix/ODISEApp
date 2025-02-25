package com.example.odisea;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.Calendar;

import androidx.fragment.app.Fragment;

public class ReservesHotel extends Fragment {

    private EditText etStartDate, etEndDate, etGuests;
    private Button btnReserve;
    private Calendar calendar;

    public ReservesHotel() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reserves_hotel, container, false);

        etStartDate = view.findViewById(R.id.etStartDate);
        etEndDate = view.findViewById(R.id.etEndDate);
        etGuests = view.findViewById(R.id.etGuests);
        btnReserve = view.findViewById(R.id.btnReserve);
        calendar = Calendar.getInstance();

        etStartDate.setOnClickListener(v -> showDatePicker(etStartDate));
        etEndDate.setOnClickListener(v -> showDatePicker(etEndDate));

        btnReserve.setOnClickListener(v -> {
            String startDate = etStartDate.getText().toString().trim();
            String endDate = etEndDate.getText().toString().trim();
            String guests = etGuests.getText().toString().trim();

            if (startDate.isEmpty() || endDate.isEmpty() || guests.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Reserva realizada para " + guests + " personas, del " + startDate + " al " + endDate, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void showDatePicker(EditText editText) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            editText.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }
}