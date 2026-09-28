package com.ctma.miformacionctma.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ctma.miformacionctma.domain.RolUsuario
import com.ctma.miformacionctma.domain.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class PreferenciasRepository(private val context: Context) {
    private val KEY_FILTRO_PRIORIDAD = stringPreferencesKey("filtro_prioridad")
    private val KEY_USER_ID = stringPreferencesKey("user_id")
    private val KEY_USER_NOMBRE = stringPreferencesKey("user_nombre")
    private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
    private val KEY_USER_ROL = stringPreferencesKey("user_rol")

    val filtroPrioridad: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[KEY_FILTRO_PRIORIDAD]
    }

    suspend fun guardarFiltroPrioridad(prioridad: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_FILTRO_PRIORIDAD] = prioridad
        }
    }

    val usuarioActual: Flow<Usuario?> = context.dataStore.data.map { preferences ->
        val email = preferences[KEY_USER_EMAIL]
        if (email.isNullOrBlank()) null
        else {
            Usuario(
                id = preferences[KEY_USER_ID] ?: "1",
                nombre = preferences[KEY_USER_NOMBRE] ?: "Usuario",
                email = email,
                rol = try {
                    RolUsuario.valueOf(preferences[KEY_USER_ROL] ?: "APRENDIZ")
                } catch (_: Exception) {
                    RolUsuario.APRENDIZ
                }
            )
        }
    }

    suspend fun guardarSesion(usuario: Usuario) {
        context.dataStore.edit { preferences ->
            preferences[KEY_USER_ID] = usuario.id
            preferences[KEY_USER_NOMBRE] = usuario.nombre
            preferences[KEY_USER_EMAIL] = usuario.email
            preferences[KEY_USER_ROL] = usuario.rol.name
        }
    }

    suspend fun cerrarSesion() {
        context.dataStore.edit { preferences ->
            preferences.remove(KEY_USER_ID)
            preferences.remove(KEY_USER_NOMBRE)
            preferences.remove(KEY_USER_EMAIL)
            preferences.remove(KEY_USER_ROL)
        }
    }
}
