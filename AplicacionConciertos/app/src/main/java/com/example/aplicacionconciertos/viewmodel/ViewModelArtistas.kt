package com.example.aplicacionconciertos.viewmodel

import android.util.Log
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


    fun fetchArtistas() {
        viewModelScope.launch {
            try {
                val artistas = repository.getArtistRepository()
                _artistas.value = artistas
                Log.d("ViewModelArtistas", "Artistas cargados: ${artistas.size}")
            } catch (e: Exception) {
                println("Error: ${e.message}")
                _artistas.value = emptyList()
            }
        }
    }


}