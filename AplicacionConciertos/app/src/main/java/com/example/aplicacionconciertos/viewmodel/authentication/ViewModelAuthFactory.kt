package com.example.aplicacionconciertos.viewmodel.authentication

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aplicacionconciertos.model.authentication.AuthRepository

class ViewModelAuthFactory(
    private val authRepository: AuthRepository,
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewModelAuth::class.java)) {
            return ViewModelAuth(authRepository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}