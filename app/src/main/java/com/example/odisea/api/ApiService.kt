package com.example.odisea.api

import com.example.odisea.data.*
import com.example.odisea.models.*

import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    // 1. Obtener lista de lugares populares
    @GET("lugares")
    fun getLugares(): Call<List<Lugar>>

    // 2. Obtener detalles de un lugar específico
    @GET("lugares/{id}")
    fun getLugarDetalles(@Path("id") id: String): Call<Lugar>

    // 3. Agregar un lugar a favoritos
    @POST("favoritos/")
    fun agregarAFavoritos(
        @Query("socio_id") socioId: Int,
        @Query("tipo_establecimiento") tipoEstablecimiento: String,
        @Query("referencia_id") referenciaId: Int
    ): Call<Map<String, Any>>

    // Eliminar un lugar de favoritos
    @DELETE("favoritos/{socio_id}/{referencia_id}")
    fun eliminarFavorito(
        @Path("socio_id") socioId: Int,
        @Path("referencia_id") referenciaId: Int
    ): Call<Map<String, Any>>

    // 4. Obtener lista de favoritos de un socio
    @GET("favoritos/{socio_id}")
    fun obtenerFavoritos(@Path("socio_id") socioId: Int): Call<List<Lugar>>

    // 5. Obtener reservas de hotel de un socio
    @GET("reservas_hotel/{socio_id}")
    fun obtenerReservasHotel(@Path("socio_id") socioId: Int): Call<List<ReservaHotel>>

    // 6. Obtener reservas de pista de un socio
    @GET("reservas_pista/{socio_id}")
    fun obtenerReservasPista(@Path("socio_id") socioId: Int): Call<List<ReservaPista>>

    // 7. Obtener reservas de restaurante de un socio
    @GET("reservas_restaurante/{socio_id}")
    fun obtenerReservasRestaurante(@Path("socio_id") socioId: Int): Call<List<ReservaRestaurante>>

    // 8. Obtener reservas de spa de un socio
    @GET("reservas_spa/{socio_id}")
    fun obtenerReservasSpa(@Path("socio_id") socioId: Int): Call<List<ReservaSpa>>

    // Obtener habitaciones de un hotel (usa Habitacion)
    @GET("hotel/{hotel_id}/habitaciones")
    fun obtenerHabitacionesHotel(@Path("hotel_id") hotelId: Int): Call<List<Habitacion>>

    // Obtener servicios de una pista
    @GET("pista/{pista_id}/servicios")
    fun obtenerServiciosPista(@Path("pista_id") pistaId: Int): Call<List<ServicioPista>>

    // Obtener servicios de un restaurante
    @GET("restaurante/{restaurante_id}/servicios")
    fun obtenerServiciosRestaurante(@Path("restaurante_id") restauranteId: Int): Call<List<ServicioRestaurante>>

    // Obtener servicios de un spa
    @GET("spa/{spa_id}/servicios")
    fun obtenerServiciosSpa(@Path("spa_id") spaId: Int): Call<List<ServicioSpa>>

    // 13. Buscar lugares por nombre y categoría
    @GET("buscar")
    fun buscarLugares(
        @Query("query") query: String,
        @Query("categoria") categoria: String
    ): Call<List<Lugar>>

    // 14. Crear reserva de hotel
    @POST("reservas_hotel/")
    fun crearReservaHotel(@Body reserva: ReservaHotelCreate): Call<Void>

    // 15. Crear reserva de pista
    @POST("reservas_pista/")
    fun crearReservaPista(@Body reserva: ReservaPistaCreate): Call<Void>

    // 16. Crear reserva de restaurante
    @POST("reservas_restaurante/")
    fun crearReservaRestaurante(@Body reserva: ReservaRestauranteCreate): Call<Void>

    // 17. Crear reserva de spa
    @POST("reservas_spa/")
    fun crearReservaSpa(@Body reserva: ReservaSpaCreate): Call<Void>

    // 18. Obtener el perfil de un socio
    @GET("perfil/{socio_id}")
    fun obtenerPerfilSocio(@Path("socio_id") socioId: Int): Call<UserProfile>

    // 19. Eliminar un socio
    @DELETE("socios/{socio_id}")
    fun eliminarSocio(@Path("socio_id") socioId: Int): Call<Void>

    // 20. Validar credenciales de inicio de sesión
    @POST("buscar_socio/")
    fun validarCredenciales(
        @Query("email") email: String,
        @Query("contrasenya") contrasenya: String
    ): Call<Map<String, Any>>
}