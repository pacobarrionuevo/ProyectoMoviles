package com.example.aplicacionconciertos

import android.app.Application
import com.example.aplicacionconciertos.model.tareas.ContenedorMisTareas

class AplicacionTareas : Application() {
    lateinit var contenedor: ContenedorMisTareas

    override fun onCreate() {
        super.onCreate()
        contenedor = ContenedorMisTareas(this)
    }
}