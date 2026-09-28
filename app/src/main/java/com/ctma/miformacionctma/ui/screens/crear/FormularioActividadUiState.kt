package com.ctma.miformacionctma.ui.screens.crear

import com.ctma.miformacionctma.domain.Prioridad

data class FormularioActividadUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val fecha: String = "",
    val prioridad: Prioridad = Prioridad.BAJA,
    val progreso: Int = 0,
    val errorTitulo: String? = null,
    val errorDescripcion: String? = null,
    val errorFecha: String? = null,
    val errorProgreso: String? = null,
    val puedeGuardar: Boolean = false
)
