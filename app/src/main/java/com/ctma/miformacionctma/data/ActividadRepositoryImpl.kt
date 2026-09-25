package com.ctma.miformacionctma.data

import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.domain.ActividadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ActividadRepositoryImpl : ActividadRepository {
    private val actividades = mutableListOf<ActividadFormativa>()

    override fun obtenerActividades(): Flow<List<ActividadFormativa>> {
        return flow { emit(actividades) }
    }

    override suspend fun obtenerActividadPorId(id: Long): ActividadFormativa? {
        return actividades.find { it.id == id }
    }

    override suspend fun agregarActividad(actividad: ActividadFormativa) {
        actividades.add(actividad)
    }
}