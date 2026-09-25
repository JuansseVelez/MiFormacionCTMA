package com.ctma.miformacionctma.data.local.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migraciones {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE ActividadEntity ADD COLUMN completada INTEGER NOT NULL DEFAULT 0")
        }
    }
}
