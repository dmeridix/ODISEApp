package com.example.odisea.reservas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.odisea.HomeFragment
import com.example.odisea.R
import com.example.odisea.api.RetrofitClient
import com.example.odisea.data.Habitacion
import com.example.odisea.data.Lugar
import com.example.odisea.models.ReservaHotelCreate
import com.example.odisea.utils.SharedPreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservaHotelFragment : Fragment() {

    private lateinit var spnTipoHabitacion: Spinner
    private lateinit var etFechaEntrada: EditText
    private lateinit var etFechaSalida: EditText
    private lateinit var btnReservar: Button
    private lateinit var etNombre: EditText
    private lateinit var etUbicacion: EditText
    private lateinit var ivHotelImage: ImageView
    private var habitaciones: List<Habitacion> = emptyList()
    private lateinit var lugarActual: Lugar
    private lateinit var sharedPreferenceHelper: SharedPreferenceHelper

    companion object {
        private const val ARG_LUGAR = "lugar"

        /**
         * Crea una nueva instancia del fragmento con los datos del hotel.
         */
        fun newInstance(lugar: Lugar): ReservaHotelFragment {
            val fragment = ReservaHotelFragment()
            val args = Bundle()
            args.putParcelable(ARG_LUGAR, lugar)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reserves_hotel, container, false)

        // Recuperar el objeto Lugar desde los argumentos
        lugarActual = arguments?.getParcelable(ARG_LUGAR) ?: run {
            Toast.makeText(requireContext(), "Error: Datos del hotel no válidos", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
            return view
        }

        // Vincular vistas
        spnTipoHabitacion = view.findViewById(R.id.spnTipoHabitacion)
        etFechaEntrada = view.findViewById(R.id.etFechaEntrada)
        etFechaSalida = view.findViewById(R.id.etFechaSalida)
        btnReservar = view.findViewById(R.id.btnReservar)
        etNombre = view.findViewById(R.id.etNombrees) // Campo para mostrar el nombre del hotel
        etUbicacion = view.findViewById(R.id.etUbicacion) // Campo para mostrar la ubicación del hotel
        ivHotelImage = view.findViewById(R.id.imageView) // Imagen del hotel

        // Mostrar los datos del hotel
        etNombre.setText(lugarActual.nombre) // Mostrar el nombre del hotel
        etUbicacion.setText(lugarActual.ubicacion) // Mostrar la ubicación del hotel

        Glide.with(this)
            .load(lugarActual.imagenUrl?.trim())
            .centerCrop()
            .into(ivHotelImage)

        // Inicializar SharedPreferenceHelper
        sharedPreferenceHelper = SharedPreferenceHelper(requireContext())

        // Cargar habitaciones del hotel en el spinner
        cargarHabitaciones(lugarActual.id)

        // Acción del botón Reservar
        btnReservar.setOnClickListener {
            realizarReserva()
        }

        return view
    }

    private fun cargarHabitaciones(hotelId: Int) {
        RetrofitClient.apiService.obtenerHabitacionesHotel(hotelId).enqueue(object : Callback<List<Habitacion>> {
            override fun onResponse(call: Call<List<Habitacion>>, response: Response<List<Habitacion>>) {
                if (response.isSuccessful && response.body() != null) {
                    habitaciones = response.body()!!
                    // Mostrar las habitaciones en el spinner (solo el tipo)
                    val nombresHabitaciones = habitaciones.map { it.tipo }
                    spnTipoHabitacion.adapter =
                        ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, nombresHabitaciones)
                } else {
                    Toast.makeText(requireContext(), "Error al cargar habitaciones", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Habitacion>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun realizarReserva() {
        val habitacionSeleccionada = habitaciones[spnTipoHabitacion.selectedItemPosition]
        val fechaEntrada = etFechaEntrada.text.toString().trim()
        val fechaSalida = etFechaSalida.text.toString().trim()

        if (fechaEntrada.isEmpty() || fechaSalida.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, ingresa todas las fechas", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener el ID del socio usando SharedPreferenceHelper
        val socioId = sharedPreferenceHelper.getSocioId()

        if (socioId == -1) {
            Toast.makeText(requireContext(), "No hay un socio logueado", Toast.LENGTH_SHORT).show()
            return
        }

        // Llamar al endpoint de reserva
        RetrofitClient.apiService.crearReservaHotel(
            ReservaHotelCreate(
                socio_id = socioId,
                hotel_id = lugarActual.id,
                habitacion_id = habitacionSeleccionada.id,
                fecha_entrada = fechaEntrada,
                fecha_salida = fechaSalida
            )
        ).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Reserva realizada con éxito", Toast.LENGTH_SHORT).show()

                    // 🔁 Redirigir al HomeFragment
                    val homeFragment = HomeFragment()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, homeFragment)
                        .addToBackStack(null)
                        .commit()
                } else {
                    Toast.makeText(requireContext(), "Error al realizar la reserva", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }
}