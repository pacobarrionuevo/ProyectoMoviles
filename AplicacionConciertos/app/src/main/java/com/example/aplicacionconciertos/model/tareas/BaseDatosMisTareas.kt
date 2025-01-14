package com.example.aplicacionconciertos.model.tareas
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MiTarea::class], version = 1)
abstract class BaseDatosMisTareas : RoomDatabase() {
    abstract fun daoMisTareas(): DaoMisTareas

    companion object {
        @Volatile
        private var INSTANCE: BaseDatosMisTareas? = null

        fun obtenerBaseDatos(context: Context): BaseDatosMisTareas {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    BaseDatosMisTareas::class.java,
                    "mis_tareas_database_v2"
                ).build().also { INSTANCE = it }
            }
        }
    }
}