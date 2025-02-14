package com.example.aplicacionconciertos.viewmodel.authentication

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStoreAuth by preferencesDataStore(name = "auth_preferences")

class DataStoreManager {

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val EMAIL_KEY = stringPreferencesKey("email")

        suspend fun saveCredentials(context: Context, accessToken: String, refreshToken: String, email: String) {
            context.dataStoreAuth.edit { preferences ->
                preferences[ACCESS_TOKEN_KEY] = accessToken
                preferences[REFRESH_TOKEN_KEY] = refreshToken
                preferences[EMAIL_KEY] = email
            }
        }

        fun getAccessToken(context: Context): Flow<String?> {
            return context.dataStoreAuth.data.map { preferences ->
                preferences[ACCESS_TOKEN_KEY]
            }
        }

        fun getRefreshToken(context: Context): Flow<String?> {
            return context.dataStoreAuth.data.map { preferences ->
                preferences[REFRESH_TOKEN_KEY]
            }
        }

        fun getEmail(context: Context): Flow<String?> {
            return context.dataStoreAuth.data.map { preferences ->
                preferences[EMAIL_KEY]
            }
        }

        suspend fun clearCredentials(context: Context) {
            context.dataStoreAuth.edit { preferences ->
                preferences.clear()
            }
        }
    }
}
