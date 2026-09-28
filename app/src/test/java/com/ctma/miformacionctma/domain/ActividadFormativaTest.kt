package com.ctma.miformacionctma.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ActividadFormativaTest {

    @Test
    fun crearActividadConValoresCompletos() {
        val actividad = ActividadFormativa(
            id = 1L,
            titulo = "Taller Kotlin",
            descripcion = "Aprender corrutinas",
            progreso = 75,
            diasRestantes = 5,
            prioridad = Prioridad.ALTA,
            fechaLimite = "2026-10-15"
        )

        assertEquals(1L, actividad.id)
        assertEquals("Taller Kotlin", actividad.titulo)
        assertEquals("Aprender corrutinas", actividad.descripcion)
        assertEquals(75, actividad.progreso)
        assertEquals(5, actividad.diasRestantes)
        assertEquals(Prioridad.ALTA, actividad.prioridad)
        assertEquals("2026-10-15", actividad.fechaLimite)
    }

    @Test
    fun verificarEnumPrioridades() {
        assertEquals("ALTA", Prioridad.ALTA.name)
        assertEquals("MEDIA", Prioridad.MEDIA.name)
        assertEquals("BAJA", Prioridad.BAJA.name)
        assertEquals(3, Prioridad.entries.size)
    }

    @Test
    fun crearActividadConDescripcionYFechaNulas() {
        val actividad = ActividadFormativa(
            id = 2L,
            titulo = "Actividad Básica",
            descripcion = null,
            progreso = 0,
            diasRestantes = 0,
            prioridad = Prioridad.BAJA,
            fechaLimite = null
        )

        assertNull(actividad.descripcion)
        assertNull(actividad.fechaLimite)
        assertEquals(0, actividad.progreso)
    }
}
