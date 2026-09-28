package com.ctma.miformacionctma.data

import com.ctma.miformacionctma.domain.RolUsuario
import com.ctma.miformacionctma.domain.Usuario

class AuthRepository(private val preferenciasRepository: PreferenciasRepository) {

    suspend fun iniciarSesion(email: String, pass: String): Result<Usuario> {
        val emailLimpio = email.trim().lowercase()
        
        val (rol, nombre) = when {
            emailLimpio == "instructor@formacion.ctma" && pass == "instructor123" -> 
                Pair(RolUsuario.INSTRUCTOR, "Instructor Administrador")
            emailLimpio == "aprendiz@formacion.ctma" && pass == "aprendiz123" -> 
                Pair(RolUsuario.APRENDIZ, "Aprendiz Usuario")
            else -> return Result.failure(Exception("Correo o contraseña incorrectos. Cuenta no existe."))
        }

        val usuario = Usuario(
            id = "user_${System.currentTimeMillis()}",
            nombre = nombre,
            email = emailLimpio,
            rol = rol
        )
        preferenciasRepository.guardarSesion(usuario)
        return Result.success(usuario)
    }

    suspend fun registrarse(nombre: String, email: String, pass: String, rol: RolUsuario): Result<Usuario> {
        if (email.isBlank() || pass.length < 6) {
            return Result.failure(Exception("Datos inválidos. Contraseña mínimo de 6 caracteres."))
        }
        val usuario = Usuario(
            id = "user_${System.currentTimeMillis()}",
            nombre = nombre,
            email = email.trim().lowercase(),
            rol = rol
        )
        preferenciasRepository.guardarSesion(usuario)
        return Result.success(usuario)
    }

    suspend fun cerrarSesion() {
        preferenciasRepository.cerrarSesion()
    }
}
