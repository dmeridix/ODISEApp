package com.example.odisea

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.odisea.api.RetrofitClient
import com.example.odisea.data.ReservaHotel
import com.example.odisea.data.ReservaPista
import com.example.odisea.data.ReservaRestaurante
import com.example.odisea.data.ReservaSpa
import com.example.odisea.data.UserProfile
import com.example.odisea.utils.SharedPreferenceHelper
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PreferencesFragment : Fragment() {

    // Instancia de SharedPreferenceHelper para acceder al socioId
    private lateinit var sharedPreferenceHelper: SharedPreferenceHelper
    private var socioId: Int = -1 // ID del socio (inicialmente -1 si no hay socio logueado)

    // Referencias a las vistas
    private lateinit var tvProfileName: TextView
    private lateinit var tvProfilePhone: TextView
    private lateinit var tvProfileEmail: TextView
    private lateinit var tvProfileDni: TextView
    private lateinit var tvReservationsHotels: TextView
    private lateinit var tvReservationsRestaurants: TextView
    private lateinit var tvReservationsSpas: TextView
    private lateinit var tvReservationsPistas: TextView
    private lateinit var btnUnsubscribe: Button
    private lateinit var btnCancelReservation: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_preferences, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar SharedPreferenceHelper
        sharedPreferenceHelper = SharedPreferenceHelper(requireContext())
        socioId = sharedPreferenceHelper.getSocioId()

        // Vincular las vistas
        tvProfileName = view.findViewById(R.id.tv_profile_name)
        tvProfilePhone = view.findViewById(R.id.tv_profile_phone)
        tvProfileEmail = view.findViewById(R.id.tv_profile_email)
        tvProfileDni = view.findViewById(R.id.tv_profile_dni)
        tvReservationsHotels = view.findViewById(R.id.tv_reservations_hotels)
        tvReservationsRestaurants = view.findViewById(R.id.tv_reservations_restaurants)
        tvReservationsSpas = view.findViewById(R.id.tv_reservations_spas)
        tvReservationsPistas = view.findViewById(R.id.tv_reservations_pistas)
        btnUnsubscribe = view.findViewById(R.id.btn_unsubscribe)
        btnCancelReservation = view.findViewById(R.id.btn_cancel_reservation)

        // Verificar si hay un socio logueado
        if (socioId == -1) {
            Toast.makeText(requireContext(), "No hay un socio logueado", Toast.LENGTH_SHORT).show()
            return
        }

        // Cargar datos del usuario y sus reservas
        loadUserProfile()
        loadUserReservations()

        // Configurar el botón "Darse de baja"
        btnUnsubscribe.setOnClickListener {
            unsubscribeUser()
        }

        // Configurar el botón "Cancelar Reserva"
        btnCancelReservation.setOnClickListener {
            cancelReservation()
        }
    }

    /**
     * Carga el perfil del socio desde la API.
     */
    private fun loadUserProfile() {
        lifecycleScope.launch {
            RetrofitClient.apiService.obtenerPerfilSocio(socioId).enqueue(object : Callback<UserProfile> {
                override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                    if (response.isSuccessful && response.body() != null) {
                        val userProfile = response.body()!!
                        tvProfileName.text = "Nombre: ${userProfile.nombre}"
                        tvProfilePhone.text = "Teléfono: ${userProfile.telefono}"
                        tvProfileEmail.text = "Email: ${userProfile.email}"
                        tvProfileDni.text = "DNI: ${userProfile.dni}"
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error al cargar el perfil del usuario",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Fallo al conectar con el servidor",
                        Toast.LENGTH_SHORT
                    ).show()
                    t.printStackTrace()
                }
            })
        }
    }

    /**
     * Carga las reservas del socio desde la API.
     */
    private fun loadUserReservations() {
        loadHotelReservations()
        loadRestaurantReservations()
        loadSpaReservations()
        loadPistaReservations()
    }

    private fun loadHotelReservations() {
        lifecycleScope.launch {
            RetrofitClient.apiService.obtenerReservasHotel(socioId).enqueue(object : Callback<List<ReservaHotel>> {
                override fun onResponse(call: Call<List<ReservaHotel>>, response: Response<List<ReservaHotel>>) {
                    if (response.isSuccessful && response.body() != null) {
                        val reservas = response.body()!!
                        tvReservationsHotels.text = if (reservas.isNotEmpty()) {
                            "Reservas Hoteles: ${reservas.size}"
                        } else {
                            "No hay reservas de hoteles"
                        }
                    } else {
                        tvReservationsHotels.text = "Error al cargar reservas de hoteles"
                    }
                }

                override fun onFailure(call: Call<List<ReservaHotel>>, t: Throwable) {
                    tvReservationsHotels.text = "Fallo al cargar reservas de hoteles"
                    t.printStackTrace()
                }
            })
        }
    }

    private fun loadRestaurantReservations() {
        lifecycleScope.launch {
            RetrofitClient.apiService.obtenerReservasRestaurante(socioId).enqueue(object : Callback<List<ReservaRestaurante>> {
                override fun onResponse(call: Call<List<ReservaRestaurante>>, response: Response<List<ReservaRestaurante>>) {
                    if (response.isSuccessful && response.body() != null) {
                        val reservas = response.body()!!
                        tvReservationsRestaurants.text = if (reservas.isNotEmpty()) {
                            "Reservas Restaurantes: ${reservas.size}"
                        } else {
                            "No hay reservas de restaurantes"
                        }
                    } else {
                        tvReservationsRestaurants.text = "Error al cargar reservas de restaurantes"
                    }
                }

                override fun onFailure(call: Call<List<ReservaRestaurante>>, t: Throwable) {
                    tvReservationsRestaurants.text = "Fallo al cargar reservas de restaurantes"
                    t.printStackTrace()
                }
            })
        }
    }

    private fun loadSpaReservations() {
        lifecycleScope.launch {
            RetrofitClient.apiService.obtenerReservasSpa(socioId).enqueue(object : Callback<List<ReservaSpa>> {
                override fun onResponse(call: Call<List<ReservaSpa>>, response: Response<List<ReservaSpa>>) {
                    if (response.isSuccessful && response.body() != null) {
                        val reservas = response.body()!!
                        tvReservationsSpas.text = if (reservas.isNotEmpty()) {
                            "Reservas Spas: ${reservas.size}"
                        } else {
                            "No hay reservas de spas"
                        }
                    } else {
                        tvReservationsSpas.text = "Error al cargar reservas de spas"
                    }
                }

                override fun onFailure(call: Call<List<ReservaSpa>>, t: Throwable) {
                    tvReservationsSpas.text = "Fallo al cargar reservas de spas"
                    t.printStackTrace()
                }
            })
        }
    }

    private fun loadPistaReservations() {
        lifecycleScope.launch {
            RetrofitClient.apiService.obtenerReservasPista(socioId).enqueue(object : Callback<List<ReservaPista>> {
                override fun onResponse(call: Call<List<ReservaPista>>, response: Response<List<ReservaPista>>) {
                    if (response.isSuccessful && response.body() != null) {
                        val reservas = response.body()!!
                        tvReservationsPistas.text = if (reservas.isNotEmpty()) {
                            "Reservas Pistas: ${reservas.size}"
                        } else {
                            "No hay reservas de pistas"
                        }
                    } else {
                        tvReservationsPistas.text = "Error al cargar reservas de pistas"
                    }
                }

                override fun onFailure(call: Call<List<ReservaPista>>, t: Throwable) {
                    tvReservationsPistas.text = "Fallo al cargar reservas de pistas"
                    t.printStackTrace()
                }
            })
        }
    }

    /**
     * Elimina al usuario (darse de baja).
     */
    private fun unsubscribeUser() {
        lifecycleScope.launch {
            RetrofitClient.apiService.eliminarSocio(socioId).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Usuario eliminado correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        sharedPreferenceHelper.clearSocioData()
                        requireActivity().finish() // Cerrar la actividad actual
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error al eliminar el usuario",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Fallo al conectar con el servidor",
                        Toast.LENGTH_SHORT
                    ).show()
                    t.printStackTrace()
                }
            })
        }
    }

    /**
     * Cancela una reserva seleccionada.
     */
    private fun cancelReservation() {
        Toast.makeText(requireContext(), "Funcionalidad no implementada", Toast.LENGTH_SHORT).show()
    }
}