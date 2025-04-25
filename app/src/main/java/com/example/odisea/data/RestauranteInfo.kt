package com.example.odisea.data

data class RestauranteInfo(
    val restaurante: Restaurante,
    val servicios: List<ServicioRestaurante>
)

data class Restaurante(
    val id: Int,
    val nombre: String,
    val ubicacion: String,
    val valoracion: Float,
    val descripcion: String
)

data class ServicioRestaurante(
    val id: Int,
    val restauranteId: Int,
    val tipoMenu: String,
    val tipoCocina: String,
    val ambiente: String,
    val terraza: Boolean
)