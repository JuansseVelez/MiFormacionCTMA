package com.ctma.miformacionctma.data.local.mappers

import com.ctma.miformacionctma.data.local.entities.ActividadConCompetencia
import com.ctma.miformacionctma.data.local.entities.ActividadEntity
import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.domain.Prioridad
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.TimeUnit

class MapeadoresDataTest {

    @Test
    fun toDomainMapeaPropiedadesCorrectamente() {
        val fechaFutura = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(10)
        val entidad = ActividadEntity(
            id = 5L,
            competenciaId = null,
            titulo = "Mapeador Test",
            descripcion = "Prueba de mapeo",
            progreso = 40,
            fechaLimiteMillis = fechaFutura,
            prioridad = "ALTA",
            completada = false
        )
        val relacion = ActividadConCompetencia(actividad = entidad, competencia = null)

        val dominio = relacion.toDomain()

        assertEquals(5L, dominio.id)
        assertEquals("Mapeador Test", dominio.titulo)
        assertEquals("Prueba de mapeo", dominio.descripcion)
        assertEquals(40, dominio.progreso)
        assertEquals(Prioridad.ALTA, dominio.prioridad)
        assertNotNull(dominio.fechaLimite)
    }

    @Test
    fun toDomainManejaPrioridadInvalidaFallbackABaja() {
        val entidad = ActividadEntity(
            id = 1L,
            competenciaId = null,
            titulo = "Prioridad Inválida",
            descripcion = null,
            progreso = 0,
            fechaLimiteMillis = System.currentTimeMillis(),
            prioridad = "INVALID_PRIORITY",
            completada = false
        )
        val relacion = ActividadConCompetencia(actividad = entidad, competencia = null)

        val dominio = relacion.toDomain()

        assertEquals(Prioridad.BAJA, dominio.prioridad)
    }

    @Test
    fun toDomainCalculaDiasRestantes() {
        val diezDiasMillis = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(10) + 5000
        val entidad = ActividadEntity(
            id = 2L,
            competenciaId = null,
            titulo = "Dias Test",
            descripcion = null,
            progreso = 10,
            fechaLimiteMillis = diezDiasMillis,
            prioridad = "MEDIA",
            completada = false
        )
        val relacion = ActividadConCompetencia(actividad = entidad, competencia = null)

        val dominio = relacion.toDomain()

        assertTrue(dominio.diasRestantes >= 9)
    }

    @Test
    fun toEntityMapeaPropiedadesDesdeModeloDominio() {
        val dominio = ActividadFormativa(
            id = 8L,
            titulo = "Entidad Test",
            descripcion = "Detalle",
            progreso = 90,
            diasRestantes = 3,
            prioridad = Prioridad.ALTA,
            fechaLimite = "2026-12-31"
        )

        val entidad = dominio.toEntity(competenciaId = 2L, completada = true)

        assertEquals(8L, entidad.id)
        assertEquals(2L, entidad.competenciaId)
        assertEquals("Entidad Test", entidad.titulo)
        assertEquals("Detalle", entidad.descripcion)
        assertEquals(90, entidad.progreso)
        assertEquals("ALTA", entidad.prioridad)
        assertEquals(true, entidad.completada)
    }

    @Test
    fun toEntityUsaValoresPorDefectoParaCompetenciaYCompletada() {
        val dominio = ActividadFormativa(
            id = 0L,
            titulo = "Defectos",
            descripcion = null,
            progreso = 0,
            diasRestantes = 5,
            prioridad = Prioridad.BAJA,
            fechaLimite = null
        )

        val entidad = dominio.toEntity()

        assertEquals(null, entidad.competenciaId)
        assertEquals(false, entidad.completada)
        assertEquals("BAJA", entidad.prioridad)
    }

    @Test
    fun toEntityCalculaMillisDesdeDiasRestantesCuandoFechaEsNula() {
        val dominio = ActividadFormativa(
            id = 3L,
            titulo = "Sin Fecha String",
            descripcion = null,
            progreso = 0,
            diasRestantes = 7,
            prioridad = Prioridad.MEDIA,
            fechaLimite = null
        )

        val entidad = dominio.toEntity()

        assertNotNull(entidad.fechaLimiteMillis)
    }
}
