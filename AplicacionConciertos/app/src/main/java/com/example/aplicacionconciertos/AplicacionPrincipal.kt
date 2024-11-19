package com.example.aplicacionconciertos

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacionconciertos.ui.theme.AppConciertosTheme


@Composable
fun AplicacionPrincipal(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.inversePrimary)
            .padding(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.onPrimaryContainer)
        ) {
            Text(
                text = stringResource(id = R.string.titulo_aplicacion),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.surface
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.concierto),
            contentDescription = stringResource(id = R.string.concierto),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .width(400.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.SobreNosotrosVistaPrincipal),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate("sobre_nosotros")
            }
        ) {
            Text(stringResource(id = R.string.SobreNosotrosTitulo))

        }
        Button(
            onClick = {
                navController.navigate("AcercaDe")
            }
        ) {
            Text(stringResource(id = R.string.AcercaDeTitulo))

        }
        Button(
            onClick = {
                navController.navigate("Configuracion")
            }
        ) {
            Text(stringResource(id = R.string.ConfiguracionTituloBoton))

        }
        Button(
            onClick = { showDialog = true }
        ) {
            Text(stringResource(id = R.string.Salir))
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = stringResource(id = R.string.ConfirmarSalida)) },
                text = { Text(text = stringResource(id = R.string.Salir)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            exitApp(context) // Llamar a la función exitApp
                        }
                    ) {
                        Text(stringResource(id = R.string.Aceptar))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text(stringResource(id = R.string.Cancelar))
                    }
                }
            )
        }
    }
}

// Función para salir de la aplicación
fun exitApp(context: Context) {
    ActivityCompat.finishAffinity(context as android.app.Activity)
}

@Composable
fun ControladorNav() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") { AplicacionPrincipal(navController) }
        composable("sobre_nosotros") { SobreNosotros(navController) }
        composable("AcercaDe") { AcercaDe(navController) }
        composable("Configuracion") { Configuracion(navController) }
    }
}