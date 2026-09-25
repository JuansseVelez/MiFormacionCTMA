package com.ctma.miformacionctma.data

import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.domain.Prioridad
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ActividadRepositoryImplTest {

    private lateinit var repository: ActividadRepositoryImpl

    @Before
    fun setup() {
        repository = ActividadRepositoryImpl()
    }

    @Test
    fun obtenerActividadesRetornaListaInicialVacia() = runBlocking {
        val lista = repository.obtenerActividades().first()
        assertEquals(0, lista.size)
    }

    @Test
    fun obtenerActividadPorIdDevuelveActividadExistente() = runBlocking {
        val actividad = ActividadFormativa(
            id = 1L,
            titulo = "Taller 1: Kotlin y Modelado de Datos",
            descripcion = "Desc",
            progreso = 50,
            diasRestantes = 5,
            prioridad = Prioridad.ALTA
        )
        repository.agregarActividad(actividad)

        val recuperada = repository.obtenerActividadPorId(1L)
        assertNotNull(recuperada)
        assertEquals("Taller 1: Kotlin y Modelado de Datos", recuperada?.titulo)
    }

    @Test
    fun obtenerActividadPorIdDevuelveNullParaIdInexistente() = runBlocking {
        val actividad = repository.obtenerActividadPorId(999L)
        assertNull(actividad)
    }

    @Test
    fun agregarActividadInsertaNuevaActividadYActualizaFlujo() = runBlocking {
        val nueva = ActividadFormativa(
            id = 4L,
            titulo = "Nueva Actividad Test",
            descripcion = "Descripción Test",
            progreso = 0,
            diasRestantes = 7,
            prioridad = Prioridad.ALTA
        )

        repository.agregarActividad(nueva)

        val listaActualizada = repository.obtenerActividades().first()
        assertEquals(1, listaActualizada.size)

        val recuperada = repository.obtenerActividadPorId(4L)
        assertNotNull(recuperada)
        assertEquals("Nueva Actividad Test", recuperada?.titulo)
    }
}
