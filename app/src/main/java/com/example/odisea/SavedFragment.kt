package com.example.odisea

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.odisea.adapters.FavoritesAdapter
import com.example.odisea.api.RetrofitClient
import com.example.odisea.data.Lugar
import com.example.odisea.utils.SharedPreferenceHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SavedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavoritesAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferenceHelper: SharedPreferenceHelper
    private var socioId: Int = -1
    private var favoritosIds = mutableSetOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_saved, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferenceHelper = SharedPreferenceHelper(requireContext())
        socioId = sharedPreferenceHelper.getSocioId()

        if (socioId == -1) {
            Toast.makeText(requireContext(), "No hay un socio logueado", Toast.LENGTH_SHORT).show()
            return
        }

        recyclerView = view.findViewById(R.id.recycler_view_saved_places)
        progressBar = view.findViewById(R.id.progress_bar_saved)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = FavoritesAdapter(
            emptyList(),
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

        loadSavedPlaces()
    }

    private fun loadSavedPlaces() {
        progressBar.visibility = View.VISIBLE

        RetrofitClient.apiService.obtenerFavoritos(socioId)
            .enqueue(object : Callback<List<Lugar>> {
                override fun onResponse(call: Call<List<Lugar>>, response: Response<List<Lugar>>) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body() != null) {
                        val lugaresGuardados = response.body()!!
                        favoritosIds = lugaresGuardados.map { it.id }.toMutableSet()
                        adapter.updateData(lugaresGuardados, favoritosIds)
                    } else {
                        Toast.makeText(requireContext(), "Error al cargar favoritos", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Lugar>>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Fallo al conectar", Toast.LENGTH_SHORT).show()
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
        RetrofitClient.apiService.agregarAFavoritos(socioId, lugar.tipoEstablecimiento ?: "", lugar.id)
            .enqueue(object : Callback<Map<String, Any>> {
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