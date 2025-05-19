package com.example.odisea

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.odisea.adapters.ReservationAdapter
import com.example.odisea.api.RetrofitClient
import com.example.odisea.data.Lugar
import com.example.odisea.data.Reserva
import com.example.odisea.utils.SharedPreferenceHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

class PreferencesFragment : Fragment() {

    private lateinit var sharedPrefs: SharedPreferenceHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReservationAdapter
    private val reservasList = mutableListOf<Reserva>()

    private lateinit var tvName: TextView
    private lateinit var tvPhone: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvDni: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_preferences, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPrefs = SharedPreferenceHelper(requireContext())
        val socioId = sharedPrefs.getSocioId()

        if (socioId == -1) {
            Toast.makeText(requireContext(), "No hay un socio logueado", Toast.LENGTH_SHORT).show()
            return
        }

        // Bind views
        tvName = view.findViewById(R.id.tv_profile_name)
        tvPhone = view.findViewById(R.id.tv_profile_phone)
        tvEmail = view.findViewById(R.id.tv_profile_email)
        tvDni = view.findViewById(R.id.tv_profile_dni)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ReservationAdapter(reservasList) { reserva ->
            cancelarReserva(socioId, reserva)
        }
        recyclerView.adapter = adapter

        cargarPerfil(socioId)
        cargarTodasLasReservas(socioId)
    }

    private fun cargarPerfil(socioId: Int) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.apiService.obtenerPerfilSocio(socioId).awaitResponse()
                if (response.isSuccessful && response.body() != null) {
                    val perfil = response.body()!!
                    tvName.text = "Nombre: ${perfil.nombre}"
                    tvPhone.text = "Teléfono: ${perfil.telefono}"
                    tvEmail.text = "Email: ${perfil.email}"
                    tvDni.text = "DNI: ${perfil.dni}"
                } else {
                    Toast.makeText(requireContext(), "Error al cargar perfil", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarTodasLasReservas(socioId: Int) {
        lifecycleScope.launch {
            try {
                val reservas = mutableListOf<Reserva>()

                val hoteles = RetrofitClient.apiService.obtenerReservasHotel(socioId).awaitResponse().body() ?: emptyList()
                reservas.addAll(hoteles.map { Reserva("Hotel", it.nombreLugar, it.fechaEntrada, it.id) })

                val restaurantes = RetrofitClient.apiService.obtenerReservasRestaurante(socioId).awaitResponse().body() ?: emptyList()
                reservas.addAll(restaurantes.map { Reserva("Restaurante", it.nombreLugar, it.fecha, it.id) })

                val spas = RetrofitClient.apiService.obtenerReservasSpa(socioId).awaitResponse().body() ?: emptyList()
                reservas.addAll(spas.map { Reserva("Spa", it.nombreLugar, it.fecha, it.id) })

                val pistas = RetrofitClient.apiService.obtenerReservasPista(socioId).awaitResponse().body() ?: emptyList()
                reservas.addAll(pistas.map { Reserva("Pista", it.nombreLugar, it.fecha, it.id) })

                withContext(Dispatchers.Main) {
                    reservasList.clear()
                    reservasList.addAll(reservas)
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error al cargar reservas", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cancelarReserva(socioId: Int, reserva: Reserva) {
        val call: Call<Void>? = when (reserva.tipo.lowercase()) {
            "hotel" -> RetrofitClient.apiService.cancelarReservaHotel(socioId, reserva.idLugar)
            "restaurante" -> RetrofitClient.apiService.cancelarReservaRestaurante(socioId, reserva.idLugar)
            "spa" -> RetrofitClient.apiService.cancelarReservaSpa(socioId, reserva.idLugar)
            "pista" -> RetrofitClient.apiService.cancelarReservaPista(socioId, reserva.idLugar)
            else -> null
        }

        if (call == null) {
            Toast.makeText(requireContext(), "Tipo de reserva desconocido", Toast.LENGTH_SHORT).show()
            return
        }

        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Reserva cancelada", Toast.LENGTH_SHORT).show()
                    reservasList.remove(reserva)
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Error al cancelar reserva", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Fallo al conectar", Toast.LENGTH_SHORT).show()
            }
        })
    }
}