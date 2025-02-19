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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ViewModelActivities : ViewModel() {
    private val _accessToken = MutableStateFlow<String>("")
    val accessToken: StateFlow<String> = _accessToken

    private val _activities = MutableStateFlow<List<ActivityResponse>>(emptyList())
    val activities: StateFlow<List<ActivityResponse>> = _activities

    private val _userActivities = MutableStateFlow<List<ParticipationResponse>>(emptyList())
    val userActivities: StateFlow<List<ParticipationResponse>> = _userActivities

    private var userId: String? = null

    fun loadCredentials(context: Context) {
        viewModelScope.launch {
            _accessToken.value = DataStoreManager.getAccessToken(context).first() ?: ""
            userId = DataStoreManager.getEmail(context).first()
        }
    }

    fun getAllActivities(activitiesRepository: ActivitiesRepository) {
        viewModelScope.launch {
            val result = activitiesRepository.getAllActivities()
            _activities.value = result
        }
    }

    fun getUserActivities(activitiesRepository: ActivitiesRepository) {
        viewModelScope.launch {
            userId?.let {
                val result = activitiesRepository.getUserParticipations(it)
                _userActivities.value = result
            }
        }
    }

    fun createParticipation(activitiesRepository: ActivitiesRepository, activityId: Long) {
        viewModelScope.launch {
            userId?.let {
                val result = activitiesRepository.createParticipation(it, activityId)
                result?.let { participation ->
                    _userActivities.value = _userActivities.value + participation
                }
            }
        }
    }

    fun deleteParticipation(activitiesRepository: ActivitiesRepository, participationId: Long) {
        viewModelScope.launch {
            val success = activitiesRepository.deleteParticipation(participationId)
            if (success) {
                _userActivities.value = _userActivities.value.filterNot { it.id == participationId }
            }
        }
    }

    // Función para crear una actividad (evento)
    fun createActivity(
        activitiesRepository: ActivitiesRepository,
        name: String,
        description: String,
        date: String,
        place: String,
        category: String
    ) {
        viewModelScope.launch {
            val result = activitiesRepository.createActivity(name, description, date, place, category)
            result?.let { newActivity ->
                _activities.value = _activities.value + newActivity
            }
        }
    }

    // Función para borrar una actividad
    fun deleteActivity(activitiesRepository: ActivitiesRepository, activityId: Long) {
        viewModelScope.launch {
            val success = activitiesRepository.deleteActivity(activityId)
            if (success) {
                _activities.value = _activities.value.filterNot { it.id == activityId }
            }
        }
    }
}
