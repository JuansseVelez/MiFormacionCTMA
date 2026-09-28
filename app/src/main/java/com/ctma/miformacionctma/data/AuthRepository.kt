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
            // Permisivo para cuentas creadas por registro local previo
            emailLimpio.contains("instructor") && pass.length >= 6 -> 
                Pair(RolUsuario.INSTRUCTOR, "Instructor (Registrado)")
            emailLimpio.isNotBlank() && pass.length >= 6 -> 
                Pair(RolUsuario.APRENDIZ, "Aprendiz (Registrado)")
            else -> return Result.failure(Exception("Credenciales inválidas o contraseña muy corta"))
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
        if (pass.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
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
