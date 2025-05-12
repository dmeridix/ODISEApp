package com.example.odisea

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.odisea.databinding.FragmentDetailBinding
import com.example.odisea.data.Lugar

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

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
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lugar = arguments?.getParcelable<Lugar>(ARG_LUGAR)

        if (lugar == null) {
            requireActivity().onBackPressedDispatcher.onBackPressed()
            return
        }

        Glide.with(this)
            .load(lugar.imagenUrl?.trim())
            .centerCrop()
            .into(binding.placeMainPicture)

        binding.placeName.text = lugar.nombre
        binding.placeLocation.text = lugar.ubicacion ?: "Ubicación no disponible"
        binding.rating.text = if (lugar.valoracion != null) "${lugar.valoracion} ⭐" else "Sin calificación"
        binding.placeDescription.text = lugar.descripcion ?: "Sin descripción"

        configurarDetalles(lugar)
    }

    private fun configurarDetalles(lugar: Lugar) {
        val detalles = lugar.detalles?.split("\n") ?: listOf("Detalles no disponibles")

        val tipoEstablecimientoRaw = lugar.tipoEstablecimiento ?: ""
        val tipoEstablecimiento = tipoEstablecimientoRaw.trim().lowercase()
        Log.d("DetailFragment", "TipoEstablecimiento recibido: '$tipoEstablecimientoRaw' (normalizado: '$tipoEstablecimiento')")

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
