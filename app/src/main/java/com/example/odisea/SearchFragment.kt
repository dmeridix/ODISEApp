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
import androidx.recyclerview.widget.RecyclerView
import com.example.odisea.adapters.FavoritesAdapter
import com.example.odisea.api.RetrofitClient
import com.example.odisea.data.Lugar
import kotlinx.coroutines.launch

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

        // Referencias a las vistas
        searchEditText = view.findViewById(R.id.searchEditText)
        recyclerView = view.findViewById(R.id.searchRecyclerView)
        tabRestaurant = view.findViewById(R.id.tabRestaurant)
        tabHotel = view.findViewById(R.id.tabHotel)
        tabSpa = view.findViewById(R.id.tabSpa)
        tabTracks = view.findViewById(R.id.tabTracks)

        // Configurar RecyclerView con el adaptador
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
        tabRestaurant.setOnClickListener { selectTab(tabRestaurant) }
        tabHotel.setOnClickListener { selectTab(tabHotel) }
        tabSpa.setOnClickListener { selectTab(tabSpa) }
        tabTracks.setOnClickListener { selectTab(tabTracks) }

        // Cargar restaurantes por defecto
        selectTab(tabRestaurant)
    }

    private fun selectTab(selected: TextView) {
        resetTabs()
        selected.setTextColor(resources.getColor(android.R.color.black))
        selected.setBackgroundColor(resources.getColor(android.R.color.darker_gray))

        // Obtener la categoría seleccionada
        val category = when (selected.id) {
            R.id.tabRestaurant -> "restaurant"
            R.id.tabHotel -> "hotel"
            R.id.tabSpa -> "spa"
            else -> "track"
        }

        // Cargar datos para la categoría seleccionada
        fetchItems(searchEditText.text.toString(), category)
    }

    private fun resetTabs() {
        listOf(tabRestaurant, tabHotel, tabSpa, tabTracks).forEach { tab ->
            tab.setTextColor(resources.getColor(android.R.color.darker_gray))
            tab.setBackgroundColor(resources.getColor(android.R.color.transparent))
        }
    }

    private fun getSelectedCategory(): String {
        return when {
            tabRestaurant.currentTextColor == resources.getColor(android.R.color.black) -> "restaurant"
            tabHotel.currentTextColor == resources.getColor(android.R.color.black) -> "hotel"
            tabSpa.currentTextColor == resources.getColor(android.R.color.black) -> "spa"
            else -> "track"
        }
    }

    private fun fetchItems(query: String, category: String) {
        lifecycleScope.launch {
            try {
                // Llamar al endpoint correspondiente
                val response = RetrofitClient.apiService.searchItems(query, category).execute()
                if (response.isSuccessful && response.body() != null) {
                    val nuevosLugares = response.body()!!
                    updateRecyclerView(nuevosLugares)
                } else {
                    // Manejar respuesta no exitosa
                    println("Respuesta no exitosa: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Mostrar mensaje de error al usuario
                println("Error al cargar datos: ${e.message}")
            }
        }
    }

    private fun updateRecyclerView(newLugares: List<Lugar>) {
        lugares = newLugares
        adapter.updateData(lugares)
    }
}