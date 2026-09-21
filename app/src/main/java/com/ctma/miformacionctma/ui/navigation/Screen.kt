package com.ctma.miformacionctma.ui.navigation

sealed class Screen(val route: String) {
    object Lista : Screen("lista")
    object Crear : Screen("crear")
    object Detalle : Screen("detalle/{actividadId}") {
        fun crearRuta(actividadId: Long): String = "detalle/$actividadId"
    }
}
