package com.example.odisea

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.odisea.adapters.FavoritesAdapter
import com.example.odisea.api.RetrofitClient
import com.example.odisea.data.Lugar
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var tabRestaurant: TextView
    private lateinit var tabHotel: TextView
    private lateinit var tabSpa: TextView
    private lateinit var tabTracks: TextView

    private lateinit var adapter: FavoritesAdapter
    private var lugares: List<Lugar> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vincular las vistas
        searchEditText = view.findViewById(R.id.searchEditText)
        recyclerView = view.findViewById(R.id.searchRecyclerView)
        tabRestaurant = view.findViewById(R.id.tabRestaurant)
        tabHotel = view.findViewById(R.id.tabHotel)
        tabSpa = view.findViewById(R.id.tabSpa)
        tabTracks = view.findViewById(R.id.tabTracks)

        // Configurar el RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = FavoritesAdapter(lugares, requireContext())
        recyclerView.adapter = adapter

        // Listener para el buscador
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                fetchItems(query, getSelectedCategory())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Listeners para las pestañas
        tabRestaurant.setOnClickListener { selectTab(tabRestaurant, "restaurant") }
        tabHotel.setOnClickListener { selectTab(tabHotel, "hotel") }
        tabSpa.setOnClickListener { selectTab(tabSpa, "spa") }
        tabTracks.setOnClickListener { selectTab(tabTracks, "pista") }

        // Cargar restaurantes por defecto
        selectTab(tabRestaurant, "restaurant")
    }

    /**
     * Selecciona una pestaña y carga los datos correspondientes.
     */
    private fun selectTab(selected: TextView, category: String) {
        resetTabs()
        selected.setTextColor(resources.getColor(android.R.color.black))
        selected.setBackgroundColor(resources.getColor(android.R.color.darker_gray))

        // Cargar datos para la categoría seleccionada
        fetchItems(searchEditText.text.toString(), category)
    }

    /**
     * Reinicia el estilo de todas las pestañas.
     */
    private fun resetTabs() {
        listOf(tabRestaurant, tabHotel, tabSpa, tabTracks).forEach { tab ->
            tab.setTextColor(resources.getColor(android.R.color.darker_gray))
            tab.setBackgroundColor(resources.getColor(android.R.color.transparent))
        }
    }

    /**
     * Obtiene la categoría seleccionada actualmente.
     */
    private fun getSelectedCategory(): String {
        return when {
            tabRestaurant.currentTextColor == resources.getColor(android.R.color.black) -> "restaurant"
            tabHotel.currentTextColor == resources.getColor(android.R.color.black) -> "hotel"
            tabSpa.currentTextColor == resources.getColor(android.R.color.black) -> "spa"
            else -> "pista"
        }
    }

    /**
     * Realiza la búsqueda de elementos según el término y la categoría.
     */
    private fun fetchItems(query: String, category: String) {
        lifecycleScope.launch {
            RetrofitClient.apiService.buscarLugares(query, category).enqueue(object : Callback<List<Lugar>> {
                override fun onResponse(call: Call<List<Lugar>>, response: Response<List<Lugar>>) {
                    if (response.isSuccessful && response.body() != null) {
                        val nuevosLugares = response.body()!!
                        updateRecyclerView(nuevosLugares)
                    } else {
                        // Mostrar mensaje de error al usuario
                        println("Respuesta no exitosa: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<Lugar>>, t: Throwable) {
                    t.printStackTrace()
                    // Mostrar mensaje de error al usuario
                    println("Error al cargar datos: ${t.message}")
                }
            })
        }
    }

    /**
     * Actualiza el RecyclerView con los nuevos datos.
     */
    private fun updateRecyclerView(newLugares: List<Lugar>) {
        lugares = newLugares
        adapter.updateData(lugares)
    }
}