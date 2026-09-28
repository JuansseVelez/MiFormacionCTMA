package com.ctma.miformacionctma.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ctma.miformacionctma.data.AuthRepository
import com.ctma.miformacionctma.data.PreferenciasRepository
import com.ctma.miformacionctma.domain.RolUsuario
import com.ctma.miformacionctma.domain.Usuario
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    preferenciasRepository: PreferenciasRepository
) : ViewModel() {

    val usuarioActual: StateFlow<Usuario?> = preferenciasRepository.usuarioActual
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun iniciarSesion(email: String, pass: String, onResultado: (Boolean) -> Unit) {
        viewModelScope.launch {
            val resultado = authRepository.iniciarSesion(email, pass)
            onResultado(resultado.isSuccess)
        }
    }

    fun registrarse(nombre: String, email: String, pass: String, rol: RolUsuario, onResultado: (Boolean) -> Unit) {
        viewModelScope.launch {
            val resultado = authRepository.registrarse(nombre, email, pass, rol)
            onResultado(resultado.isSuccess)
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            authRepository.cerrarSesion()
        }
    }

    companion object {
        fun Factory(authRepository: AuthRepository, preferenciasRepository: PreferenciasRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AuthViewModel(authRepository, preferenciasRepository) as T
                }
            }
    }
}
