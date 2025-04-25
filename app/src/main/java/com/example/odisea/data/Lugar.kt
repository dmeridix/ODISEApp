package com.example.odisea.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lugar(
    val id: Int,
    val nombre: String,
    val ubicacion: String?,
    val valoracion: Float?,
    val descripcion: String?,
    val tipo_establecimiento: String
) : Parcelable