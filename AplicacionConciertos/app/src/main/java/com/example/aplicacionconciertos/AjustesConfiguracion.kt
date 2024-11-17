package com.example.aplicacionconciertos

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AjustesConfiguracion(private val context: Context) {

    // Para asegurar que solo haya una instancia
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
        val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
        val FAVORITE_SINGER_KEY = stringPreferencesKey("favorite_singer")
        val FAVORITE_EPOCH_KEY = stringPreferencesKey("favorite_epoch")
    }

    // Obtener el tema oscuro
    val getDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_THEME_KEY] ?: false
        }

    // Obtener el cantante favorito
    val getFavoriteSinger: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[FAVORITE_SINGER_KEY] ?: "Kendrick Lamar"
        }

    // Obtener la época favorita
    val getFavoriteEpoch: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[FAVORITE_EPOCH_KEY] ?: "80s"
        }

    // Guardar el tema oscuro en DataStore
    suspend fun saveDarkTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDarkTheme
        }
    }

    // Guardar el cantante favorito en DataStore
    suspend fun saveFavoriteSinger(singer: String) {
        context.dataStore.edit { preferences ->
            preferences[FAVORITE_SINGER_KEY] = singer
        }
    }

    // Guardar la época favorita en DataStore
    suspend fun saveFavoriteEpoch(epoch: String) {
        context.dataStore.edit { preferences ->
            preferences[FAVORITE_EPOCH_KEY] = epoch
        }
    }
}