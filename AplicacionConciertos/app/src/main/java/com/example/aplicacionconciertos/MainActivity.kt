package com.example.aplicacionconciertos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.aplicacionconciertos.model.AppNavigation
import com.example.aplicacionconciertos.model.authentication.AuthRepository
import com.example.aplicacionconciertos.model.tareas.ContenedorMisTareas
import com.example.aplicacionconciertos.ui.NavigationDrawer
import com.example.aplicacionconciertos.ui.theme.AppConciertosTheme
import com.example.aplicacionconciertos.viewmodel.TareasViewModel
import com.example.aplicacionconciertos.viewmodel.activities.ViewModelActivities
import com.example.aplicacionconciertos.viewmodel.authentication.ViewModelAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current
            val contenedor = ContenedorMisTareas(context)
            val tareasViewModel = TareasViewModel(contenedor.repositorioMisTareas)

            val authViewModel: ViewModelAuth = viewModel { ViewModelAuth(AuthRepository(), context) }

            LaunchedEffect(authViewModel.authState.collectAsState().value) {
                authViewModel.refreshAndSaveToken()
            }

            AppConciertosTheme {
                NavigationDrawer(navController, tareasViewModel) {
                    AppNavigation(navController, authViewModel.authState.collectAsState().value, authViewModel)
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