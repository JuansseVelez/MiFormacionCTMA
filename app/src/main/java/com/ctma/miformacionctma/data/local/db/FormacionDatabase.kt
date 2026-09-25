package com.ctma.miformacionctma.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ctma.miformacionctma.data.local.dao.FormacionDao
import com.ctma.miformacionctma.data.local.entities.ActividadEntity
import com.ctma.miformacionctma.data.local.entities.CompetenciaEntity

@Database(
    entities = [ActividadEntity::class, CompetenciaEntity::class],
    version = 2,
    exportSchema = true
)
abstract class FormacionDatabase : RoomDatabase() {
    abstract fun formacionDao(): FormacionDao
}
