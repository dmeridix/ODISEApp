package com.example.odisea

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.odisea.adapters.PopularPlacesAdapter
import com.example.odisea.api.RetrofitClient
import com.example.odisea.data.Lugar
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    // Variables para las vistas
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PopularPlacesAdapter
    private lateinit var welcomeText: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño del fragmento
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vincular las vistas del diseño XML
        welcomeText = view.findViewById(R.id.text_welcome)
        progressBar = view.findViewById(R.id.progress_bar)
        recyclerView = view.findViewById(R.id.recycler_view_places)

        // Configurar el RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Obtener el nombre del usuario autenticado
        val userName = getLoggedInUserName()
        welcomeText.text = "Hola, $userName"

        // Inicializar el adaptador vacío
        adapter = PopularPlacesAdapter(null, requireContext())
        recyclerView.adapter = adapter

        // Mostrar ProgressBar mientras se cargan los datos
        progressBar.visibility = View.VISIBLE

        // Cargar los datos de lugares populares desde la API
        loadPopularPlaces()
    }

    /**
     * Simula la obtención del nombre del usuario autenticado.
     * Reemplaza esta lógica con la fuente real de datos (SharedPreferences, Firebase Auth, etc.).
     */
    private fun getLoggedInUserName(): String {
        return "Juan" // Cambia esto por la lógica real para obtener el nombre del usuario
    }

    /**
     * Carga los lugares populares desde la API utilizando Retrofit.
     */
    private fun loadPopularPlaces() {
        lifecycleScope.launch {
            val apiService = RetrofitClient.apiService // Referencia al servicio de la API
            apiService.getLugares().enqueue(object : Callback<List<Lugar>> {
                override fun onResponse(call: Call<List<Lugar>>, response: Response<List<Lugar>>) {
                    progressBar.visibility = View.GONE // Ocultar ProgressBar

                    if (response.isSuccessful && response.body() != null) {
                        val lugares = response.body()
                        lugares?.let {
                            adapter.updateData(it) // Actualizar el adaptador con los datos obtenidos
                        }
                    } else {
                        // Manejar respuesta no exitosa
                        Toast.makeText(
                            requireContext(),
                            "Error al cargar los datos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Lugar>>, t: Throwable) {
                    progressBar.visibility = View.GONE // Ocultar ProgressBar

                    // Mostrar mensaje de error al usuario
                    Toast.makeText(
                        requireContext(),
                        "Fallo al conectar con el servidor",
                        Toast.LENGTH_SHORT
                    ).show()

                    t.printStackTrace() // Imprimir el error en la consola para depuración
                }
            })
        }
    }
}