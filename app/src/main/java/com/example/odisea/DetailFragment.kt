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

        // Recuperar el objeto Lugar del Bundle
        val lugar = arguments?.getParcelable<Lugar>(ARG_LUGAR)

        if (lugar == null) {
            requireActivity().onBackPressedDispatcher.onBackPressed()
            return
        }

        // Mostramos la imagen y los datos generales
        Glide.with(this)
            .load(lugar.imagenUrl)
            .centerCrop()
            .into(binding.placeMainPicture)

        binding.placeName.text = lugar.nombre
        binding.placeLocation.text = lugar.ubicacion ?: "Ubicación no disponible"
        binding.rating.text = if (lugar.valoracion != null) "${lugar.valoracion} ⭐" else "Sin calificación"
        binding.placeDescription.text = lugar.descripcion ?: "Sin descripción"

        // Mostrar los detalles específicos en el RecyclerView
        configurarDetalles(lugar)
    }

    /**
     * Configura los detalles específicos para cualquier tipo de lugar.
     */
    private fun configurarDetalles(lugar: Lugar) {
        // Dividir los detalles en una lista de líneas usando '\n'
        val detalles = lugar.detalles?.split("\n") ?: listOf("Detalles no disponibles")

        // Obtener el tipo de establecimiento y asignar un título dinámico
        val tipoEstablecimiento = lugar.tipoEstablecimiento ?: "Lugar"
        Log.d("DetallesActivity", "TipoEstablecimiento recibido: '${tipoEstablecimiento}'")
        val titulo = when (tipoEstablecimiento.lowercase()) {
            "hotel" -> "Normas del hotel"
            "spa" -> "Servicios del spa"
            "restaurante" -> "Detalles del restaurante"
            "pista" -> "Horarios de la pista"
            else -> "Detalles específicos"
        }

        // Configurar el título y mostrar el layout de detalles
        binding.typeTitle.text = titulo
        binding.typeSpecificLayout.visibility = View.VISIBLE

        // Configurar el RecyclerView con los detalles
        configurarRecyclerView(detalles)
    }

    /**
     * Configura el RecyclerView con los datos proporcionados.
     */
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