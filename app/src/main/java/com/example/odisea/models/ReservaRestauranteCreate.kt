package com.example.odisea.models

data class ReservaRestauranteCreate(
    val socio_id: Int,
    val restaurante_id: Int,
    val fecha: String,
    val hora: String
)