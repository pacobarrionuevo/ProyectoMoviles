package com.example.aplicacionconciertos.viewmodel.activities

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionconciertos.model.activities.ActivitiesRepository
import com.example.aplicacionconciertos.model.activities.ActivityResponse
import com.example.aplicacionconciertos.model.activities.ParticipationResponse
import com.example.aplicacionconciertos.viewmodel.authentication.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ViewModelActivities(
    private val activitiesRepository: ActivitiesRepository,
    context: Context
) : ViewModel() {

    private val appContext = context.applicationContext
    private val dataStoreManager = DataStoreManager()

    // Estado para almacenar las actividades
    private val _activities = MutableStateFlow<List<ActivityResponse>>(emptyList())
    val activities: StateFlow<List<ActivityResponse>> = _activities

    // Estado para almacenar actividades en las que el usuario participa
    private val _userParticipations = MutableStateFlow<List<ParticipationResponse>>(emptyList())
    val userParticipations: StateFlow<List<ParticipationResponse>> = _userParticipations

    // Estado para manejar errores o mensajes
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    // Cargar el accessToken al inicializar el ViewModel
    /*init {
        viewModelScope.launch {
            val token = DataStoreManager.getAccessToken(appContext)
            if (!token.isNullOrEmpty()) {
                activitiesRepository.setAccessToken(token)
            }
        }
    }
    */

    // Obtener todas las actividades
    fun getAllActivities() {
        viewModelScope.launch {
            _activities.value = activitiesRepository.getAllActivities()
        }
    }

    // Obtener las actividades en las que el usuario participa
    fun getUserParticipations(userId: String) {
        viewModelScope.launch {
            _userParticipations.value = activitiesRepository.getUserParticipations(userId)
        }
    }

    // Apuntarse a una actividad
    fun createParticipation(userId: String, activityId: Long) {
        viewModelScope.launch {
            val result = activitiesRepository.createParticipation(userId, activityId)
            if (result != null) {
                _message.value = "Te has apuntado a la actividad"
                getUserParticipations(userId) // Actualizar lista de participaciones
            } else {
                _message.value = "Error al apuntarse a la actividad"
            }
        }
    }

    // Borrarse de una actividad
    fun deleteParticipation(participationId: Long, userId: String) {
        viewModelScope.launch {
            val success = activitiesRepository.deleteParticipation(participationId)
            if (success) {
                _message.value = "Te has borrado de la actividad"
                getUserParticipations(userId) // Actualizar lista de participaciones
            } else {
                _message.value = "Error al borrarse de la actividad"
            }
        }
    }
}
