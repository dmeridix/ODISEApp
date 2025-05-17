package com.example.odisea.models

data class ReservaHotelCreate(
    val socio_id: Int,
    val hotel_id: Int, // Nuevo campo para el ID del hotel
    val habitacion_id: Int,
    val fecha_entrada: String, // Formato "YYYY-MM-DD"
    val fecha_salida: String  // Formato "YYYY-MM-DD"
)