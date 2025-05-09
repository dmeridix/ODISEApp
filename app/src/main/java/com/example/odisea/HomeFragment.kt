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
import com.example.odisea.utils.SharedPreferenceHelper
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), PopularPlacesAdapter.OnItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PopularPlacesAdapter
    private lateinit var welcomeText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferenceHelper: SharedPreferenceHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferenceHelper = SharedPreferenceHelper(requireContext())

        welcomeText = view.findViewById(R.id.text_welcome)
        progressBar = view.findViewById(R.id.progress_bar)
        recyclerView = view.findViewById(R.id.recycler_view_places)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val userName = getLoggedInUserName()
        welcomeText.text = "Hola, $userName"

        // 👇 Pasamos "this" como listener
        adapter = PopularPlacesAdapter(emptyList(), requireContext(), this)
        recyclerView.adapter = adapter

        progressBar.visibility = View.VISIBLE
        loadPopularPlaces()
    }

    private fun getLoggedInUserName(): String {
        return sharedPreferenceHelper.getSocioNombre() ?: "Usuario"
    }

    private fun loadPopularPlaces() {
        lifecycleScope.launch {
            val apiService = RetrofitClient.apiService
            apiService.getLugares().enqueue(object : Callback<List<Lugar>> {
                override fun onResponse(call: Call<List<Lugar>>, response: Response<List<Lugar>>) {
                    progressBar.visibility = View.GONE
                    if (response.isSuccessful && response.body() != null) {
                        val lugares = response.body()!!
                        adapter.updateData(lugares)
                    } else {
                        Toast.makeText(requireContext(), "Error al cargar los datos", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Lugar>>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Fallo al conectar con el servidor", Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }
            })
        }
    }

    // 👇 Aquí manejamos el click y abrimos el DetailFragment
    override fun onItemClick(lugar: Lugar) {
        val detailFragment = DetailFragment.newInstance(lugar)

        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, detailFragment)
            .addToBackStack(null)
            .commit()
    }
}
