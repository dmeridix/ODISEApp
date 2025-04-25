package com.example.odisea.data

data class PistaInfo(
    val pista: Pista,
    val horarios: List<HorarioPista>
)

data class Pista(
    val id: Int,
    val nombre: String,
    val ubicacion: String,
    val valoracion: Float,
    val descripcion: String
)

data class HorarioPista(
    val id: Int,
    val pistaId: Int,
    val horaInicio: String, // Formato: "HH:mm"
    val horaFin: String     // Formato: "HH:mm"
)