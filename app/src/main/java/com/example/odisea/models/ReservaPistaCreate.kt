package com.example.odisea.models

data class ReservaPistaCreate(
    val socio_id: Int,
    val pista_id: Int, // Nuevo campo para el ID de la pista
    val servicio_pista_id: Int,
    val fecha: String // Formato "YYYY-MM-DD"
)