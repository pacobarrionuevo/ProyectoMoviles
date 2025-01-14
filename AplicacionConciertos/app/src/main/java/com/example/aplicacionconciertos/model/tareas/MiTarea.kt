package com.example.aplicacionconciertos.model.tareas

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mis_tareas")
data class MiTarea(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "titulo")
    val titulo: String = "", // Valor por defecto
    @ColumnInfo(name = "descripcion")
    val descripcion: String = "", // Valor por defecto
    @ColumnInfo(name = "completada")
    val completada: Boolean = false
)