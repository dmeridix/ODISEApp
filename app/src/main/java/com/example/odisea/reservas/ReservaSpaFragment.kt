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
import com.example.odisea.data.Lugar
import com.example.odisea.data.ServicioSpa
import com.example.odisea.models.ReservaSpaCreate
import com.example.odisea.utils.SharedPreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservaSpaFragment : Fragment() {

    private lateinit var etNombre: EditText
    private lateinit var etUbicacion: EditText
    private lateinit var spnTipoServicio: Spinner
    private lateinit var spnDuracion: Spinner
    private lateinit var spnFecha: Spinner
    private lateinit var spnHora: Spinner
    private lateinit var btnReservar: Button
    private lateinit var ivSpaImage: ImageView
    private lateinit var lugarActual: Lugar
    private lateinit var sharedPreferenceHelper: SharedPreferenceHelper
    private var serviciosSpa: List<ServicioSpa> = emptyList()

    companion object {
        private const val ARG_LUGAR = "lugar"

        fun newInstance(lugar: Lugar): ReservaSpaFragment {
            val fragment = ReservaSpaFragment()
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
        val view = inflater.inflate(R.layout.fragment_reserva_spa, container, false)

        // Recuperar el objeto Lugar desde los argumentos
        lugarActual = arguments?.getParcelable(ARG_LUGAR) ?: run {
            Toast.makeText(requireContext(), "Error: Datos del lugar no válidos", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
            return view
        }

        // Vincular vistas
        etNombre = view.findViewById(R.id.etNombre)
        etUbicacion = view.findViewById(R.id.etUbicacion)
        spnTipoServicio = view.findViewById(R.id.spnTipoServicio)
        spnDuracion = view.findViewById(R.id.spnDuracion)
        spnFecha = view.findViewById(R.id.spnFecha)
        spnHora = view.findViewById(R.id.spnHora)
        btnReservar = view.findViewById(R.id.btnReservar)
        ivSpaImage = view.findViewById(R.id.imageView)

        // Mostrar los datos del lugar
        etNombre.setText(lugarActual.nombre)
        etUbicacion.setText("Ubicación: ${lugarActual.ubicacion ?: "No especificado"}")

        // Cargar imagen del spa usando Glide
        Glide.with(this)
            .load(lugarActual.imagenUrl?.trim())
            .centerCrop()
            .into(ivSpaImage)

        // Inicializar SharedPreferenceHelper
        sharedPreferenceHelper = SharedPreferenceHelper(requireContext())

        // Cargar los servicios del spa desde la API
        cargarServiciosSpa(lugarActual.id)

        // Acción del botón Reservar
        btnReservar.setOnClickListener {
            realizarReserva()
        }

        return view
    }

    private fun cargarServiciosSpa(spaId: Int) {
        RetrofitClient.apiService.obtenerServiciosSpa(spaId).enqueue(object : Callback<List<ServicioSpa>> {
            override fun onResponse(call: Call<List<ServicioSpa>>, response: Response<List<ServicioSpa>>) {
                if (response.isSuccessful && response.body() != null) {
                    serviciosSpa = response.body()!!
                    configurarSpinners()
                } else {
                    Toast.makeText(requireContext(), "Error al cargar servicios del spa", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ServicioSpa>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun configurarSpinners() {
        // Configurar el Spinner para el tipo de servicio
        val tiposServicio = serviciosSpa.map { it.tipo_servicio }.distinct()
        spnTipoServicio.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tiposServicio)

        // Configurar el Spinner para la duración
        val duraciones = serviciosSpa.map { "${it.duracion} minutos" }.distinct()
        spnDuracion.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, duraciones)

        // Configurar el Spinner para las fechas predefinidas
        val fechasPredefinidas = listOf("2025-05-17", "2025-05-18", "2025-05-19")
        spnFecha.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, fechasPredefinidas)

        // Configurar el Spinner para las horas predefinidas
        val horasPredefinidas = listOf("10:00", "12:00", "14:00", "16:00", "18:00")
        spnHora.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, horasPredefinidas)
    }

    private fun realizarReserva() {
        val tipoServicioSeleccionado = spnTipoServicio.selectedItem.toString()
        val duracionSeleccionada = spnDuracion.selectedItem.toString().replace(" minutos", "").toIntOrNull() ?: 0
        val fechaSeleccionada = spnFecha.selectedItem.toString()
        val horaSeleccionada = spnHora.selectedItem.toString()

        if (duracionSeleccionada == 0 || fechaSeleccionada.isEmpty() || horaSeleccionada.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener el ID del socio usando SharedPreferenceHelper
        val socioId = sharedPreferenceHelper.getSocioId()

        if (socioId == -1) {
            Toast.makeText(requireContext(), "No hay un socio logueado", Toast.LENGTH_SHORT).show()
            return
        }

        // Encontrar el servicio de spa seleccionado
        val servicioSeleccionado = serviciosSpa.find {
            it.tipo_servicio == tipoServicioSeleccionado && it.duracion == duracionSeleccionada
        }

        if (servicioSeleccionado == null) {
            Toast.makeText(requireContext(), "Error: Servicio de spa no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        // Llamar al endpoint de reserva
        RetrofitClient.apiService.crearReservaSpa(
            ReservaSpaCreate(
                socio_id = socioId,
                spa_id = lugarActual.id,
                servicio_spa_id = servicioSeleccionado.id,
                fecha = fechaSeleccionada,
                hora = horaSeleccionada
            )
        ).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Reserva realizada con éxito", Toast.LENGTH_SHORT).show()

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