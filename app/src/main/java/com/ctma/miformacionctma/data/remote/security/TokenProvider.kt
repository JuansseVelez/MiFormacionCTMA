package com.ctma.miformacionctma.data.remote.security

class TokenProvider {

    private var tokenSesion: String? = null

    fun obtenerToken(): String? {
        return tokenSesion
    }

    fun guardarToken(nuevoToken: String) {
        tokenSesion = nuevoToken
    }

    fun limpiarToken() {
        tokenSesion = null
    }
}
