package com.example.aplicacionconciertos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aplicacionconciertos.model.RutasNavegacion
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel :  ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthState.Unauthenticated
        } else {
            _authState.value = AuthState.Authenticated
        }
    }

    fun inicioSesion(email: String, password: String) {

        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("El email y/o la contraseña no pueden ser vacíos.")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message?:"Algo ha ido mal")
                }
            }
    }

    fun registro(email: String, password: String) {

        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("El email y/o la contraseña no pueden ser vacíos.")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message?:"Algo ha ido mal")
                }
            }
    }

    fun signout() {
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

}

sealed class AuthState {
    object Authenticated : AuthState()
    object Unauthenticated: AuthState()
    object Loading: AuthState()
    data class Error(val message: String) : AuthState()
}