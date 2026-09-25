package com.ctma.miformacionctma.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ctma.miformacionctma.data.local.db.FormacionDatabase
import com.ctma.miformacionctma.data.local.entities.ActividadEntity
import com.ctma.miformacionctma.domain.Prioridad
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FormacionDaoTest {

    private lateinit var database: FormacionDatabase
    private lateinit var dao: FormacionDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FormacionDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.formacionDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertarYObtenerActividad() = runBlocking {
        val actividad = ActividadEntity(
            id = 1L,
            competenciaId = null,
            titulo = "Test Actividad",
            descripcion = "Descripción",
            progreso = 50,
            fechaLimiteMillis = System.currentTimeMillis(),
            prioridad = Prioridad.ALTA.name,
            completada = false
        )
        
        dao.insertarActividad(actividad)
        
        val resultado = dao.obtenerTodasLasActividades().first()
        assertEquals(1, resultado.size)
        assertEquals("Test Actividad", resultado[0].actividad.titulo)
    }

    @Test
    fun obtenerActividadPorId() = runBlocking {
        val actividad = ActividadEntity(
            id = 10L,
            competenciaId = null,
            titulo = "Busqueda por ID",
            descripcion = null,
            progreso = 0,
            fechaLimiteMillis = System.currentTimeMillis(),
            prioridad = Prioridad.BAJA.name
        )
        dao.insertarActividad(actividad)
        
        val encontrado = dao.obtenerActividadPorId(10L)
        assertNotNull(encontrado)
        assertEquals("Busqueda por ID", encontrado?.actividad?.titulo)
    }
}
