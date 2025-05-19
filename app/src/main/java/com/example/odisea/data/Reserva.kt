package com.example.odisea.data

data class Reserva(
    val id: Int,
    val socio_id: Int,
    val lugarNombre: String, // Nombre del lugar
    val fecha: String,        // Fecha de la reserva
    val tipoEstablecimiento: String // Tipo de establecimiento (hotel, pista, restaurante, spa)
)