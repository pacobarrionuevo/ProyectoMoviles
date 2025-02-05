package com.example.aplicacionconciertos.viewmodel.authentication

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionconciertos.R
import com.example.aplicacionconciertos.model.authentication.AuthRepository
import com.example.aplicacionconciertos.model.authentication.AuthRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ViewModelAuth(
    private val authRepository: AuthRepository,
    private val context: Context
) : ViewModel() {

    // Estado de autenticación
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // Iniciar sesión
    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            // Crear un objeto AuthRequest con el email y la contraseña
            val authRequest = AuthRequest(email, password)
            val result = authRepository.login(email, password) // Pasar el objeto AuthRequest
            if (result.isSuccess) {
                val loginResponse = result.getOrNull()!!
                DataStoreManager.saveCredentials(context, loginResponse.accessToken, loginResponse.refreshToken, email)
                _authState.value = AuthState.Authenticated(loginResponse.accessToken, loginResponse.refreshToken, email)
            } else {
                _authState.value = AuthState.Error(R.string.ErrorInicioSesion)
            }
        }
    }

    // Registrar usuario
    fun signUp(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            // Crear un objeto AuthRequest con el email y la contraseña
            val authRequest = AuthRequest(email, password)
            val result = authRepository.signUp(email, password) // Pasar el objeto AuthRequest
            if (result.isSuccess) {
                _authState.value = AuthState.Success("User registered successfully")
            } else {
                _authState.value = AuthState.Error(R.string.ErrorRegistro)
            }
        }
    }

    // Cerrar sesión
    fun signOut() {
        viewModelScope.launch {
            DataStoreManager.clearCredentials(context)
            _authState.value = AuthState.SignedOut
        }
    }

    // Obtener datos del usuario
    fun getUserDataAndSave(email: String) {
        viewModelScope.launch {
            val accessToken = DataStoreManager.getAccessToken(context).first()
            if (accessToken != null) {
                val result = authRepository.getUserDetails(accessToken)
                if (result.isSuccess) {
                    _authState.value = AuthState.Success("User data retrieved")
                } else {
                    _authState.value = AuthState.Error(R.string.ErrorUserData)
                }
            }
        }
    }

    // Refrescar token de acceso
    fun refreshAndSaveToken() {
        viewModelScope.launch {
            val refreshToken = DataStoreManager.getRefreshToken(context).first()
            if (refreshToken != null) {
                val result = authRepository.refreshToken(refreshToken)
                if (result.isSuccess) {
                    val newAccessToken = result.getOrNull()!!.token
                    DataStoreManager.saveCredentials(context, newAccessToken, refreshToken, "")
                    _authState.value = AuthState.Authenticated(newAccessToken, refreshToken, "")
                } else {
                    _authState.value = AuthState.Error(R.string.ErrorTokenRefresh)
                }
            }
        }
    }

    // Cargar credenciales al iniciar
    fun loadCredentials() {
        viewModelScope.launch {
            val accessToken = DataStoreManager.getAccessToken(context).first()
            val refreshToken = DataStoreManager.getRefreshToken(context).first()
            val email = DataStoreManager.getEmail(context).first()
            if (accessToken != null && refreshToken != null && email != null) {
                _authState.value = AuthState.Authenticated(accessToken, refreshToken, email)
            }
        }
    }
}