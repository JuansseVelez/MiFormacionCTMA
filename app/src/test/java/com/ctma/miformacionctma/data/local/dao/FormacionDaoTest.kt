package com.ctma.miformacionctma.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ctma.miformacionctma.data.local.db.FormacionDatabase
import com.ctma.miformacionctma.data.local.entities.ActividadEntity
import com.ctma.miformacionctma.data.local.entities.CompetenciaEntity
import com.ctma.miformacionctma.domain.Prioridad
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
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
    fun insertarYObtenerTodasLasActividades() = runBlocking {
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
    fun obtenerActividadPorIdExistente() = runBlocking {
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

    @Test
    fun obtenerActividadPorIdInexistenteRetornaNull() = runBlocking {
        val encontrado = dao.obtenerActividadPorId(999L)
        assertNull(encontrado)
    }

    @Test
    fun buscarActividadesPorTextoCoincidente() = runBlocking {
        val act1 = ActividadEntity(1L, null, "Aprender Kotlin", "Desc", 0, System.currentTimeMillis(), "ALTA")
        val act2 = ActividadEntity(2L, null, "Aprender Java", "Desc", 0, System.currentTimeMillis(), "BAJA")
        dao.insertarActividad(act1)
        dao.insertarActividad(act2)

        val resultados = dao.buscarActividadesPorTexto("Kotlin").first()
        assertEquals(1, resultados.size)
        assertEquals("Aprender Kotlin", resultados[0].actividad.titulo)
    }

    @Test
    fun buscarActividadesPorCompetencia() = runBlocking {
        val comp = CompetenciaEntity(1L, "COMP1", "Desarrollo Android")
        dao.insertarCompetencia(comp)

        val act = ActividadEntity(1L, 1L, "Taller Android", "Desc", 100, System.currentTimeMillis(), "ALTA")
        dao.insertarActividad(act)

        val resultados = dao.buscarActividadesPorCompetencia(1L).first()
        assertEquals(1, resultados.size)
        assertEquals("Taller Android", resultados[0].actividad.titulo)
        assertEquals("COMP1", resultados[0].competencia?.codigo)
    }

    @Test
    fun eliminarActividadLaRemueveDeLaBaseDeDatos() = runBlocking {
        val act = ActividadEntity(5L, null, "Para Eliminar", "Desc", 0, System.currentTimeMillis(), "BAJA")
        dao.insertarActividad(act)

        val antes = dao.obtenerTodasLasActividades().first()
        assertEquals(1, antes.size)

        dao.eliminarActividad(act)

        val despues = dao.obtenerTodasLasActividades().first()
        assertEquals(0, despues.size)
    }
}
