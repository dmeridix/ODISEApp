package com.example.odisea.data
// Clase principal que representa la información detallada de un hotel
data class HotelInfo(
    val hotel: Hotel,
    val habitaciones: List<Habitacion>,
    val servicios: List<ServicioHotel>
)

// Clase que representa un hotel
data class Hotel(
    val id: Int,
    val nombre: String,
    val ubicacion: String,
    val valoracion: Float,
    val descripcion: String
)

// Clase que representa una habitación del hotel
data class Habitacion(
    val id: Int,
    val hotelId: Int,
    val tipo: String,
    val disponibilidad: Boolean
)

// Clase que representa los servicios ofrecidos por el hotel
data class ServicioHotel(
    val id: Int,
    val hotelId: Int,
    val tipoServicio: String,
    val descripcion: String
)