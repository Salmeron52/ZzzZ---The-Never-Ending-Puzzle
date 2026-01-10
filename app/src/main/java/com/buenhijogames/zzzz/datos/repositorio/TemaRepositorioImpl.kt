package com.buenhijogames.zzzz.datos.repositorio

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.buenhijogames.zzzz.dominio.repositorio.TemaRepositorio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ajustes")

class TemaRepositorioImpl @Inject constructor(
    private val context: Context
) : TemaRepositorio {

    private object PreferencesKeys {
        val ES_TEMA_OSCURO = booleanPreferencesKey("es_tema_oscuro")
    }

    override val esTemaOscuro: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            // Por defecto true (modo oscuro)
            preferences[PreferencesKeys.ES_TEMA_OSCURO] ?: true
        }

    override suspend fun establecerTemaOscuro(esOscuro: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ES_TEMA_OSCURO] = esOscuro
        }
    }
}
