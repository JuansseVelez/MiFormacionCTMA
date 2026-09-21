package com.ctma.miformacionctma.ui.screens.crear

object ValidadorFormulario {
    fun validarTitulo(titulo: String): String? {
        val trimmed = titulo.trim()
        if (trimmed.isEmpty()) return "El título es obligatorio."
        if (trimmed.length < 3) return "El título debe tener al menos 3 caracteres."
        if (trimmed.length > 80) return "El título no puede superar los 80 caracteres."
        return null
    }

    fun validarDescripcion(descripcion: String): String? {
        if (descripcion.length > 240) return "La descripción no puede superar los 240 caracteres."
        return null
    }
}
