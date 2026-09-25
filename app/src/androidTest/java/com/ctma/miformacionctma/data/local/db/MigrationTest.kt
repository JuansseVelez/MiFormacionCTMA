package com.ctma.miformacionctma.data.local.db

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    private val TEST_DB = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        FormacionDatabase::class.java
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        // Crear base de datos con versión 1
        var db = helper.createDatabase(TEST_DB, 1)

        // Insertar datos si fuera necesario (validar esquema v1)
        db.execSQL("INSERT INTO CompetenciaEntity (id, codigo, descripcion) VALUES (1, 'C1', 'Desc')")
        // En v1 ActividadEntity NO tiene 'completada'
        db.execSQL("INSERT INTO ActividadEntity (id, competenciaId, titulo, descripcion, progreso, fechaLimiteMillis, prioridad) " +
                "VALUES (1, 1, 'Act v1', 'Desc v1', 10, 12345678, 'ALTA')")
        
        db.close()

        // Migrar a versión 2
        db = helper.runMigrationsAndValidate(TEST_DB, 2, true, Migraciones.MIGRATION_1_2)

        // Verificar que la columna 'completada' existe y tiene valor por defecto 0 (false)
        val cursor = db.query("SELECT * FROM ActividadEntity WHERE id = 1")
        assert(cursor.moveToFirst())
        val completadaIndex = cursor.getColumnIndex("completada")
        assert(completadaIndex != -1)
        assert(cursor.getInt(completadaIndex) == 0)
    }
}
