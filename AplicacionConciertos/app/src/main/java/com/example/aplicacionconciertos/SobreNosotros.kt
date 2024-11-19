package com.example.aplicacionconciertos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SobreNosotros(navController: NavController) {
    Column(
        modifier = androidx.compose.ui.Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.inversePrimary)
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (modifier = androidx.compose.ui.Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.onPrimaryContainer),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.SobreNosotrosTitulo),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.surface
            )

            Spacer(modifier = androidx.compose.ui.Modifier.height(20.dp))
        }
        Text (
            text = stringResource(id = R.string.SobreNosotrosText),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                navController.navigate("AplicacionPrincipal")
            }
        ) {
            Text(stringResource(id = R.string.Volver))

        }
    }

}


@Composable
fun SobreNosotrosScreen() {
    Text(text = "Bienvenidos a la sección SobreNosotros.")
}