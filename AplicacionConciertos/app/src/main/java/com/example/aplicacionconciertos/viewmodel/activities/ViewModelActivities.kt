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

    private val _activities = MutableStateFlow<List<ActivityResponse>>(emptyList())
    val activities: StateFlow<List<ActivityResponse>> = _activities

    private val _userActivities = MutableStateFlow<List<ParticipationResponse>>(emptyList())
    val userActivities: StateFlow<List<ParticipationResponse>> = _userActivities

    var accessToken: String? = null
    private var userId: String? = null

    fun loadCredentials(context: Context) {
        viewModelScope.launch {
            accessToken = DataStoreManager.getAccessToken(context).first()
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
                result?.let { _userActivities.value = _userActivities.value + it }
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
}