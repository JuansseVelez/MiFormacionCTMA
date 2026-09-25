package com.ctma.miformacionctma.data.remote.mappers

import com.ctma.miformacionctma.data.local.entities.ActividadEntity
import com.ctma.miformacionctma.data.remote.dto.ActividadDto

fun ActividadDto.toEntity(): ActividadEntity {
    return ActividadEntity(
        id = id,
        competenciaId = competenciaId,
        titulo = titulo,
        descripcion = descripcion,
        progreso = progreso,
        fechaLimiteMillis = fechaLimiteMillis,
        prioridad = prioridad,
        completada = completada
    )
}

fun ActividadEntity.toDto(): ActividadDto {
    return ActividadDto(
        id = id,
        competenciaId = competenciaId,
        titulo = titulo,
        descripcion = descripcion,
        progreso = progreso,
        fechaLimiteMillis = fechaLimiteMillis,
        prioridad = prioridad,
        completada = completada
    )
}

fun List<ActividadDto>.toEntityList(): List<ActividadEntity> {
    return map { it.toEntity() }
}
