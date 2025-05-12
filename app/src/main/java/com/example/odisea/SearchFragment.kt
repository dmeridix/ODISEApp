package com.example.odisea

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.odisea.data.Lugar
import com.example.odisea.api.RetrofitClient
import com.example.odisea.adapters.FavoritesAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavoritesAdapter

    private lateinit var tabRestaurant: TextView
    private lateinit var tabHotel: TextView
    private lateinit var tabSpa: TextView
    private lateinit var tabTracks: TextView

    private var currentCategory: String = "restaurante"
    private val socioId = 1
    private var favoritosIds = mutableSetOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchEditText = view.findViewById(R.id.searchEditText)
        recyclerView = view.findViewById(R.id.searchRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        tabRestaurant = view.findViewById(R.id.tabRestaurant)
        tabHotel = view.findViewById(R.id.tabHotel)
        tabSpa = view.findViewById(R.id.tabSpa)
        tabTracks = view.findViewById(R.id.tabTracks)

        setupCategoryTabs()

        // Predeterminar categoría restaurante
        highlightSelectedTab(tabRestaurant)
        fetchFavoritosYBuscar("", currentCategory)

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                fetchFavoritosYBuscar(query, currentCategory)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // Enter en teclado
        searchEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                val query = searchEditText.text.toString().trim()
                // Si está vacío, buscar todos los de la categoría actual
                fetchFavoritosYBuscar(query, currentCategory)
                true
            } else {
                false
            }
        }
    }

    private fun setupCategoryTabs() {
        val tabs = listOf(
            tabRestaurant to "restaurante",
            tabHotel to "hotel",
            tabSpa to "spa",
            tabTracks to "pista"
        )

        for ((tab, category) in tabs) {
            tab.setOnClickListener {
                currentCategory = category
                highlightSelectedTab(tab)
                val currentQuery = searchEditText.text.toString().trim()
                fetchFavoritosYBuscar(currentQuery, currentCategory)
            }
        }
    }

    private fun highlightSelectedTab(selectedTab: TextView) {
        val allTabs = listOf(tabRestaurant, tabHotel, tabSpa, tabTracks)
        allTabs.forEach { it.setTextColor(resources.getColor(R.color.gray)) }
        selectedTab.setTextColor(resources.getColor(R.color.black))
    }

    private fun fetchFavoritosYBuscar(query: String, categoria: String) {
        RetrofitClient.apiService.obtenerFavoritos(socioId).enqueue(object : Callback<List<Lugar>> {
            override fun onResponse(call: Call<List<Lugar>>, response: Response<List<Lugar>>) {
                if (response.isSuccessful) {
                    favoritosIds = response.body()?.map { it.id }?.toMutableSet() ?: mutableSetOf()
                }
                fetchItems(query, categoria)
            }

            override fun onFailure(call: Call<List<Lugar>>, t: Throwable) {
                t.printStackTrace()
                fetchItems(query, categoria)
            }
        })
    }

    private fun fetchItems(query: String, categoria: String) {
        val effectiveQuery = if (query.isBlank()) "" else query
        RetrofitClient.apiService.buscarLugares(effectiveQuery, categoria)
            .enqueue(object : Callback<List<Lugar>> {
                override fun onResponse(call: Call<List<Lugar>>, response: Response<List<Lugar>>) {
                    if (response.isSuccessful) {
                        val lugares = response.body() ?: emptyList()
                        adapter = FavoritesAdapter(
                            lugares,
                            favoritosIds,
                            requireContext(),
                            onItemClick = { lugar -> /* Acción al hacer clic */ },
                            onFavoriteClick = { lugar ->
                                if (favoritosIds.contains(lugar.id)) {
                                    eliminarFavorito(lugar)
                                } else {
                                    agregarAFavoritos(lugar)
                                }
                            }
                        )
                        recyclerView.adapter = adapter
                    }
                }

                override fun onFailure(call: Call<List<Lugar>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }

    private fun agregarAFavoritos(lugar: Lugar) {
        RetrofitClient.apiService.agregarAFavoritos(
            socioId,
            lugar.tipoEstablecimiento ?: "",
            lugar.id
        ).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if (response.isSuccessful) {
                    favoritosIds.add(lugar.id)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun eliminarFavorito(lugar: Lugar) {
        RetrofitClient.apiService.eliminarFavorito(
            socioId,
            lugar.id
        ).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if (response.isSuccessful) {
                    favoritosIds.remove(lugar.id)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}