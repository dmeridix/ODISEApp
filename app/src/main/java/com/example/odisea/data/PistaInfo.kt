package com.example.odisea.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class PistaInfo(
    val pista: Pista,
    val horarios: List<HorarioPista>
)
@Parcelize
data class Pista(
    val id: Int,
    val nombre: String,
    val ubicacion: String,
    val valoracion: Float,
    val descripcion: String
) : Parcelable

data class HorarioPista(
    val id: Int,
    val pistaId: Int,
    val horaInicio: String, // Formato: "HH:mm"
    val horaFin: String     // Formato: "HH:mm"
)
@Parcelize
data class ServicioPista(
    val id: Int,
    @SerializedName("pista_id") val pistaId: Int,
    @SerializedName("tipo_pista") val tipoPista: String?,
    @SerializedName("numero_personas") val numeroPersonas: Int?,
    val material: String?,
    @SerializedName("tiempo_disponible") val tiempoDisponible: String?
) : Parcelable