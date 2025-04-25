package com.example.odisea

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        if (socioId == -1) {
            // Si no hay socio logueado, mostrar un mensaje o redirigir al inicio de sesión
            Toast.makeText(requireContext(), "No hay un socio logueado", Toast.LENGTH_SHORT).show()
            return
        }

        // Cargar datos del usuario y sus reservas
        loadUserProfile()
        loadUserReservations()
    }

    private fun loadUserProfile() {
        val apiService = RetrofitClient.apiService
        apiService.getUserProfile(socioId).enqueue(object : Callback<UserProfile> {
            override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                if (response.isSuccessful && response.body() != null) {
                    val userProfile = response.body()
                    updateProfileSection(userProfile!!)
                } else {
                    // Manejar respuesta no exitosa
                    Toast.makeText(requireContext(), "Error al cargar el perfil", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                // Mostrar mensaje de error al usuario
                Toast.makeText(requireContext(), "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }

    private fun loadUserReservations() {
        lifecycleScope.launch {
            // Cargar reservas de hoteles
            loadHotelReservations()
            // Cargar reservas de restaurantes
            loadRestaurantReservations()
            // Cargar reservas de spas
            loadSpaReservations()
            // Cargar reservas de pistas
            loadPistaReservations()
        }
    }

    private fun loadHotelReservations() {
        val apiService = RetrofitClient.apiService
        apiService.obtenerReservasHotel(socioId).enqueue(object : Callback<List<ReservaHotel>> {
            override fun onResponse(call: Call<List<ReservaHotel>>, response: Response<List<ReservaHotel>>) {
                if (response.isSuccessful && response.body() != null) {
                    val hotelReservations = response.body()
                    updateHotelReservations(hotelReservations!!)
                } else {
                    // Manejar respuesta no exitosa
                    Toast.makeText(requireContext(), "Error al cargar las reservas de hoteles", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ReservaHotel>>, t: Throwable) {
                // Mostrar mensaje de error al usuario
                Toast.makeText(requireContext(), "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }

    private fun loadRestaurantReservations() {
        val apiService = RetrofitClient.apiService
        apiService.obtenerReservasRestaurante(socioId).enqueue(object : Callback<List<ReservaRestaurante>> {
            override fun onResponse(call: Call<List<ReservaRestaurante>>, response: Response<List<ReservaRestaurante>>) {
                if (response.isSuccessful && response.body() != null) {
                    val restaurantReservations = response.body()
                    updateRestaurantReservations(restaurantReservations!!)
                } else {
                    // Manejar respuesta no exitosa
                    Toast.makeText(requireContext(), "Error al cargar las reservas de restaurantes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ReservaRestaurante>>, t: Throwable) {
                // Mostrar mensaje de error al usuario
                Toast.makeText(requireContext(), "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }

    private fun loadSpaReservations() {
        val apiService = RetrofitClient.apiService
        apiService.obtenerReservasSpa(socioId).enqueue(object : Callback<List<ReservaSpa>> {
            override fun onResponse(call: Call<List<ReservaSpa>>, response: Response<List<ReservaSpa>>) {
                if (response.isSuccessful && response.body() != null) {
                    val spaReservations = response.body()
                    updateSpaReservations(spaReservations!!)
                } else {
                    // Manejar respuesta no exitosa
                    Toast.makeText(requireContext(), "Error al cargar las reservas de spas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ReservaSpa>>, t: Throwable) {
                // Mostrar mensaje de error al usuario
                Toast.makeText(requireContext(), "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }

    private fun loadPistaReservations() {
        val apiService = RetrofitClient.apiService
        apiService.obtenerReservasPista(socioId).enqueue(object : Callback<List<ReservaPista>> {
            override fun onResponse(call: Call<List<ReservaPista>>, response: Response<List<ReservaPista>>) {
                if (response.isSuccessful && response.body() != null) {
                    val pistaReservations = response.body()
                    updatePistaReservations(pistaReservations!!)
                } else {
                    // Manejar respuesta no exitosa
                    Toast.makeText(requireContext(), "Error al cargar las reservas de pistas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ReservaPista>>, t: Throwable) {
                // Mostrar mensaje de error al usuario
                Toast.makeText(requireContext(), "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show()
                t.printStackTrace()
            }
        })
    }

    private fun updateProfileSection(userProfile: UserProfile) {
        // Actualizar sección de perfil con los datos obtenidos
        requireView().findViewById<TextView>(R.id.tv_profile_name).text = "Nombre: ${userProfile.nombre}"
        requireView().findViewById<TextView>(R.id.tv_profile_phone).text = "Teléfono: ${userProfile.telefono ?: "No disponible"}"
        requireView().findViewById<TextView>(R.id.tv_profile_email).text = "Email: ${userProfile.email}"
        requireView().findViewById<TextView>(R.id.tv_profile_dni).text = "DNI: ${userProfile.dni}"
    }

    private fun updateHotelReservations(reservations: List<ReservaHotel>) {
        val tvReservationsHotels = requireView().findViewById<TextView>(R.id.tv_reservations_hotels)
        if (reservations.isNotEmpty()) {
            val formattedReservations = reservations.joinToString("\n• ") { it.fechaEntrada }
            tvReservationsHotels.text = "• $formattedReservations"
        } else {
            tvReservationsHotels.text = "No hay reservas de hoteles"
        }
    }

    private fun updateRestaurantReservations(reservations: List<ReservaRestaurante>) {
        val tvReservationsRestaurants = requireView().findViewById<TextView>(R.id.tv_reservations_restaurants)
        if (reservations.isNotEmpty()) {
            val formattedReservations = reservations.joinToString("\n• ") { "${it.fecha} - ${it.hora}" }
            tvReservationsRestaurants.text = "• $formattedReservations"
        } else {
            tvReservationsRestaurants.text = "No hay reservas de restaurantes"
        }
    }

    private fun updateSpaReservations(reservations: List<ReservaSpa>) {
        val tvReservationsSpas = requireView().findViewById<TextView>(R.id.tv_reservations_spas)
        if (reservations.isNotEmpty()) {
            val formattedReservations = reservations.joinToString("\n• ") { "${it.fecha} - ${it.hora}" }
            tvReservationsSpas.text = "• $formattedReservations"
        } else {
            tvReservationsSpas.text = "No hay reservas de spas"
        }
    }

    private fun updatePistaReservations(reservations: List<ReservaPista>) {
        val tvReservationsPistas = requireView().findViewById<TextView>(R.id.tv_reservations_pistas)
        if (reservations.isNotEmpty()) {
            val formattedReservations = reservations.joinToString("\n• ") { it.fecha }
            tvReservationsPistas.text = "• $formattedReservations"
        } else {
            tvReservationsPistas.text = "No hay reservas de pistas"
        }
    }
}