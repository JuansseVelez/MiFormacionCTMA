package com.ctma.miformacionctma.data.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "EvidenciaEntity",
    foreignKeys = [
        ForeignKey(
            entity = ActividadEntity::class,
            parentColumns = ["id"],
            childColumns = ["actividadId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["actividadId"])]
)
data class EvidenciaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val actividadId: Long,
    val uri: String,
    val tipoMime: String,
    val tamanoBytes: Long,
    val estado: String = "LOCAL", // LOCAL, SUBIENDO, SINCRONIZADA, FALLIDA
    val fechaCreacionMillis: Long = System.currentTimeMillis()
)
