package com.example.odisea.data

data class SpaInfo(
    val spa: Spa,
    val servicios: List<ServicioSpa>
)

data class Spa(
    val id: Int,
    val nombre: String,
    val ubicacion: String,
    val valoracion: Float,
    val descripcion: String
)

data class ServicioSpa(
    val id: Int,
    val spaId: Int,
    val tipoServicio: String,
    val duracion: Int // Duración en minutos
)