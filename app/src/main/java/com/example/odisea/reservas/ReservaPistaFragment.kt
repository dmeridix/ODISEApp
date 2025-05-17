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
import com.example.odisea.R
import com.example.odisea.api.RetrofitClient
import com.example.odisea.data.Lugar
import com.example.odisea.data.ServicioPista
import com.example.odisea.models.ReservaPistaCreate
import com.example.odisea.utils.SharedPreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservaPistaFragment : Fragment() {

    private lateinit var etNombre: EditText
    private lateinit var etUbicacion: EditText
    private lateinit var spnTipoPista: Spinner
    private lateinit var spnNumeroPersonas: Spinner
    private lateinit var spnMaterial: Spinner
    private lateinit var spnTiempoDisponible: Spinner
    private lateinit var etFechaHora: EditText
    private lateinit var btnReservar: Button
    private lateinit var ivPistaImage: ImageView
    private lateinit var lugarActual: Lugar
    private lateinit var sharedPreferenceHelper: SharedPreferenceHelper
    private var serviciosPista: List<ServicioPista> = emptyList()

    companion object {
        private const val ARG_LUGAR = "lugar"

        fun newInstance(lugar: Lugar): ReservaPistaFragment {
            val fragment = ReservaPistaFragment()
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
        val view = inflater.inflate(R.layout.fragment_reserva_pista, container, false)

        // Recuperar el objeto Lugar desde los argumentos
        lugarActual = arguments?.getParcelable(ARG_LUGAR) ?: run {
            Toast.makeText(requireContext(), "Error: Datos del lugar no válidos", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
            return view
        }

        // Vincular vistas
        etNombre = view.findViewById(R.id.etNombre)
        etUbicacion = view.findViewById(R.id.etUbicacion)
        spnTipoPista = view.findViewById(R.id.spnTipoPista)
        spnNumeroPersonas = view.findViewById(R.id.spnNumeroPersonas)
        spnMaterial = view.findViewById(R.id.spnMaterial)
        spnTiempoDisponible = view.findViewById(R.id.spnTiempoDisponible)
        etFechaHora = view.findViewById(R.id.etFechaHora)
        btnReservar = view.findViewById(R.id.btnReservar)
        ivPistaImage = view.findViewById(R.id.imageView)

        // Mostrar los datos del lugar
        etNombre.setText(lugarActual.nombre)
        etUbicacion.setText("Ubicación: ${lugarActual.ubicacion ?: "No especificado"}")

        // Cargar imagen de la pista usando Glide
        Glide.with(this)
            .load(lugarActual.imagenUrl?.trim())
            .centerCrop()
            .into(ivPistaImage)

        // Inicializar SharedPreferenceHelper
        sharedPreferenceHelper = SharedPreferenceHelper(requireContext())

        // Cargar los servicios de la pista desde la API
        cargarServiciosPista(lugarActual.id)

        // Acción del botón Reservar
        btnReservar.setOnClickListener {
            realizarReserva()
        }

        return view
    }

    private fun cargarServiciosPista(pistaId: Int) {
        RetrofitClient.apiService.obtenerServiciosPista(pistaId).enqueue(object : Callback<List<ServicioPista>> {
            override fun onResponse(call: Call<List<ServicioPista>>, response: Response<List<ServicioPista>>) {
                if (response.isSuccessful && response.body() != null) {
                    serviciosPista = response.body()!!
                    configurarSpinners()
                } else {
                    Toast.makeText(requireContext(), "Error al cargar servicios de la pista", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ServicioPista>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun configurarSpinners() {
        // Configurar el Spinner para el tipo de pista
        val tiposPista = serviciosPista.map { it.tipoPista ?: "No especificado" }.distinct()
        spnTipoPista.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tiposPista)

        // Configurar el Spinner para el número de personas
        val numerosPersonas = serviciosPista.mapNotNull { it.numeroPersonas }.distinct()
        spnNumeroPersonas.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, numerosPersonas)

        // Configurar el Spinner para el material
        val materiales = serviciosPista.map { it.material ?: "No especificado" }.distinct()
        spnMaterial.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, materiales)

        // Configurar el Spinner para el tiempo disponible
        val tiemposDisponibles = serviciosPista.map { it.tiempoDisponible ?: "No especificado" }.distinct()
        spnTiempoDisponible.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tiemposDisponibles)
    }

    private fun realizarReserva() {
        val tipoPistaSeleccionado = spnTipoPista.selectedItem.toString()
        val numeroPersonasSeleccionado = spnNumeroPersonas.selectedItem.toString().toIntOrNull() ?: 0
        val materialSeleccionado = spnMaterial.selectedItem.toString()
        val tiempoDisponibleSeleccionado = spnTiempoDisponible.selectedItem.toString()
        val fechaHora = etFechaHora.text.toString().trim()

        if (numeroPersonasSeleccionado == 0 || fechaHora.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener el ID del socio usando SharedPreferenceHelper
        val socioId = sharedPreferenceHelper.getSocioId()

        if (socioId == -1) {
            Toast.makeText(requireContext(), "No hay un socio logueado", Toast.LENGTH_SHORT).show()
            return
        }

        // Encontrar el servicio de pista seleccionado
        val servicioSeleccionado = serviciosPista.find {
            it.tipoPista == tipoPistaSeleccionado &&
                    it.numeroPersonas == numeroPersonasSeleccionado &&
                    it.material == materialSeleccionado &&
                    it.tiempoDisponible == tiempoDisponibleSeleccionado
        }

        if (servicioSeleccionado == null) {
            Toast.makeText(requireContext(), "Error: Servicio de pista no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        // Llamar al endpoint de reserva
        RetrofitClient.apiService.crearReservaPista(
            ReservaPistaCreate(
                socio_id = socioId,
                pista_id = lugarActual.id,
                servicio_pista_id = servicioSeleccionado.id,
                fecha = fechaHora
            )
        ).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Reserva realizada con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    try {
                        val errorBody = response.errorBody()?.string()
                        Toast.makeText(requireContext(), "Error al realizar la reserva: $errorBody", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Error desconocido al realizar la reserva.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(requireContext(), "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }
}