package com.ctma.miformacionctma.domain

interface ActividadRepository {
    fun obtenerActividades(): List<ActividadFormativa>
    fun obtenerActividadPorId(id: Long): ActividadFormativa?
    fun agregarActividad(actividad: ActividadFormativa)
}