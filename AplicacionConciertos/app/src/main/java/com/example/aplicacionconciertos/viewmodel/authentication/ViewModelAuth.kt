package com.example.aplicacionconciertos.viewmodel.authentication


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authRepository.login(email, password)
            if (result.isSuccess) {
                val loginResponse = result.getOrNull()
                if (loginResponse != null) {
                    DataStoreManager.saveCredentials(context, loginResponse.accessToken, loginResponse.refreshToken, email)
                    _authState.value = AuthState.Authenticated(loginResponse.accessToken, loginResponse.refreshToken, email)
                } else {
                    _authState.value = AuthState.Error("Unexpected response from server.")
                }
            } else {
                _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Login failed.")
            }
        }
    }

    fun signUp(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            val result = authRepository.signUp(email, password)
            if (result.isSuccess) {
                _authState.value = AuthState.Success("User registered successfully.")
            } else {
                _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Registration failed.")
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            DataStoreManager.clearCredentials(context)
            _authState.value = AuthState.SignedOut
        }
    }

    fun refreshAndSaveToken() {
        viewModelScope.launch {
            val refreshToken = DataStoreManager.getRefreshToken(context).first()
            if (refreshToken != null) {
                val result = authRepository.refreshToken(refreshToken)
                if (result.isSuccess) {
                    val newAccessToken = result.getOrNull()?.token
                    if (newAccessToken != null) {
                        DataStoreManager.saveCredentials(context, newAccessToken, refreshToken, "")
                        _authState.value = AuthState.Authenticated(newAccessToken, refreshToken, "")
                    } else {
                        _authState.value = AuthState.Error("Failed to refresh token.")
                    }
                } else {
                    _authState.value = AuthState.Error(result.exceptionOrNull()?.message ?: "Token refresh failed.")
                }
            }
        }
    }

    fun loadCredentials() {
        viewModelScope.launch {
            val accessToken = DataStoreManager.getAccessToken(context).first()
            val refreshToken = DataStoreManager.getRefreshToken(context).first()
            val email = DataStoreManager.getEmail(context).first()
            if (accessToken != null && refreshToken != null && email != null) {
                _authState.value = AuthState.Authenticated(accessToken, refreshToken, email)
            } else {
                _authState.value = AuthState.Idle
            }
        }
    }
}
