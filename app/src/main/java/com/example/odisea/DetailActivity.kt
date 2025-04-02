package com.example.odisea

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.odisea.GenericAdapter
import com.example.odisea.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recuperar datos del Intent
        val nombre = intent.getStringExtra("nombre") ?: "Sin nombre"
        val ubicacion = intent.getStringExtra("ubicacion") ?: "Ubicación desconocida"
        val calificacion = intent.getFloatExtra("calificacion", 0f)
        val descripcion = intent.getStringExtra("descripcion") ?: "Sin descripción"
        val imagenUrl = intent.getStringExtra("imagenUrl") ?: "https://via.placeholder.com/150"
        val tipoEstablecimiento = intent.getStringExtra("tipoEstablecimiento") ?: "desconocido"

        // Mostrar datos generales
        Glide.with(this).load(imagenUrl).into(binding.hotelMainPicture)
        binding.hotelName.text = nombre
        binding.hotelLocation.text = ubicacion
        binding.hotelRating.text = calificacion.toString()
        binding.hotelDescription.text = descripcion

        // Configurar características específicas según el tipo de establecimiento
        when (tipoEstablecimiento) {
            "hotel" -> configurarHotel()
            "spa" -> configurarSpa()
            "restaurante" -> configurarRestaurante()
            "pista" -> configurarPista()
            else -> binding.typeSpecificLayout.visibility = View.GONE // Ocultar si no hay tipo válido
        }
    }

    private fun configurarHotel() {
        binding.typeSpecificLayout.visibility = View.VISIBLE
        binding.typeTitle.text = "Normas del hotel"

        val normas = listOf("No fumar", "Check-in a las 15:00", "Mascotas permitidas")
        val adapter = GenericAdapter(normas)
        binding.rvFeatures.adapter = adapter
        binding.rvFeatures.layoutManager = LinearLayoutManager(this)
    }

    private fun configurarSpa() {
        binding.typeSpecificLayout.visibility = View.VISIBLE
        binding.typeTitle.text = "Servicios del spa"

        val servicios = listOf("Masajes", "Tratamientos faciales", "Sauna")
        val adapter = GenericAdapter(servicios)
        binding.rvFeatures.adapter = adapter
        binding.rvFeatures.layoutManager = LinearLayoutManager(this)
    }

    private fun configurarRestaurante() {
        binding.typeSpecificLayout.visibility = View.VISIBLE
        binding.typeTitle.text = "Detalles del restaurante"

        val detalles = listOf("Cocina italiana", "Abierto 24/7", "Ambiente romántico")
        val adapter = GenericAdapter(detalles)
        binding.rvFeatures.adapter = adapter
        binding.rvFeatures.layoutManager = LinearLayoutManager(this)
    }

    private fun configurarPista() {
        binding.typeSpecificLayout.visibility = View.VISIBLE
        binding.typeTitle.text = "Horarios de la pista"

        val horarios = listOf("08:00 - 12:00", "14:00 - 18:00", "20:00 - 23:00")
        val adapter = GenericAdapter(horarios)
        binding.rvFeatures.adapter = adapter
        binding.rvFeatures.layoutManager = LinearLayoutManager(this)
    }
}