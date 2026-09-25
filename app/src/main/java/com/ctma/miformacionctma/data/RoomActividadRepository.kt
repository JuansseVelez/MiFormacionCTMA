package com.ctma.miformacionctma.data

import com.ctma.miformacionctma.data.local.dao.FormacionDao
import com.ctma.miformacionctma.data.local.mappers.toDomain
import com.ctma.miformacionctma.data.local.mappers.toEntity
import com.ctma.miformacionctma.data.remote.RemoteActividadDataSource
import com.ctma.miformacionctma.data.remote.mappers.toEntityList
import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.domain.ActividadRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomActividadRepository(
    private val dao: FormacionDao,
    private val remoteDataSource: RemoteActividadDataSource? = null
) : ActividadRepository {
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

    suspend fun sincronizarConServidor(): Result<Unit> {
        val dataSource = remoteDataSource ?: return Result.success(Unit)
        return try {
            val resultadoRemoto = dataSource.obtenerActividadesRemotas()
            if (resultadoRemoto.isSuccess) {
                val dtos = resultadoRemoto.getOrDefault(emptyList())
                val entidades = dtos.toEntityList()
                for (entidad in entidades) {
                    dao.insertarActividad(entidad)
                }
                Result.success(Unit)
            } else {
                Result.failure(resultadoRemoto.exceptionOrNull() ?: Exception("Error desconocido en red"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
