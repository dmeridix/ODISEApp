package com.example.odisea.data

data class ReservaHotel(
    val id: Int,
    val socioId: Int,
    val hotelId: Int, // Nuevo campo: ID del hotel
    val habitacionId: Int,
    val fechaEntrada: String, // Fecha de entrada (formato "YYYY-MM-DD")
    val fechaSalida: String  // Fecha de salida (formato "YYYY-MM-DD")
)

