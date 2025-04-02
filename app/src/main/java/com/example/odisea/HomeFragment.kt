package com.example.odisea

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.odisea.PopularPlacesAdapter
import com.example.odisea.RetrofitClient
import com.example.odisea.data.Lugar
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PopularPlacesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vincular vistas
        recyclerView = view.findViewById(R.id.recycler_view_places)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Inicializar el adaptador vacío
        adapter = PopularPlacesAdapter(null, requireContext())
        recyclerView.adapter = adapter

        // Cargar datos desde la API usando coroutines
        loadPopularPlaces()
    }

    private fun loadPopularPlaces() {
        lifecycleScope.launch {
            val apiService = RetrofitClient.apiService // Usar la propiedad apiService directamente
            apiService.getLugares().enqueue(object : Callback<List<Lugar>> {
                override fun onResponse(call: Call<List<Lugar>>, response: Response<List<Lugar>>) {
                    if (response.isSuccessful && response.body() != null) {
                        val lugares = response.body()
                        lugares?.let {
                            adapter.updateData(it) // Actualizar el adaptador con los datos
                        }
                    } else {
                        // Manejar respuesta no exitosa
                        Toast.makeText(requireContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Lugar>>, t: Throwable) {
                    t.printStackTrace()

                    // Mostrar mensaje de error al usuario
                    Toast.makeText(requireContext(), "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}