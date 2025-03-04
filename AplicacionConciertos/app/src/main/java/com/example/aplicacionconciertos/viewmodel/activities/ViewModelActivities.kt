package com.example.aplicacionconciertos.viewmodel.activities

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _userParticipations = MutableStateFlow<List<ParticipationResponse>>(emptyList())
    val userParticipations: StateFlow<List<ParticipationResponse>> = _userParticipations

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    val currentToken: StateFlow<String?> = DataStoreManager.getAccessToken(context)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    init {
        viewModelScope.launch {
            DataStoreManager.getAccessToken(appContext).collectLatest { token ->
                if (!token.isNullOrEmpty()) {
                    Log.d("ViewModelActivities", "Token obtenido: '$token'")
                    getAllActivities(token)
                } else {
                    Log.e("ViewModelActivities", "Error: Token es nulo o vacío")
                }
            }
        }
    }



    fun getAllActivities(accessToken: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val activitiesList = activitiesRepository.getAllActivities(accessToken)
            _activities.emit(activitiesList)
            _isLoading.value = false
        }
    }


    fun getUserParticipations(userId: String, accessToken: String) {
        viewModelScope.launch {
            Log.d("ViewModelActivities", "Solicitando participaciones para userId: $userId")
            val participations = activitiesRepository.getUserParticipations(userId, accessToken)
            Log.d("ViewModelActivities", "Participaciones recibidas: ${participations.size}")
            _userParticipations.value = participations
        }
    }


    fun createParticipation(userId: String, activityId: Long, accessToken: String) {
        viewModelScope.launch {
            Log.d("ViewModelActivities", "Intentando crear participación para userId: $userId en actividad: $activityId")
            val result = activitiesRepository.createParticipation(userId, activityId, accessToken)
            if (result != null) {
                Log.d("ViewModelActivities", "Participación creada: $result")
                _message.value = "Te has apuntado a la actividad"
                getUserParticipations(userId, accessToken)
            } else {
                Log.e("ViewModelActivities", "Error al crear la participación")
                _message.value = "Error al apuntarse a la actividad"
            }
        }
    }




    fun deleteParticipation(
        userId: String,
        activityId: Long,
        accessToken: String
    ) {
        viewModelScope.launch {
            Log.d("ViewModelActivities", "Intentando borrar participación para userId: $userId, activityId: $activityId")
            val success = activitiesRepository.deleteParticipation(userId, activityId, accessToken)
            if (success) {
                Log.d("ViewModelActivities", "Participación borrada correctamente")
                _message.value = "Te has borrado de la actividad"
                getUserParticipations(userId, accessToken)
            } else {
                Log.e("ViewModelActivities", "Error al borrar la participación")
                _message.value = "Error al borrarse de la actividad"
            }
        }
    }

}
