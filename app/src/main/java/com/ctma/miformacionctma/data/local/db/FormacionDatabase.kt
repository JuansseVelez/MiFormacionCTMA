package com.ctma.miformacionctma.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ctma.miformacionctma.data.local.dao.FormacionDao
import com.ctma.miformacionctma.data.local.entities.ActividadEntity
import com.ctma.miformacionctma.data.local.entities.CompetenciaEntity
import com.ctma.miformacionctma.data.local.entities.EvidenciaEntity

@Database(
    entities = [ActividadEntity::class, CompetenciaEntity::class, EvidenciaEntity::class],
    version = 3,
    exportSchema = true
)
abstract class FormacionDatabase : RoomDatabase() {
    abstract fun formacionDao(): FormacionDao
}
