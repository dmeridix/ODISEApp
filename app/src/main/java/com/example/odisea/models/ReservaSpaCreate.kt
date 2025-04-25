package com.example.odisea.models

data class ReservaSpaCreate(
    val socio_id: Int,
    val spa_id: Int,
    val fecha: String,
    val hora: String
)