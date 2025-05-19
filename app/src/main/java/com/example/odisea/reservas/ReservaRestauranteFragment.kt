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
import com.example.odisea.data.ServicioRestaurante
import com.example.odisea.models.ReservaRestauranteCreate
import com.example.odisea.utils.SharedPreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReservaRestauranteFragment : Fragment() {

    private lateinit var etNombre: EditText
    private lateinit var etUbicacion: EditText
    private lateinit var spnTipoMenu: Spinner
    private lateinit var spnTipoCocina: Spinner
    private lateinit var spnAmbiente: Spinner
    private lateinit var spnTerraza: Spinner
    private lateinit var spnFecha: Spinner
    private lateinit var spnHora: Spinner
    private lateinit var btnReservar: Button
    private lateinit var ivRestauranteImage: ImageView
    private lateinit var lugarActual: Lugar
    private lateinit var sharedPreferenceHelper: SharedPreferenceHelper
    private var serviciosRestaurante: List<ServicioRestaurante> = emptyList()

    companion object {
        private const val ARG_LUGAR = "lugar"

        fun newInstance(lugar: Lugar): ReservaRestauranteFragment {
            val fragment = ReservaRestauranteFragment()
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
        val view = inflater.inflate(R.layout.fragment_reserva_restaurante, container, false)

        // Recuperar el objeto Lugar desde los argumentos
        lugarActual = arguments?.getParcelable(ARG_LUGAR) ?: run {
            Toast.makeText(requireContext(), "Error: Datos del lugar no válidos", Toast.LENGTH_SHORT).show()
            requireActivity().onBackPressedDispatcher.onBackPressed()
            return view
        }

        // Vincular vistas
        etNombre = view.findViewById(R.id.etNombre)
        etUbicacion = view.findViewById(R.id.etUbicacion)
        spnTipoMenu = view.findViewById(R.id.spnTipoMenu)
        spnTipoCocina = view.findViewById(R.id.spnTipoCocina)
        spnAmbiente = view.findViewById(R.id.spnAmbiente)
        spnTerraza = view.findViewById(R.id.spnTerraza)
        spnFecha = view.findViewById(R.id.spnFecha)
        spnHora = view.findViewById(R.id.spnHora)
        btnReservar = view.findViewById(R.id.btnReservar)
        ivRestauranteImage = view.findViewById(R.id.imageView)

        // Mostrar los datos del lugar
        etNombre.setText(lugarActual.nombre)
        etUbicacion.setText("Ubicación: ${lugarActual.ubicacion ?: "No especificado"}")

        // Cargar imagen del restaurante usando Glide
        Glide.with(this)
            .load(lugarActual.imagenUrl?.trim())
            .centerCrop()
            .into(ivRestauranteImage)

        // Inicializar SharedPreferenceHelper
        sharedPreferenceHelper = SharedPreferenceHelper(requireContext())

        // Cargar los servicios del restaurante desde la API
        cargarServiciosRestaurante(lugarActual.id)

        // Acción del botón Reservar
        btnReservar.setOnClickListener {
            realizarReserva()
        }

        return view
    }

    private fun cargarServiciosRestaurante(restauranteId: Int) {
        RetrofitClient.apiService.obtenerServiciosRestaurante(restauranteId).enqueue(object : Callback<List<ServicioRestaurante>> {
            override fun onResponse(call: Call<List<ServicioRestaurante>>, response: Response<List<ServicioRestaurante>>) {
                if (response.isSuccessful && response.body() != null) {
                    serviciosRestaurante = response.body()!!
                    configurarSpinners()
                } else {
                    Toast.makeText(requireContext(), "Error al cargar servicios del restaurante", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<ServicioRestaurante>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error al conectar con el servidor", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun configurarSpinners() {
        // Configurar el Spinner para el tipo de menú
        val tiposMenu = serviciosRestaurante.map { it.tipo_menu }.distinct()
        spnTipoMenu.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tiposMenu)

        // Configurar el Spinner para el tipo de cocina
        val tiposCocina = serviciosRestaurante.map { it.tipo_cocina }.distinct()
        spnTipoCocina.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tiposCocina)

        // Configurar el Spinner para el ambiente
        val ambientes = serviciosRestaurante.map { it.ambiente }.distinct()
        spnAmbiente.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, ambientes)

        // Configurar el Spinner para la terraza
        val opcionesTerraza = listOf("Sí", "No")
        spnTerraza.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opcionesTerraza)

        // Configurar el Spinner para las fechas predefinidas
        val fechasPredefinidas = listOf("2025-05-17", "2025-05-18", "2025-05-19")
        spnFecha.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, fechasPredefinidas)

        // Configurar el Spinner para las horas predefinidas
        val horasPredefinidas = listOf("10:00", "12:00", "14:00", "16:00", "18:00")
        spnHora.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, horasPredefinidas)
    }

    private fun realizarReserva() {
        val tipoMenuSeleccionado = spnTipoMenu.selectedItem.toString()
        val tipoCocinaSeleccionada = spnTipoCocina.selectedItem.toString()
        val ambienteSeleccionado = spnAmbiente.selectedItem.toString()
        val terrazaSeleccionada = spnTerraza.selectedItem.toString()
        val fechaSeleccionada = spnFecha.selectedItem.toString()
        val horaSeleccionada = spnHora.selectedItem.toString()

        if (fechaSeleccionada.isEmpty() || horaSeleccionada.isEmpty()) {
            Toast.makeText(requireContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Obtener el ID del socio usando SharedPreferenceHelper
        val socioId = sharedPreferenceHelper.getSocioId()

        if (socioId == -1) {
            Toast.makeText(requireContext(), "No hay un socio logueado", Toast.LENGTH_SHORT).show()
            return
        }

        // Encontrar el servicio de restaurante seleccionado
        val servicioSeleccionado = serviciosRestaurante.find {
            it.tipo_menu == tipoMenuSeleccionado &&
                    it.tipo_cocina == tipoCocinaSeleccionada &&
                    it.ambiente == ambienteSeleccionado &&
                    it.terraza == (terrazaSeleccionada == "Sí")
        }

        if (servicioSeleccionado == null) {
            Toast.makeText(requireContext(), "Error: Servicio de restaurante no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        // Llamar al endpoint de reserva
        RetrofitClient.apiService.crearReservaRestaurante(
            ReservaRestauranteCreate(
                socio_id = socioId,
                restaurante_id = lugarActual.id,
                servicio_restaurante_id = servicioSeleccionado.id,
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