package com.example.aplicacionconciertos.model


import AplicacionPrincipal
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.aplicacionconciertos.AcercaDe
import com.example.aplicacionconciertos.ColeccionArtistas
import com.example.aplicacionconciertos.Configuracion
import com.example.aplicacionconciertos.SobreNosotros
import com.example.aplicacionconciertos.Tareas
import com.example.aplicacionconciertos.auth.InicioSesion
import com.example.aplicacionconciertos.auth.Registro
import com.example.aplicacionconciertos.viewmodel.AuthState
import com.example.aplicacionconciertos.viewmodel.AuthViewModel
import com.example.aplicacionconciertos.viewmodel.ViewModelArtistas


@Composable
fun AppNavigation(navController: NavHostController, authState: AuthState) {

    val authViewModel: AuthViewModel = viewModel()
    val viewModelArtistas: ViewModelArtistas = viewModel()

    val startDestination = when (authState) {
        is AuthState.Authenticated -> RutasNavegacion.Home.route
        else -> RutasNavegacion.InicioSesion.route
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(RutasNavegacion.Home.route) {
            AplicacionPrincipal(navController, authViewModel)
        }
        composable(RutasNavegacion.AcercaDe.route) {
            AcercaDe(navController)
        }
        composable(RutasNavegacion.SobreNosotros.route) {
            SobreNosotros(navController)
        }
        composable(RutasNavegacion.Artistas.route) {
            ColeccionArtistas(viewModelArtistas, navController)
        }
        composable(RutasNavegacion.Configuracion.route) {
            Configuracion(navController)
        }
        composable(RutasNavegacion.InicioSesion.route) {
            InicioSesion(authViewModel, navController)
        }
        composable(RutasNavegacion.Registro.route) {
            Registro(authViewModel, navController)
        }
        composable(RutasNavegacion.Tareas.route) {
            Tareas(navController)
        }
    }
}
