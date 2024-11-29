package com.example.aplicacionconciertos.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionconciertos.model.artistas.ArtistRepository
import com.example.aplicacionconciertos.model.artistas.DatosArtistas
import kotlinx.coroutines.launch

class ViewModelArtistas : ViewModel() {
    private val repository = ArtistRepository()

    private val _artistas = MutableLiveData<List<DatosArtistas>>()
    val artistas: LiveData<List<DatosArtistas>> = _artistas

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun fetchArtistas() {
        viewModelScope.launch {
            try {
                val artistas = repository.getArtistRepository()
                _artistas.value = artistas
            } catch (e: Exception) {
                _error.value = e.message ?: "Error desconocido"
            }
        }
    }


}