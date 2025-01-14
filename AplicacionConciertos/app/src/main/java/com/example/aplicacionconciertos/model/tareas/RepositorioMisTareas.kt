package com.example.aplicacionconciertos.model.tareas

class RepositorioMisTareas(private val daoMisTareas: DaoMisTareas) {
    fun obtenerTodasLasTareas() = daoMisTareas.obtenerTodasLasTareas()

    suspend fun insertarTarea(miTarea: MiTarea) = daoMisTareas.insertarTarea(miTarea)

    suspend fun actualizarTarea(miTarea: MiTarea) = daoMisTareas.actualizarTarea(miTarea)

    suspend fun eliminarTarea(miTarea: MiTarea) = daoMisTareas.eliminarTarea(miTarea)

    fun obtenerNumeroTareasPendientes(miTarea: MiTarea) = daoMisTareas.obtenerNumeroTareasPendientes(miTarea)
}