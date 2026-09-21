package com.ctma.miformacionctma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ctma.miformacionctma.data.ActividadRepositoryImpl
import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.domain.Prioridad
import com.ctma.miformacionctma.ui.screens.PantallaActividades
import com.ctma.miformacionctma.ui.theme.MiFormacionCTMATheme

class MainActivity : ComponentActivity() {

    private val repository = ActividadRepositoryImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cargar datos de prueba (10 actividades para validar LazyColumn)
        cargarDatosPrueba()

        enableEdgeToEdge()
        setContent {
            MiFormacionCTMATheme {
                val listaActividades = repository.obtenerActividades()
                PantallaActividades(actividades = listaActividades)
            }
        }
    }

    private fun cargarDatosPrueba() {
        val actividadesDePrueba = listOf(
            ActividadFormativa(1L, "Guía 1: Configuración de Entorno", "Instalación de Android Studio y Git", 100, 0, Prioridad.ALTA),
            ActividadFormativa(2L, "Guía 2: Sintaxis de Kotlin", "Uso de data classes y enum classes", 100, 0, Prioridad.ALTA),
            ActividadFormativa(3L, "Guía 3: Jetpack Compose", "Creación de tarjetas y listas perezosas", 60, 2, Prioridad.ALTA),
            ActividadFormativa(4L, "Taller de Git y GitHub", "Flujo de trabajo con ramas y Pull Requests", 90, 1, Prioridad.MEDIA),
            ActividadFormativa(5L, "Revisión de Accesibilidad", "Validación de lectores de pantalla y fuentes", 20, 5, Prioridad.BAJA),
            ActividadFormativa(6L, "Diseño Adaptable M3", "Ajuste de interfaces para pantallas anchas", 0, 7, Prioridad.MEDIA),
            ActividadFormativa(7L, "Pruebas Unitarias Kotlin", "Validación de reglas de negocio en domain", 10, 8, Prioridad.BAJA),
            ActividadFormativa(8L, "Inyección de Dependencias", "Configuración básica con Hilt/Koin", 0, 10, Prioridad.MEDIA),
            ActividadFormativa(9L, "Persistencia con Room", "Creación de base de datos local SQLite", 0, 12, Prioridad.ALTA),
            ActividadFormativa(10L, "Consumo de API Rest", "Integración con Retrofit para datos remotos", 0, 15, Prioridad.ALTA)
        )

        actividadesDePrueba.forEach { actividad ->
            repository.agregarActividad(actividad)
        }
    }
}