package com.ctma.miformacionctma.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.domain.ActividadRepository
import com.ctma.miformacionctma.domain.Prioridad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FormularioViewModel(private val repository: ActividadRepository) : ViewModel() {

    private val _estaGuardando = MutableStateFlow(false)
    val estaGuardando: StateFlow<Boolean> = _estaGuardando.asStateFlow()

    suspend fun guardarActividad(
        titulo: String,
        descripcion: String,
        fecha: String,
        prioridad: Prioridad,
        progreso: Int
    ) {
        _estaGuardando.value = true
        val nuevaActividad = ActividadFormativa(
            id = 0,
            titulo = titulo,
            descripcion = descripcion,
            progreso = progreso,
            diasRestantes = 7, // Simulado, se calcula en el mapper
            prioridad = prioridad,
            fechaLimite = fecha
        )
        repository.agregarActividad(nuevaActividad)
        _estaGuardando.value = false
    }

    companion object {
        fun Factory(repository: ActividadRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return FormularioViewModel(repository) as T
            }
        }
    }
}
