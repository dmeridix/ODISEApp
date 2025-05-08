package com.example.odisea

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.odisea.api.RetrofitClient
import com.example.odisea.data.Lugar
import com.example.odisea.adapters.PopularPlacesAdapter
import com.example.odisea.utils.SharedPreferenceHelper
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedFragment : Fragment() {

    // Variables para las vistas
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PopularPlacesAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferenceHelper: SharedPreferenceHelper
    private var socioId: Int = -1 // ID del socio (inicialmente -1 si no hay socio logueado)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar SharedPreferenceHelper
        sharedPreferenceHelper = SharedPreferenceHelper(requireContext())
        socioId = sharedPreferenceHelper.getSocioId()

        // Verificar si hay un socio logueado
        if (socioId == -1) {
            Toast.makeText(requireContext(), "No hay un socio logueado", Toast.LENGTH_SHORT).show()
            return
        }

        // Vincular vistas
        recyclerView = view.findViewById(R.id.recycler_view_saved_places)
        progressBar = view.findViewById(R.id.progress_bar)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Mostrar ProgressBar mientras se cargan los datos
        progressBar.visibility = View.VISIBLE

        // Inicializar el adaptador vacío y pasar el FragmentManager
        adapter = PopularPlacesAdapter(emptyList(), requireContext(), childFragmentManager)
        recyclerView.adapter = adapter

        // Cargar datos desde la API usando coroutines
        loadSavedPlaces()
    }

    /**
     * Carga los lugares guardados (favoritos) del socio desde la API.
     */
    private fun loadSavedPlaces() {
        lifecycleScope.launch {
            val apiService = RetrofitClient.apiService
            apiService.obtenerFavoritos(socioId).enqueue(object : Callback<List<Lugar>> {
                override fun onResponse(call: Call<List<Lugar>>, response: Response<List<Lugar>>) {
                    progressBar.visibility = View.GONE // Ocultar ProgressBar

                    if (response.isSuccessful && response.body() != null) {
                        val lugaresGuardados = response.body()!!
                        if (lugaresGuardados.isNotEmpty()) {
                            adapter.updateData(lugaresGuardados) // Actualizar el adaptador con los datos
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "No tienes lugares guardados",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        // Manejar respuesta no exitosa
                        Toast.makeText(
                            requireContext(),
                            "Error al cargar los lugares guardados",
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
                    t.printStackTrace()
                }
            })
        }
    }
}