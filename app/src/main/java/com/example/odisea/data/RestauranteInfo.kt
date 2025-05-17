package com.example.odisea.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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

@Parcelize
data class ServicioRestaurante(
    val id: Int,
    val restauranteId: Int,
    val tipo_menu: String,
    val tipo_cocina: String,
    val ambiente: String,
    val terraza: Boolean
) : Parcelable