package com.example.odisea

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.odisea.api.RetrofitClient
import com.example.odisea.data.Lugar
import com.example.odisea.databinding.FragmentDetailBinding
import com.example.odisea.reservas.ReservaHotelFragment
import com.example.odisea.reservas.ReservaPistaFragment
import com.example.odisea.reservas.ReservaSpaFragment
import com.example.odisea.reservas.ReservaRestauranteFragment // Importar el fragmento de reserva de restaurante
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private var isFavorite = false
    private val socioId = 1
    private var lugarActual: Lugar? = null

    companion object {
        private const val ARG_LUGAR = "lugar"

        fun newInstance(lugar: Lugar): DetailFragment {
            val fragment = DetailFragment()
            val args = Bundle()
            args.putParcelable(ARG_LUGAR, lugar)
            fragment.arguments = args
            Log.d("DetailFragment", "Lugar recibido: $lugar")
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lugarActual = arguments?.getParcelable(ARG_LUGAR)

        if (lugarActual == null) {
            requireActivity().onBackPressedDispatcher.onBackPressed()
            return
        }

        val lugar = lugarActual!!

        Glide.with(this)
            .load(lugar.imagenUrl?.trim())
            .centerCrop()
            .into(binding.placeMainPicture)

        binding.placeName.text = lugar.nombre
        binding.placeLocation.text = lugar.ubicacion ?: "Ubicación no disponible"
        binding.rating.text = lugar.valoracion?.let { "$it ⭐" } ?: "Sin calificación"
        binding.placeDescription.text = lugar.descripcion ?: "Sin descripción"

        configurarDetalles(lugar)
        verificarSiEsFavorito(lugar)

        // Configurar el botón de favoritos
        binding.fabFavorite.setOnClickListener {
            if (isFavorite) {
                eliminarFavorito(lugar)
            } else {
                agregarAFavoritos(lugar)
            }
        }

        // Configurar el botón de reserva
        binding.btnReservar.setOnClickListener {
            abrirReservaFragment(lugar)
        }
    }

    private fun abrirReservaFragment(lugar: Lugar) {
        val tipoEstablecimiento = lugar.tipoEstablecimiento?.trim()?.lowercase() ?: ""

        when (tipoEstablecimiento) {
            "hotel" -> {
                val reservaFragment = ReservaHotelFragment.newInstance(lugar)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, reservaFragment)
                    .addToBackStack(null)
                    .commit()
            }
            "pista" -> {
                val reservaFragment = ReservaPistaFragment.newInstance(lugar)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, reservaFragment)
                    .addToBackStack(null)
                    .commit()
            }
            "spa" -> {
                val reservaFragment = ReservaSpaFragment.newInstance(lugar)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, reservaFragment)
                    .addToBackStack(null)
                    .commit()
            }
            "restaurante" -> {
                val reservaFragment = ReservaRestauranteFragment.newInstance(lugar) // Nuevo caso para restaurante
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, reservaFragment)
                    .addToBackStack(null)
                    .commit()
            }
            else -> {
                mostrarMensajeError("El tipo de lugar no es compatible con reservas.")
            }
        }
    }

    private fun mostrarMensajeError(mensaje: String) {
        Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun verificarSiEsFavorito(lugar: Lugar) {
        RetrofitClient.apiService.obtenerFavoritos(socioId).enqueue(object : Callback<List<Lugar>> {
            override fun onResponse(call: Call<List<Lugar>>, response: Response<List<Lugar>>) {
                if (response.isSuccessful) {
                    val favoritos = response.body()?.map { it.id } ?: emptyList()
                    isFavorite = favoritos.contains(lugar.id)
                    actualizarIconoFavorito()
                }
            }

            override fun onFailure(call: Call<List<Lugar>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun actualizarIconoFavorito() {
        val iconRes = if (isFavorite) {
            R.drawable.baseline_bookmark_24
        } else {
            R.drawable.baseline_bookmark_border_24
        }
        binding.fabFavorite.setImageResource(iconRes)
    }

    private fun agregarAFavoritos(lugar: Lugar) {
        isFavorite = true
        actualizarIconoFavorito()

        RetrofitClient.apiService.agregarAFavoritos(
            socioId,
            lugar.tipoEstablecimiento ?: "",
            lugar.id
        ).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                // Confirmación opcional si quieres
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                t.printStackTrace()
                isFavorite = false
                actualizarIconoFavorito()
                mostrarMensajeError("Error al añadir a favoritos")
            }
        })
    }

    private fun eliminarFavorito(lugar: Lugar) {
        isFavorite = false
        actualizarIconoFavorito()

        RetrofitClient.apiService.eliminarFavorito(
            socioId,
            lugar.id
        ).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                // OK
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                t.printStackTrace()
                isFavorite = true
                actualizarIconoFavorito()
                mostrarMensajeError("Error al eliminar de favoritos")
            }
        })
    }

    private fun configurarDetalles(lugar: Lugar) {
        val detalles = lugar.detalles?.split("\n") ?: listOf("Detalles no disponibles")
        val tipoEstablecimiento = lugar.tipoEstablecimiento?.trim()?.lowercase() ?: ""

        val titulo = when (tipoEstablecimiento) {
            "hotel" -> "Normas del hotel"
            "spa" -> "Servicios del spa"
            "restaurante" -> "Detalles del restaurante"
            "pista" -> "Horarios de la pista"
            else -> "Detalles específicos"
        }

        binding.typeTitle.text = titulo
        binding.typeSpecificLayout.visibility = View.VISIBLE
        configurarRecyclerView(detalles)
    }

    private fun configurarRecyclerView(items: List<String>) {
        val adapter = GenericAdapter(items)
        binding.rvFeatures.adapter = adapter
        binding.rvFeatures.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}