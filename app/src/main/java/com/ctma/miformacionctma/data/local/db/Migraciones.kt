package com.ctma.miformacionctma.data.local.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migraciones {
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE ActividadEntity ADD COLUMN completada INTEGER NOT NULL DEFAULT 0")
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("CREATE TABLE IF NOT EXISTS `EvidenciaEntity` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `actividadId` INTEGER NOT NULL, `uri` TEXT NOT NULL, `tipoMime` TEXT NOT NULL, `tamanoBytes` INTEGER NOT NULL, `estado` TEXT NOT NULL DEFAULT 'LOCAL', `fechaCreacionMillis` INTEGER NOT NULL, FOREIGN KEY(`actividadId`) REFERENCES `ActividadEntity`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
            db.execSQL("CREATE INDEX IF NOT EXISTS `index_EvidenciaEntity_actividadId` ON `EvidenciaEntity` (`actividadId`)")
        }
    }
}
