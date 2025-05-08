package com.example.odisea.data

data class UserProfile(
    val id: Int, // ID único del socio
    val nombre: String, // Nombre completo del socio
    val telefono: String?, // Teléfono del socio (puede ser nulo)
    val email: String, // Email del socio
    val dni: String, // Documento Nacional de Identidad del socio
    val contrasenya: String?, // Contraseña del socio (puede ser nula)
    val cuotaSocio: Double // Cuota de membresía del socio
)
