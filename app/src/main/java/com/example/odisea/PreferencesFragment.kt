package com.example.odisea.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.odisea.R
import com.example.odisea.adapters.ReservationAdapter
import com.example.odisea.api.RetrofitClient
import com.example.odisea.data.Reserva
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class PreferenciasFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReservationAdapter
    private val reservasList = mutableListOf<Reserva>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preferences, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vincular vistas
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Inicializar el adaptador
        recyclerView.adapter = adapter

        // Cargar datos del perfil del socio
        cargarPerfilSocio()

        // Cargar reservas del socio
        cargarReservas()
    }

    private fun cargarPerfilSocio() {
        // Simular datos del perfil del socio
        val nombre = "Nombre del Socio"
        val telefono = "Teléfono del Socio"
        val email = "Email del Socio"
        val dni = "DNI del Socio"

        // Actualizar UI
        view?.findViewById<TextView>(R.id.tv_profile_name)?.text = "Nombre: $nombre"
        view?.findViewById<TextView>(R.id.tv_profile_phone)?.text = "Teléfono: $telefono"
        view?.findViewById<TextView>(R.id.tv_profile_email)?.text = "Email: $email"
        view?.findViewById<TextView>(R.id.tv_profile_dni)?.text = "DNI: $dni"
    }

    private fun cargarReservas() {
        lifecycleScope.launch {
            try {
                val socioId = obtenerSocioId() // Obtener el ID del socio desde SharedPreferences
                val reservasHotel = RetrofitClient.apiService.obtenerReservasHotel(socioId).execute().body() ?: emptyList()
                val reservasPista = RetrofitClient.apiService.obtenerReservasPista(socioId).execute().body() ?: emptyList()
                val reservasRestaurante = RetrofitClient.apiService.obtenerReservasRestaurante(socioId).execute().body() ?: emptyList()
                val reservasSpa = RetrofitClient.apiService.obtenerReservasSpa(socioId).execute().body() ?: emptyList()

                // Procesar todas las reservas
                val reservas = mutableListOf<Map<String, String>>()


                // Actualizar el RecyclerView
                reservasList.clear()
                adapter.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
                Snackbar.make(requireView(), "Error al cargar las reservas", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun obtenerSocioId(): Int {
        // Implementa la lógica para obtener el ID del socio desde SharedPreferences
        return 1 // Cambia esto según tu implementación
    }
}