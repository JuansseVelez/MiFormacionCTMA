package com.ctma.miformacionctma.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "ActividadEntity",
    foreignKeys = [
        ForeignKey(
            entity = CompetenciaEntity::class,
            parentColumns = ["id"],
            childColumns = ["competenciaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["competenciaId"])]
)
data class ActividadEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val competenciaId: Long?,
    val titulo: String,
    val descripcion: String?,
    val progreso: Int,
    val fechaLimiteMillis: Long,
    val prioridad: String,
    val completada: Boolean = false // Requisito de la migración de la versión 2
)
