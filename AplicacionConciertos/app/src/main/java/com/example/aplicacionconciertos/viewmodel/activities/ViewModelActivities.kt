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

class ViewModelActivities(
    private val activitiesRepository: ActivitiesRepository,
    context: Context
) : ViewModel() {

    private val _activities = MutableStateFlow<List<ActivityResponse>>(emptyList())
    val activities: StateFlow<List<ActivityResponse>> = _activities

    private val _userActivities = MutableStateFlow<List<ParticipationResponse>>(emptyList())
    val userActivities: StateFlow<List<ParticipationResponse>> = _userActivities

    private val appContext = context.applicationContext
    var accessToken: String? = null
    private var userId: String? = null

    init {
        loadCredentials()
    }

    fun loadCredentials() {
        viewModelScope.launch {
            accessToken = DataStoreManager.getAccessToken(appContext).first()
            userId = DataStoreManager.getEmail(appContext).first()
        }
    }

    fun getAllActivities() {
        viewModelScope.launch {
            val result = activitiesRepository.getAllActivities()
            _activities.value = result
        }
    }

    fun getUserActivities() {
        viewModelScope.launch {
            userId?.let {
                val result = activitiesRepository.getUserParticipations(it)
                _userActivities.value = result
            }
        }
    }

    fun createParticipation(activityId: Long) {
        viewModelScope.launch {
            userId?.let {
                val result = activitiesRepository.createParticipation(it, activityId)
                result?.let { _userActivities.value = _userActivities.value + it }
            }
        }
    }

    fun deleteParticipation(participationId: Long) {
        viewModelScope.launch {
            val success = activitiesRepository.deleteParticipation(participationId)
            if (success) {
                _userActivities.value = _userActivities.value.filterNot { it.id == participationId }
            }
        }
    }
}
