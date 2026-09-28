package com.ctma.miformacionctma.data.local.dao

import androidx.room.*
import com.ctma.miformacionctma.data.local.entities.ActividadEntity
import com.ctma.miformacionctma.data.local.entities.CompetenciaEntity
import com.ctma.miformacionctma.data.local.entities.ActividadConCompetencia
import kotlinx.coroutines.flow.Flow

@Dao
interface FormacionDao {
    @Transaction
    @Query("SELECT * FROM ActividadEntity ORDER BY id DESC")
    fun obtenerTodasLasActividades(): Flow<List<ActividadConCompetencia>>

    @Transaction
    @Query("SELECT * FROM ActividadEntity WHERE id = :id LIMIT 1")
    suspend fun obtenerActividadPorId(id: Long): ActividadConCompetencia?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarActividad(actividad: ActividadEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCompetencia(competencia: CompetenciaEntity)

    @Delete
    suspend fun eliminarActividad(actividad: ActividadEntity)

    @Transaction
    @Query("SELECT * FROM ActividadEntity WHERE titulo LIKE '%' || :query || '%' ORDER BY id DESC")
    fun buscarActividadesPorTexto(query: String): Flow<List<ActividadConCompetencia>>

    @Transaction
    @Query("SELECT * FROM ActividadEntity WHERE competenciaId = :competenciaId ORDER BY id DESC")
    fun buscarActividadesPorCompetencia(competenciaId: Long): Flow<List<ActividadConCompetencia>>
}
