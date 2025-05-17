package com.example.odisea.data

data class ReservaSpa(
    val id: Int,
    val socioId: Int,
    val spaId: Int, // ID del spa
    val servicioSpaId: Int, // Nuevo campo: ID del servicio de spa
    val fecha: String, // Fecha de reserva (formato "YYYY-MM-DD")
    val hora: String  // Hora de reserva (formato "HH:mm")
)

