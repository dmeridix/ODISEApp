package com.example.odisea.data

data class ReservaResumen(
    val id: Int,
    val tipo: String,
    val nombre_lugar: String,
    val fecha_entrada: String? = null,
    val fecha_salida: String? = null,
    val fecha: String? = null,
    val hora: String? = null,
    val habitacion_id: Int? = null
)