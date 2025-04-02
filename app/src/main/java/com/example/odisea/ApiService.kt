package com.example.odisea

import com.example.odisea.data.Lugar
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("lugares")
    fun getLugares(): Call<List<Lugar>>
}