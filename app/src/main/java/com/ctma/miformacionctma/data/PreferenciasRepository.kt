package com.ctma.miformacionctma.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class PreferenciasRepository(private val context: Context) {
    private val KEY_FILTRO_PRIORIDAD = stringPreferencesKey("filtro_prioridad")

    val filtroPrioridad: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[KEY_FILTRO_PRIORIDAD]
    }

    suspend fun guardarFiltroPrioridad(prioridad: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_FILTRO_PRIORIDAD] = prioridad
        }
    }
}
