package com.example.aplicacionconciertos

import AplicacionPrincipal
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacionconciertos.auth.InicioSesion
import com.example.aplicacionconciertos.auth.Registro
import com.example.aplicacionconciertos.model.RutasNavegacion
import com.example.aplicacionconciertos.ui.theme.AppConciertosTheme
import com.example.aplicacionconciertos.viewmodel.AuthViewModel
import com.example.aplicacionconciertos.viewmodel.ViewModelArtistas

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppConciertosTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = RutasNavegacion.Home.route) {
                        composable(RutasNavegacion.Home.route) { AplicacionPrincipal(navController, viewModel()) }
                        composable(RutasNavegacion.SobreNosotros.route) { SobreNosotros(navController) }
                        composable(RutasNavegacion.AcercaDe.route) { AcercaDe(navController) }
                        composable(RutasNavegacion.Configuracion.route) { Configuracion(navController) }
                        composable(RutasNavegacion.Artistas.route) { ColeccionArtistas(viewModel = ViewModelArtistas(), navController)   }
                        composable(RutasNavegacion.InicioSesion.route) { InicioSesion(navController) }
                        composable(RutasNavegacion.Registro.route) {Registro(viewModel(), navController) }
                    }
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


