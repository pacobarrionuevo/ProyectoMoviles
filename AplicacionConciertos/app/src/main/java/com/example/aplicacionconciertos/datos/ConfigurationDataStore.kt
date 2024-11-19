package com.example.aplicacionconciertos.datos

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ConfigurationDataStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("dataStore")
        val KEY_TEMA_OSCURO = booleanPreferencesKey("temaOscuro")
        val KEY_CANTANTE_FAVORITO = stringPreferencesKey("cantanteFavorito")
        val KEY_EPOCA_FAVORITA = stringPreferencesKey("epocaFavorita")
        val KEY_GENEROS_SELECCIONADOS = stringPreferencesKey("generosSeleccionados")
    }

    //Obtener y guardar el tema oscuro
    val getDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[KEY_TEMA_OSCURO] ?: false
        }

    suspend fun saveDarkTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TEMA_OSCURO] = isDarkTheme
        }
    }

    //Obtener y guardar el cantante favorito
    val getFavoriteSinger: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[KEY_CANTANTE_FAVORITO] ?: "Kendrick Lamar"
        }

    suspend fun saveFavoriteSinger(singer: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_CANTANTE_FAVORITO] = singer
        }
    }

    // Obtener y guardar la época favorita
    val getFavoriteEpoch: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[KEY_EPOCA_FAVORITA] ?: "80s"
        }

    suspend fun saveFavoriteEpoch(epoch: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_EPOCA_FAVORITA] = epoch
        }
    }

    //Obtener y guardar los generos favoritos
    suspend fun saveSelectedGenres(generosSeleccionados: List<String>) {
        val generoSet = generosSeleccionados.toSet()  // Para evitar duplicados
        context.dataStore.edit { preferences ->
            preferences[KEY_GENEROS_SELECCIONADOS] = generoSet.joinToString(",")
        }
    }

    val getSelectedGenres: Flow<List<String>> = context.dataStore.data
        .map { preferences ->
            preferences[KEY_GENEROS_SELECCIONADOS]?.split(",") ?: emptyList()
        }

}