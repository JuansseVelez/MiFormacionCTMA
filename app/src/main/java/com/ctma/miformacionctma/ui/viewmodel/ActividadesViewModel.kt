package com.ctma.miformacionctma.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.domain.ActividadRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ActividadesViewModel(private val repository: ActividadRepository) : ViewModel() {

    val actividades: StateFlow<List<ActividadFormativa>> = repository.obtenerActividades()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    companion object {
        fun Factory(repository: ActividadRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ActividadesViewModel(repository) as T
            }
        }
    }
}
