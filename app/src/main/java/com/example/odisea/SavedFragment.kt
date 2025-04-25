package com.example.odisea

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.odisea.api.RetrofitClient
import com.example.odisea.data.Lugar
import com.example.odisea.adapters.PopularPlacesAdapter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PopularPlacesAdapter
    private val socioId = 1 // Cambia esto según tu lógica de autenticación

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vincular vistas
        recyclerView = view.findViewById(R.id.recycler_view_saved_places)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Inicializar el adaptador vacío
        adapter = PopularPlacesAdapter(null, requireContext())
        recyclerView.adapter = adapter

        // Cargar datos desde la API usando coroutines
        loadSavedPlaces()
    }

    private fun loadSavedPlaces() {
        lifecycleScope.launch {
            val apiService = RetrofitClient.apiService
            apiService.obtenerFavoritos(socioId).enqueue(object : Callback<List<Lugar>> {
                override fun onResponse(call: Call<List<Lugar>>, response: Response<List<Lugar>>) {
                    if (response.isSuccessful && response.body() != null) {
                        val lugaresGuardados = response.body()
                        lugaresGuardados?.let {
                            adapter.updateData(it) // Actualizar el adaptador con los datos
                        }
                    } else {
                        // Manejar respuesta no exitosa
                        Toast.makeText(requireContext(), "Error al cargar los lugares guardados", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Lugar>>, t: Throwable) {
                    // Mostrar mensaje de error al usuario
                    Toast.makeText(requireContext(), "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }
            })
        }
    }
}