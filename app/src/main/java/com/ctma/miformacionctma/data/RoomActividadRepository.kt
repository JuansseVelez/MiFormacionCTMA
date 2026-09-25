package com.ctma.miformacionctma.data

import com.ctma.miformacionctma.data.local.dao.FormacionDao
import com.ctma.miformacionctma.data.local.mappers.toDomain
import com.ctma.miformacionctma.data.local.mappers.toEntity
import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.domain.ActividadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomActividadRepository(private val dao: FormacionDao) : ActividadRepository {
    override fun obtenerActividades(): Flow<List<ActividadFormativa>> {
        return dao.obtenerTodasLasActividades().map { lista ->
            lista.map { it.toDomain() }
        }
    }

    override suspend fun obtenerActividadPorId(id: Long): ActividadFormativa? {
        return dao.obtenerActividadPorId(id)?.toDomain()
    }

    override suspend fun agregarActividad(actividad: ActividadFormativa) {
        dao.insertarActividad(actividad.toEntity())
    }
}
