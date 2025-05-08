package com.example.odisea.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lugar(
    val id: Int,
    val nombre: String,
    val ubicacion: String?,
    val valoracion: Float?,
    val descripcion: String?,
    val tipoEstablecimiento: String,
    @SerializedName("img_url")
    val imagenUrl: String? = null
) : Parcelable