package com.ctma.miformacionctma.ui.screens

data class SincronizacionUiState(
    val estaSincronizando: Boolean = false,
    val errorMensaje: String? = null,
    val ultimaSincronizacion: String? = null
)
