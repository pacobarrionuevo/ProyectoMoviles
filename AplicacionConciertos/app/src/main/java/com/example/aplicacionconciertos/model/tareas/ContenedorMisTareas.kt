package com.example.aplicacionconciertos.model.tareas

import android.content.Context

class ContenedorMisTareas(private val context: Context) {
    val repositorioMisTareas: RepositorioMisTareas by lazy {
        RepositorioMisTareas(BaseDatosMisTareas.obtenerBaseDatos(context).daoMisTareas())
    }
}