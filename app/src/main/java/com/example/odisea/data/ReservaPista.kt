package com.example.odisea.data

data class ReservaPista(
    val id: Int,
    val socioId: Int,
    val pistaId: Int, // ID de la pista
    val servicioPistaId: Int, // Nuevo campo: ID del servicio de pista
    val fecha: String, // Fecha de reserva (formato "YYYY-MM-DD")
    val nombreLugar: String // Nom del hotel
)