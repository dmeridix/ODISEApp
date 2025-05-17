package com.example.odisea.models

data class ReservaSpaCreate(
    val socio_id: Int,
    val spa_id: Int, // Nuevo campo para el ID del spa
    val servicio_spa_id: Int,
    val fecha: String, // Formato "YYYY-MM-DD"
    val hora: String  // Formato "HH:mm"
)