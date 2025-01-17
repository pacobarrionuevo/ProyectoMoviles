package com.example.aplicacionconciertos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionconciertos.model.tareas.MiTarea
import com.example.aplicacionconciertos.model.tareas.RepositorioMisTareas
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TareasViewModel(private val repositorioMisTareas: RepositorioMisTareas) : ViewModel() {


    val numeroTareasPendientes: StateFlow<Int> = repositorioMisTareas
        .obtenerNumeroTareasPendientes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0
        )


    fun actualizarTarea(tarea: MiTarea) {
        viewModelScope.launch {
            repositorioMisTareas.actualizarTarea(tarea)
        }
    }


    fun eliminarTarea(tarea: MiTarea) {
        viewModelScope.launch {
            repositorioMisTareas.eliminarTarea(tarea)
        }
    }


    fun insertarTarea(tarea: MiTarea) {
        viewModelScope.launch {
            repositorioMisTareas.insertarTarea(tarea)
        }
    }
}