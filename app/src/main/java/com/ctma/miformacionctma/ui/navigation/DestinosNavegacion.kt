package com.ctma.miformacionctma.ui.navigation

sealed class Destino(val ruta: String) {
    object Lista : Destino("lista")
    object Crear : Destino("crear")
    object Detalle : Destino("detalle/{id}") {
        fun crearRuta(id: Long) = "detalle/$id"
    }
}
