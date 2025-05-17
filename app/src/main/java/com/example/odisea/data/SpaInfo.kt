package com.example.odisea.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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
@Parcelize
data class ServicioSpa(
    val id: Int,
    val spaId: Int,
    val tipo_servicio: String,
    val duracion: Int // Duración en minutos
) : Parcelable