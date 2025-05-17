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
import com.example.odisea.adapters.FavoritesAdapter
import com.example.odisea.api.RetrofitClient
import com.example.odisea.data.Lugar
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

    private var currentCategory: String? = "restaurante"
    private val socioId = 1
    private var favoritosIds = mutableSetOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_search, container, false)

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
        highlightSelectedTab(tabRestaurant)
        fetchFavoritosYBuscar("", currentCategory)

        searchEditText.setOnEditorActionListener { _, actionId, event ->
            val enterPressed = (actionId == EditorInfo.IME_ACTION_SEARCH) ||
                    (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            if (enterPressed) {
                val query = searchEditText.text.toString()
                fetchFavoritosYBuscar(query, currentCategory)
                true
            } else {
                false
            }
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupCategoryTabs() {
        val tabs = listOf(tabRestaurant to "restaurante", tabHotel to "hotel", tabSpa to "spa", tabTracks to "pista")

        for ((tab, category) in tabs) {
            tab.setOnClickListener {
                currentCategory = category
                highlightSelectedTab(tab)
                fetchFavoritosYBuscar(searchEditText.text.toString(), currentCategory) // Aquí se filtra por categoría
            }
        }
    }

    private fun highlightSelectedTab(selectedTab: TextView?) {
        val allTabs = listOf(tabRestaurant, tabHotel, tabSpa, tabTracks)
        allTabs.forEach { it.setTextColor(resources.getColor(R.color.gray)) }
        selectedTab?.setTextColor(resources.getColor(R.color.black))
    }

    private fun fetchFavoritosYBuscar(query: String, categoria: String?) {
        RetrofitClient.apiService.obtenerFavoritos(socioId).enqueue(object : Callback<List<Lugar>> {
            override fun onResponse(call: Call<List<Lugar>>, response: Response<List<Lugar>>) {
                val nuevosFavoritos = response.body()?.map { it.id }?.toSet() ?: emptySet()
                fetchItems(query, categoria, nuevosFavoritos)
            }

            override fun onFailure(call: Call<List<Lugar>>, t: Throwable) {
                t.printStackTrace()
                fetchItems(query, categoria, emptySet())
            }
        })
    }

    private fun fetchItems(query: String, categoria: String?, favoritos: Set<Int>) {
        val searchQuery = if (query.isBlank()) "" else query
        val categoryQuery = categoria ?: ""

        RetrofitClient.apiService.buscarLugares(searchQuery, categoryQuery)
            .enqueue(object : Callback<List<Lugar>> {
                override fun onResponse(call: Call<List<Lugar>>, response: Response<List<Lugar>>) {
                    if (response.isSuccessful) {
                        val lugares = response.body() ?: emptyList()

                        // Filtrar los favoritos solo entre los lugares que están en la categoría actual
                        val favoritosFiltrados = lugares.filter { favoritos.contains(it.id) }.map { it.id }.toSet()

                        favoritosIds = favoritosFiltrados.toMutableSet()

                        adapter = FavoritesAdapter(
                            lugares,
                            favoritosIds,
                            requireContext(),
                            onItemClick = { lugar -> openDetailFragment(lugar) },
                            onFavoriteClick = { lugar, position ->
                                if (favoritosIds.contains(lugar.id)) {
                                    eliminarFavorito(lugar, position)
                                } else {
                                    agregarAFavoritos(lugar, position)
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

    private fun openDetailFragment(lugar: Lugar) {
        val detailFragment = DetailFragment.newInstance(lugar)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun agregarAFavoritos(lugar: Lugar, position: Int) {
        RetrofitClient.apiService.agregarAFavoritos(
            socioId,
            lugar.tipoEstablecimiento ?: "",
            lugar.id
        ).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if (response.isSuccessful) {
                    favoritosIds.add(lugar.id)
                    adapter.notifyItemChanged(position)
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun eliminarFavorito(lugar: Lugar, position: Int) {
        RetrofitClient.apiService.eliminarFavorito(socioId, lugar.id)
            .enqueue(object : Callback<Map<String, Any>> {
                override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                    if (response.isSuccessful) {
                        favoritosIds.remove(lugar.id)
                        adapter.notifyItemChanged(position)
                    }
                }

                override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }
}