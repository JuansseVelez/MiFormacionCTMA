package com.ctma.miformacionctma.ui.screens.crear

import com.ctma.miformacionctma.domain.Prioridad

data class FormularioActividadUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val prioridad: Prioridad = Prioridad.BAJA,
    val intentoGuardar: Boolean = false
) {
    val errorTitulo: String?
        get() = if (intentoGuardar) ValidadorFormulario.validarTitulo(titulo) else null

    val errorDescripcion: String?
        get() = if (intentoGuardar) ValidadorFormulario.validarDescripcion(descripcion) else null

    val puedeGuardar: Boolean
        get() = ValidadorFormulario.validarTitulo(titulo) == null &&
                ValidadorFormulario.validarDescripcion(descripcion) == null
}
