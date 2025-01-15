package com.example.aplicacionconciertos.model

import AplicacionPrincipal
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ModalDrawerSheet


@Composable
fun AppNavigation(navController: NavHostController, authState: AuthState) {

    val authViewModel: AuthViewModel = viewModel()
    val viewModelArtistas: ViewModelArtistas = viewModel()
    val context = LocalContext.current
    val contenedor = remember { ContenedorMisTareas(context) }
    val tareasViewModel: TareasViewModel = viewModel {
        TareasViewModel(contenedor.repositorioMisTareas)
    }
    val drawerState = rememberDrawerState(initialValue = androidx.compose.material3.DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val tareasIncompletas by tareasViewModel.numeroTareasPendientes.collectAsState()

    val showBadge by remember { derivedStateOf { tareasIncompletas > 0 } }

    Log.d("AppNavigation", "Incomplete Task Count: $tareasIncompletas, Show Badge: $showBadge")

    val startDestination = when (authState) {
        is AuthState.Authenticated -> RutasNavegacion.Home.route
        else -> RutasNavegacion.InicioSesion.route
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                NavigationDrawerItem(
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Lista de tareas")
                            if (showBadge) {
                                Spacer(modifier = Modifier.width(4.dp))

                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(Color.Red)
                                        .padding(horizontal = 6.dp, vertical = 2.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = tareasIncompletas.toString(),
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    },
                    selected = currentRoute == "tareas",
                    onClick = {
                        navController.navigate("tareas")
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                )
                NavigationDrawerItem(
                    label = { Text("Conciertos") },
                    selected = currentRoute == "conciertos",
                    onClick = {
                        navController.navigate("conciertos")
                        scope.launch { drawerState.close() }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    ) {
        NavHost(navController = navController, startDestination = startDestination) {
            composable(RutasNavegacion.Home.route) {
                AplicacionPrincipal(navController, authViewModel, tareasViewModel)
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
}