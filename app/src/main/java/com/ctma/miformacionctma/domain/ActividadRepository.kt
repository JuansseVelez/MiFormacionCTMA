package com.ctma.miformacionctma.domain

import kotlinx.coroutines.flow.Flow

interface ActividadRepository {
    fun obtenerActividades(): Flow<List<ActividadFormativa>>
    suspend fun obtenerActividadPorId(id: Long): ActividadFormativa?
    suspend fun agregarActividad(actividad: ActividadFormativa)
}