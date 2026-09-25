package com.ctma.miformacionctma.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ctma.miformacionctma.data.RoomActividadRepository
import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.domain.ActividadRepository
import com.ctma.miformacionctma.ui.screens.SincronizacionUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ActividadesViewModel(private val repository: ActividadRepository) : ViewModel() {

    val actividades: StateFlow<List<ActividadFormativa>> = repository.obtenerActividades()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _sincronizacionState = MutableStateFlow(SincronizacionUiState())
    val sincronizacionState: StateFlow<SincronizacionUiState> = _sincronizacionState.asStateFlow()

    fun sincronizar() {
        viewModelScope.launch {
            _sincronizacionState.value = _sincronizacionState.value.copy(estaSincronizando = true, errorMensaje = null)
            if (repository is RoomActividadRepository) {
                val resultado = repository.sincronizarConServidor()
                val horaActual = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                if (resultado.isSuccess) {
                    _sincronizacionState.value = SincronizacionUiState(
                        estaSincronizando = false,
                        errorMensaje = null,
                        ultimaSincronizacion = horaActual
                    )
                } else {
                    _sincronizacionState.value = SincronizacionUiState(
                        estaSincronizando = false,
                        errorMensaje = resultado.exceptionOrNull()?.message ?: "Error al sincronizar con el servidor",
                        ultimaSincronizacion = _sincronizacionState.value.ultimaSincronizacion
                    )
                }
            } else {
                _sincronizacionState.value = SincronizacionUiState(estaSincronizando = false)
            }
        }
    }

    companion object {
        fun Factory(repository: ActividadRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ActividadesViewModel(repository) as T
            }
        }
    }
}
