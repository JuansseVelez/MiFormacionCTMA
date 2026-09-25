package com.ctma.miformacionctma.data.remote.mappers

import com.ctma.miformacionctma.data.local.entities.ActividadEntity
import com.ctma.miformacionctma.data.remote.dto.ActividadDto
import org.junit.Assert.assertEquals
import org.junit.Test

class MapeadoresRedTest {

    @Test
    fun dtoToEntityMapeaCorrectamente() {
        val dto = ActividadDto(
            id = 1L,
            competenciaId = 2L,
            titulo = "Dto Test",
            descripcion = "Desc Dto",
            progreso = 80,
            fechaLimiteMillis = 123456789L,
            prioridad = "ALTA",
            completada = true
        )

        val entidad = dto.toEntity()

        assertEquals(dto.id, entidad.id)
        assertEquals(dto.competenciaId, entidad.competenciaId)
        assertEquals(dto.titulo, entidad.titulo)
        assertEquals(dto.descripcion, entidad.descripcion)
        assertEquals(dto.progreso, entidad.progreso)
        assertEquals(dto.fechaLimiteMillis, entidad.fechaLimiteMillis)
        assertEquals(dto.prioridad, entidad.prioridad)
        assertEquals(dto.completada, entidad.completada)
    }

    @Test
    fun entityToDtoMapeaCorrectamente() {
        val entidad = ActividadEntity(
            id = 2L,
            competenciaId = null,
            titulo = "Entity Test",
            descripcion = null,
            progreso = 50,
            fechaLimiteMillis = 987654321L,
            prioridad = "MEDIA",
            completada = false
        )

        val dto = entidad.toDto()

        assertEquals(entidad.id, dto.id)
        assertEquals(entidad.competenciaId, dto.competenciaId)
        assertEquals(entidad.titulo, dto.titulo)
        assertEquals(entidad.descripcion, dto.descripcion)
        assertEquals(entidad.progreso, dto.progreso)
        assertEquals(entidad.fechaLimiteMillis, dto.fechaLimiteMillis)
        assertEquals(entidad.prioridad, dto.prioridad)
        assertEquals(entidad.completada, dto.completada)
    }

    @Test
    fun listaDtoToEntityListMapeaColeccionCompleta() {
        val dtos = listOf(
            ActividadDto(id = 1L, titulo = "A1", prioridad = "ALTA"),
            ActividadDto(id = 2L, titulo = "A2", prioridad = "BAJA")
        )

        val entidades = dtos.toEntityList()

        assertEquals(2, entidades.size)
        assertEquals("A1", entidades[0].titulo)
        assertEquals("A2", entidades[1].titulo)
    }
}
