package com.ctma.miformacionctma.data

import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.domain.ActividadRepository

class ActividadRepositoryImpl : ActividadRepository {
    private val actividades = mutableListOf<ActividadFormativa>()

    override fun obtenerActividades(): List<ActividadFormativa> {
        return actividades
    }

    override fun obtenerActividadPorId(id: Long): ActividadFormativa? {
        return actividades.find { it.id == id }
    }

    override fun agregarActividad(actividad: ActividadFormativa) {
        actividades.add(actividad)
    }
}