package com.example.aplicacionconciertos.model

import AplicacionPrincipal
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.aplicacionconciertos.AcercaDe
import com.example.aplicacionconciertos.ColeccionArtistas
import com.example.aplicacionconciertos.Configuracion
import com.example.aplicacionconciertos.SobreNosotros
import com.example.aplicacionconciertos.Tareas
import com.example.aplicacionconciertos.auth.InicioSesion
import com.example.aplicacionconciertos.auth.Registro
import com.example.aplicacionconciertos.viewmodel.AuthState
import com.example.aplicacionconciertos.viewmodel.AuthViewModel
import com.example.aplicacionconciertos.viewmodel.TareasViewModel
import com.example.aplicacionconciertos.viewmodel.ViewModelArtistas
import androidx.compose.ui.platform.LocalContext
import com.example.aplicacionconciertos.model.tareas.ContenedorMisTareas
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp

@Composable
fun AppNavigation(navController: NavHostController, authState: AuthState) {
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val contenedor = remember { ContenedorMisTareas(context) }
    val tareasViewModel: TareasViewModel = viewModel {
        TareasViewModel(contenedor.repositorioMisTareas)
    }
    val artistasViewModel: ViewModelArtistas = viewModel()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val startDestination = when (authState) {
        is AuthState.Authenticated -> RutasNavegacion.Home.route
        else -> RutasNavegacion.InicioSesion.route
    }


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(16.dp))

                NavigationDrawerItem(
                    label = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.TaskAlt,
                                contentDescription = "Tareas"
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Lista de tareas")
                        }
                    },
                    selected = currentRoute == "tareas",
                    onClick = {
                        navController.navigate("tareas") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )


                NavigationDrawerItem(
                    label = { Text("Conciertos") },
                    selected = currentRoute == "conciertos",
                    onClick = {
                        navController.navigate("conciertos") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )

                NavigationDrawerItem(
                    label = { Text("Configuración") },
                    selected = currentRoute == "configuracion",
                    onClick = {
                        navController.navigate("configuracion") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                        scope.launch { drawerState.close() }
                    }
                )

            }
        }
    ) {
        NavHost(navController = navController, startDestination = startDestination) {
            composable(RutasNavegacion.Home.route) {
                AplicacionPrincipal(navController, authViewModel, tareasViewModel)
            }
            composable(RutasNavegacion.Artistas.route) {
                ColeccionArtistas(artistasViewModel, navController)
            }
            composable(RutasNavegacion.Tareas.route) {
                Tareas(navController)
            }
            composable(RutasNavegacion.Artistas.route) {
                ColeccionArtistas(viewModel(), navController)
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
            composable(RutasNavegacion.SobreNosotros.route) {
                SobreNosotros(navController)
            }
            composable(RutasNavegacion.AcercaDe.route) {
                AcercaDe(navController)
            }
        }
    }
}