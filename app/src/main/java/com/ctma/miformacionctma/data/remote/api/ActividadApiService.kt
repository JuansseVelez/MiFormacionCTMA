package com.ctma.miformacionctma.data.remote.api

import com.ctma.miformacionctma.data.remote.dto.ActividadDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ActividadApiService {

    @GET("actividades")
    suspend fun obtenerActividades(): Response<List<ActividadDto>>

    @POST("actividades")
    suspend fun crearActividad(@Body actividad: ActividadDto): Response<ActividadDto>
}
