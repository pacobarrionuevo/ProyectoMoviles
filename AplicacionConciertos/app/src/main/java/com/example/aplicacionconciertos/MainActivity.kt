package com.example.aplicacionconciertos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.aplicacionconciertos.model.AppNavigation
import com.example.aplicacionconciertos.model.tareas.ContenedorMisTareas
import com.example.aplicacionconciertos.ui.NavigationDrawer
import com.example.aplicacionconciertos.ui.theme.AppConciertosTheme
import com.example.aplicacionconciertos.viewmodel.AuthState
import com.example.aplicacionconciertos.viewmodel.AuthViewModel
import com.example.aplicacionconciertos.viewmodel.TareasViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val authViewModel = AuthViewModel()
            val context = LocalContext.current
            val contenedor = ContenedorMisTareas(context)
            val tareasViewModel = TareasViewModel(contenedor.repositorioMisTareas)
            val authState by authViewModel.authState.observeAsState(initial = AuthState.Unauthenticated)
            AppConciertosTheme {
                NavigationDrawer(navController, tareasViewModel) {
                    AppNavigation(navController, authState)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VistaPreviaModoClaro() {
    AppConciertosTheme(darkTheme = false) {
        SobreNosotros(navController = rememberNavController())
    }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun VistaPreviaModoOscuro() {
    AppConciertosTheme(darkTheme = true) {
        SobreNosotros(navController = rememberNavController())
    }
}