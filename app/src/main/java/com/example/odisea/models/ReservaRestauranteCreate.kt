package com.example.odisea.models

data class ReservaRestauranteCreate(
    val socio_id: Int,
    val restaurante_id: Int, // Nuevo campo para el ID del restaurante
    val servicio_restaurante_id: Int,
    val fecha: String, // Formato "YYYY-MM-DD"
    val hora: String  // Formato "HH:mm"
)
