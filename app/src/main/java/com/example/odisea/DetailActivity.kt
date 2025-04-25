package com.example.odisea

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.odisea.GenericAdapter
import com.example.odisea.databinding.ActivityDetailBinding
import com.example.odisea.data.Lugar

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar el objeto Lugar del Intent
        val lugar = intent.getParcelableExtra<Lugar>("lugar")

        if (lugar == null) {
            finish()
            return
        }

        // Mostrar datos generales
        Glide.with(this).load(lugar.imagenUrl).into(binding.placeMainPicture)
        binding.placeName.text = lugar.nombre
        binding.placeLocation.text = lugar.ubicacion
        binding.placeRating.text = lugar.calificacion.toString()
        binding.placeDescription.text = lugar.descripcion

        // Configurar características específicas según el tipo de establecimiento
        when (lugar.tipoEstablecimiento) {
            "hotel" -> configurarHotel(lugar)
            "spa" -> configurarSpa(lugar)
            "restaurante" -> configurarRestaurante(lugar)
            "pista" -> configurarPista(lugar)
            else -> binding.typeSpecificLayout.visibility = View.GONE
        }
    }

    private fun configurarHotel(lugar: Lugar) {
        binding.typeSpecificLayout.visibility = View.VISIBLE
        binding.typeTitle.text = "Normas del hotel"

        val normas = listOf("No fumar", "Check-in a las 15:00", "Mascotas permitidas")
        val adapter = GenericAdapter(normas)
        binding.rvFeatures.adapter = adapter
        binding.rvFeatures.layoutManager = LinearLayoutManager(this)
    }

    private fun configurarSpa(lugar: Lugar) {
        binding.typeSpecificLayout.visibility = View.VISIBLE
        binding.typeTitle.text = "Servicios del spa"

        val servicios = listOf("Masajes", "Tratamientos faciales", "Sauna")
        val adapter = GenericAdapter(servicios)
        binding.rvFeatures.adapter = adapter
        binding.rvFeatures.layoutManager = LinearLayoutManager(this)
    }

    private fun configurarRestaurante(lugar: Lugar) {
        binding.typeSpecificLayout.visibility = View.VISIBLE
        binding.typeTitle.text = "Detalles del restaurante"

        val detalles = listOf("Cocina italiana", "Abierto 24/7", "Ambiente romántico")
        val adapter = GenericAdapter(detalles)
        binding.rvFeatures.adapter = adapter
        binding.rvFeatures.layoutManager = LinearLayoutManager(this)
    }

    private fun configurarPista(lugar: Lugar) {
        binding.typeSpecificLayout.visibility = View.VISIBLE
        binding.typeTitle.text = "Horarios de la pista"

        val horarios = listOf("08:00 - 12:00", "14:00 - 18:00", "20:00 - 23:00")
        val adapter = GenericAdapter(horarios)
        binding.rvFeatures.adapter = adapter
        binding.rvFeatures.layoutManager = LinearLayoutManager(this)
    }
}