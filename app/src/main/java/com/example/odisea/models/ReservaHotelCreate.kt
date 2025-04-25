package com.example.odisea.models

data class ReservaHotelCreate(
    val socio_id: Int,
    val habitacion_id: Int,
    val fecha_entrada: String,
    val fecha_salida: String
)