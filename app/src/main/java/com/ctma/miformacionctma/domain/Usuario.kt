package com.ctma.miformacionctma.domain

data class Usuario(
    val id: String,
    val nombre: String,
    val email: String,
    val rol: RolUsuario,
    val token: String = "mock_token_12345"
)
