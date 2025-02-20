package com.example.aplicacionconciertos.viewmodel.activities

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionconciertos.model.activities.ActivitiesRepository
import com.example.aplicacionconciertos.model.activities.ActivityResponse
import com.example.aplicacionconciertos.model.activities.ParticipationResponse
import com.example.aplicacionconciertos.viewmodel.authentication.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ViewModelActivities(
    private val activitiesRepository: ActivitiesRepository,
    context: Context
) : ViewModel() {

    private val appContext = context.applicationContext

    // Estado para almacenar las actividades
    private val _activities = MutableStateFlow<List<ActivityResponse>>(emptyList())
    val activities: StateFlow<List<ActivityResponse>> = _activities

    // Estado para almacenar actividades en las que el usuario participa
    private val _userParticipations = MutableStateFlow<List<ParticipationResponse>>(emptyList())
    val userParticipations: StateFlow<List<ParticipationResponse>> = _userParticipations

    // Estado para manejar errores o mensajes
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    // Cargar credenciales al inicializar el ViewModel
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
