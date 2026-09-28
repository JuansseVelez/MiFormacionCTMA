package com.ctma.miformacionctma.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CompetenciaEntity")
data class CompetenciaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val codigo: String,
    val descripcion: String
)
