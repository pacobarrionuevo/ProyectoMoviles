package com.example.aplicacionconciertos.viewmodel.authentication


import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionconciertos.model.authentication.AuthRepository
import com.example.aplicacionconciertos.model.authentication.AuthRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewModelAuth(
    private val authRepository: AuthRepository,
    private val context: Context
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        Log.d("Auth", "Login iniciado con email: $email")

        viewModelScope.launch(Dispatchers.IO) {  // Ejecuta en el hilo de fondo
            try {
                val result = authRepository.login(email, password)
                Log.d("Auth", "Resultado del login: $result")

                withContext(Dispatchers.Main) {
                    if (result.isSuccess) {
                        val loginResponse = result.getOrNull()
                        if (loginResponse != null) {
                            Log.d("Auth", "Login exitoso. AccessToken: ${loginResponse.accessToken}")
                            DataStoreManager.saveCredentials(
                                context,
                                loginResponse.accessToken,
                                loginResponse.refreshToken,
                                email
                            )
                            _authState.value = AuthState.Authenticated(
                                loginResponse.accessToken,
                                loginResponse.refreshToken,
                                email
                            )
                            Log.d("Auth", "Credenciales guardadas y usuario autenticado.")
                        } else {
                            Log.e("Auth", "Respuesta inesperada del servidor durante el login.")
                            _authState.value = AuthState.Error("Unexpected response from server.")
                        }
                    } else {
                        val errorMessage = result.exceptionOrNull()?.message ?: "Login failed."
                        Log.e("Auth", "Error en login: $errorMessage")
                        _authState.value = AuthState.Error(errorMessage)
                    }
                }
            } catch (e: Exception) {
                Log.e("Auth", "Excepción durante el login: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    _authState.value = AuthState.Error("An unexpected error occurred.")
                }
            }
        }
    }



    fun signUp(email: String, password: String) {
        _authState.value = AuthState.Loading
        Log.d("Auth", "SignUp iniciado con email: $email")

        viewModelScope.launch(Dispatchers.IO) {
            val result = authRepository.signUp(email, password)
            Log.d("Auth", "Resultado del signUp: $result")

            withContext(Dispatchers.Main) {
                if (result.isSuccess) {
                    Log.d("Auth", "Registro exitoso")
                    _authState.value = AuthState.Success("User registered successfully.")
                } else {
                    val errorMessage = result.exceptionOrNull()?.message ?: "Registration failed."
                    Log.e("Auth", "Error en signUp: $errorMessage")
                    _authState.value = AuthState.Error(errorMessage)
                }
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
