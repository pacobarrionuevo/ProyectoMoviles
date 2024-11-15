package com.example.miapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.aplicacionconciertos.R

@Composable
fun Configuracion(navController: NavController) {
    var notificaciones by remember { mutableStateOf(true) }
    var temaOscuro by remember { mutableStateOf(false) }
    var CantanteFavorito by remember { mutableStateOf("Kendrick Lamar") }
    var unidadMedida by remember { mutableStateOf("Metrico") }
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.inversePrimary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.settings),
            contentDescription = stringResource(id = R.string.ConfiguracionTituloBoton),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(250.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.ConfiguracionTituloBoton),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.surface
        )


        Spacer(modifier = Modifier.height(40.dp))

        SeccionCheckbox(
            titulo = "Notificaciones",
            checked = notificaciones,
            onCheckedChange = { notificaciones = it }
        )


        SeccionSwitch(
            titulo = "Tema Oscuro",
            checked = temaOscuro,
            onCheckedChange = { temaOscuro = it }
        )


        SeccionRadioButton(
            titulo = "Cantante Favorito",
            opciones = listOf("Kendrick Lamar", "Ed Sheeran", "Taylor Swift"),
            seleccionado = CantanteFavorito,
            onSeleccionChange = { CantanteFavorito = it }
        )

        Column {
            Text(text = "Unidad de Medida")

            Button(onClick = { expanded = true }) {
                Text(text = unidadMedida)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf("Metrico", "Imperial").forEach { seleccion ->
                    DropdownMenuItem(
                        onClick = {
                            unidadMedida = seleccion
                            expanded = false
                        },

                        text = { Text(text = seleccion) }
                    )
                }
            }
            Button(
                onClick = {
                    navController.navigate("home")
                }
            ) {
                Text(stringResource(id = R.string.Volver))

            }
        }}}


@Composable
fun SeccionCheckbox(titulo: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = titulo)
    }
}

@Composable
fun SeccionSwitch(titulo: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Switch(checked = checked, onCheckedChange = onCheckedChange)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = titulo)
    }
}

@Composable
fun SeccionRadioButton(
    titulo: String,
    opciones: List<String>,
    seleccionado: String,
    onSeleccionChange: (String) -> Unit
) {
    Column {
        Text(text = titulo)
        opciones.forEach { opcion ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (opcion == seleccionado),
                    onClick = { onSeleccionChange(opcion) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = opcion)
            }
        }
    }
}