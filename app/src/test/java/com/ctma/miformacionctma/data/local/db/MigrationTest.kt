package com.ctma.miformacionctma.data.local.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MigrationTest {

    @Test
    fun migrate1To2ConservaDatosYAgregaColumnaCompletada() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val dbName = "migration-test-db"
        context.deleteDatabase(dbName)

        val helper = object : SQLiteOpenHelper(context, dbName, null, 1) {
            override fun onCreate(db: SQLiteDatabase) {
                db.execSQL("CREATE TABLE IF NOT EXISTS CompetenciaEntity (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `codigo` TEXT NOT NULL, `descripcion` TEXT NOT NULL)")
                db.execSQL("CREATE TABLE IF NOT EXISTS ActividadEntity (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `competenciaId` INTEGER, `titulo` TEXT NOT NULL, `descripcion` TEXT, `progreso` INTEGER NOT NULL, `fechaLimiteMillis` INTEGER NOT NULL, `prioridad` TEXT NOT NULL, FOREIGN KEY(`competenciaId`) REFERENCES `CompetenciaEntity`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_ActividadEntity_competenciaId` ON ActividadEntity (`competenciaId`)")
            }
            override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
        }

        val v1Db = helper.writableDatabase
        v1Db.execSQL("INSERT INTO ActividadEntity (id, competenciaId, titulo, descripcion, progreso, fechaLimiteMillis, prioridad) VALUES (1, NULL, 'Act v1', 'Desc v1', 10, 12345678, 'ALTA')")
        v1Db.close()

        val roomDb = Room.databaseBuilder(context, FormacionDatabase::class.java, dbName)
            .addMigrations(Migraciones.MIGRATION_1_2)
            .build()

        val db = roomDb.openHelper.writableDatabase
        val cursor = db.query("SELECT * FROM ActividadEntity WHERE id = 1")
        assert(cursor.moveToFirst())
        
        val completadaIndex = cursor.getColumnIndex("completada")
        assert(completadaIndex != -1)
        assert(cursor.getInt(completadaIndex) == 0)

        roomDb.close()
    }
}
