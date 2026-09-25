package com.ctma.miformacionctma.data.local.mappers

import com.ctma.miformacionctma.data.local.entities.ActividadEntity
import com.ctma.miformacionctma.data.local.entities.ActividadConCompetencia
import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.domain.Prioridad
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

fun ActividadConCompetencia.toDomain(): ActividadFormativa {
    val diffMillis = actividad.fechaLimiteMillis - System.currentTimeMillis()
    val dias = TimeUnit.MILLISECONDS.toDays(diffMillis).toInt().coerceAtLeast(0)
    
    val prioridadEnum = try {
        Prioridad.valueOf(actividad.prioridad)
    } catch (e: Exception) {
        Prioridad.BAJA
    }

    val fechaStr = try {
        sdf.format(Date(actividad.fechaLimiteMillis))
    } catch (e: Exception) {
        null
    }

    return ActividadFormativa(
        id = actividad.id,
        titulo = actividad.titulo,
        descripcion = actividad.descripcion,
        progreso = actividad.progreso,
        diasRestantes = dias,
        prioridad = prioridadEnum,
        fechaLimite = fechaStr
    )
}

fun ActividadFormativa.toEntity(competenciaId: Long? = null, completada: Boolean = false): ActividadEntity {
    val fechaMillis = try {
        fechaLimite?.let { sdf.parse(it)?.time } 
            ?: (System.currentTimeMillis() + TimeUnit.DAYS.toMillis(diasRestantes.toLong()))
    } catch (e: Exception) {
        System.currentTimeMillis() + TimeUnit.DAYS.toMillis(diasRestantes.toLong())
    }

    return ActividadEntity(
        id = id,
        competenciaId = competenciaId,
        titulo = titulo,
        descripcion = descripcion,
        progreso = progreso,
        fechaLimiteMillis = fechaMillis,
        prioridad = prioridad.name,
        completada = completada
    )
}
