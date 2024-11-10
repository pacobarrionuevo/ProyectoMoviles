package com.example.aplicacionconciertos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacionconciertos.ui.theme.AppConciertosTheme
import com.example.miapp.Configuracion

@Composable
fun AplicacionPrincipal(navController: NavController) {
    val pulsado by rememberSaveable { mutableStateOf(false) }

    //Aqui va a ir la vista inicial de la página
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row (modifier = Modifier
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
            Text(text = "Ir a Sobre Nosotros")

        }
        Button(
            onClick = {
                navController.navigate("AcercaDe")
            }
        ) {
            Text(text = "Ir a Acerca de")

        }
        Button(
            onClick = {
                navController.navigate("Configuracion")
            }
        ) {
            Text(text = "Ir a Configuración")

        }
    }
}

@Composable
fun ControladorNav() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") { AplicacionPrincipal(navController) }
        composable("sobre_nosotros") { SobreNosotros(navController) }
        composable("ACercaDe") { AcercaDe(navController) }
        composable("Configuracion") { Configuracion(navController) }
    }
}

