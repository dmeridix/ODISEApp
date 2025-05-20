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
    @SerializedName("img_url")
    val imagenUrl: String? = null,
    val detalles: String? = null,
    @SerializedName("tipo_establecimiento")
    val tipoEstablecimiento: String?,
    var esFavorito: Boolean = false
) : Parcelable
