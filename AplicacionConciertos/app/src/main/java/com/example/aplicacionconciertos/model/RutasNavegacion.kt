package com.example.aplicacionconciertos.model

sealed class RutasNavegacion(val route: String) {
    object Home : RutasNavegacion("AplicacionPrincipal")
    object SobreNosotros :RutasNavegacion("SobreNosotros")
    object AcercaDe :RutasNavegacion("AcercaDe")
    object Configuracion :RutasNavegacion("Configuracion")
    object Artistas :RutasNavegacion("Artistas")
    object InicioSesion :RutasNavegacion("InicioSesion")
    object Registro: RutasNavegacion("Registro")
    object Tareas: RutasNavegacion("Tareas")
    object Actividades: RutasNavegacion("Actividades")
}