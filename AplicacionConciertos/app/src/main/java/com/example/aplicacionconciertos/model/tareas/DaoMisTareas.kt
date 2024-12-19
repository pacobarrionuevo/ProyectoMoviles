package com.example.aplicacionconciertos.model.tareas

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoMisTareas {
    @Query("SELECT * FROM mis_tareas")
    fun obtenerTodasLasTareas(): Flow<List<MiTarea>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTarea(miTarea: MiTarea)

    @Update
    suspend fun actualizarTarea(miTarea: MiTarea)

    @Delete
    suspend fun eliminarTarea(miTarea: MiTarea)
}