package com.example.odisea.data

data class Reserva(
    val idLugar: Int,
    val tipo: String,
    val nombreLugar: String,
    val fecha: String = "",
    val hora: String = ""  // Por defecto vacío para evitar errores
)
