package com.example.aplicacionconciertos.viewmodel.activities

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionconciertos.model.activities.ActivitiesRepository
import com.example.aplicacionconciertos.model.activities.ActivityResponse
import com.example.aplicacionconciertos.model.activities.ParticipationResponse
import com.example.aplicacionconciertos.viewmodel.authentication.DataStoreManager
import com.example.aplicacionconciertos.viewmodel.authentication.ViewModelAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ViewModelActivities(
    private val activitiesRepository: ActivitiesRepository,
    private val viewModelAuth: ViewModelAuth,
    context: Context
) : ViewModel() {

    private val appContext = context.applicationContext

    private val _activities = MutableStateFlow<List<ActivityResponse>>(emptyList())
    val activities: StateFlow<List<ActivityResponse>> = _activities

    private val _userParticipations = MutableStateFlow<List<ParticipationResponse>>(emptyList())
    val userParticipations: StateFlow<List<ParticipationResponse>> = _userParticipations

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init {
        loadCredentials()
    }

    private fun loadCredentials() {
        viewModelScope.launch(Dispatchers.IO) {
            val token = DataStoreManager.getAccessTokenSync(appContext)
            if (!token.isNullOrEmpty()) {
                activitiesRepository.setAccessToken(token)
            }
        }
    }

    fun refreshToken() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelAuth.refreshAndSaveToken()
            // Después de refrescar, obtén el token actualizado
            val newToken = DataStoreManager.getAccessTokenSync(appContext)
            if (!newToken.isNullOrEmpty()) {
                activitiesRepository.setAccessToken(newToken)
            } else {
                _message.value = "No se pudo actualizar el token."
            }
        }
    }

    fun getAllActivities() {
        viewModelScope.launch {
            _activities.value = activitiesRepository.getAllActivities()
        }
    }

    fun getUserParticipations(userId: String) {
        viewModelScope.launch {
            _userParticipations.value = activitiesRepository.getUserParticipations(userId)
        }
    }

    fun createParticipation(userId: String, activityId: Long) {
        viewModelScope.launch {
            val result = activitiesRepository.createParticipation(userId, activityId)
            if (result != null) {
                _message.value = "Te has apuntado a la actividad"
                getUserParticipations(userId)
            } else {
                _message.value = "Error al apuntarse a la actividad"
            }
        }
    }

    fun deleteParticipation(participationId: Long, userId: String) {
        viewModelScope.launch {
            val success = activitiesRepository.deleteParticipation(participationId)
            if (success) {
                _message.value = "Te has borrado de la actividad"
                getUserParticipations(userId)
            } else {
                _message.value = "Error al borrarse de la actividad"
            }
        }
    }
}
