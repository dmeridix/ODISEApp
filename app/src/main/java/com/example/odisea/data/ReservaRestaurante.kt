package com.example.odisea.data

data class ReservaRestaurante(
    val id: Int,
    val socioId: Int,
    val restauranteId: Int, // ID del restaurante
    val servicioRestauranteId: Int, // Nuevo campo: ID del servicio de restaurante
    val fecha: String, // Fecha de reserva (formato "YYYY-MM-DD")
    val hora: String,  // Hora de reserva (formato "HH:mm")
    val nombreLugar: String // Nom del hotel
)

