package com.example.aplicacionconciertos

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacionconciertos.datos.ConfigurationDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun Configuracion(navController: NavController) {
    val generos = listOf(
        stringResource(id = R.string.GeneroMusical1),
        stringResource(id = R.string.GeneroMusical2),
        stringResource(id = R.string.GeneroMusical3),
        stringResource(id = R.string.GeneroMusical4)
    )

    val generosSeleccionados = remember { mutableStateMapOf<String, Boolean>() }
    val epocas = listOf(
        R.string.years80,
        R.string.years90,
        R.string.years2000,
        R.string.years2010,
        R.string.years2020
    ).map { stringResource(id = it) }
    var temaOscuro by remember { mutableStateOf(false) }
    var cantanteFavorito by remember { mutableStateOf("Kendrick Lamar") }
    var epocaFavorita by remember { mutableStateOf("80s") }

    var expanded by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = ConfigurationDataStore(context)

    LaunchedEffect(Unit) {
        temaOscuro = dataStore.getDarkTheme.first()
        cantanteFavorito = dataStore.getFavoriteSinger.first() ?: "Kendrick Lamar"
        epocaFavorita = dataStore.getFavoriteEpoch.first() ?: "80s"

        val generosGuardados = dataStore.getSelectedGenres.first()
        generos.forEach { genero ->
            generosSeleccionados[genero] = generosGuardados.contains(genero)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.inversePrimary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
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

            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            Text(text = stringResource(id = R.string.GenerosMusicales))

            generos.forEach { genero ->
                if (generosSeleccionados[genero] == null) {
                    generosSeleccionados[genero] = false
                }
            }

            Column {
                generos.forEach { genero ->
                    SeccionCheckbox(
                        titulo = genero,
                        checked = generosSeleccionados[genero] ?: false,
                        onCheckedChange = { isChecked ->
                            generosSeleccionados[genero] = isChecked
                        }
                    )
                }
            }

            SeccionRadioButton(
                titulo = stringResource(id = R.string.CantanteFavorito),
                opciones = listOf(
                    stringResource(id = R.string.ArtistaFav1),
                    stringResource(id = R.string.ArtistaFav2),
                    stringResource(id = R.string.ArtistaFav3),
                    stringResource(id = R.string.ArtistaFav4)
                ),
                seleccionado = cantanteFavorito,
                onSeleccionChange = { singer ->
                    cantanteFavorito = singer
                }

            )
        }
        item {
            SeccionSwitch(
                titulo = stringResource(id = R.string.TemaOscuro),
                checked = temaOscuro,
                onCheckedChange = { isChecked ->
                    temaOscuro = isChecked
                }
            )

            Column {
                Text(text = stringResource(id = R.string.epocaFavorita))

                Button(onClick = { expanded = true }) {
                    Text(text = epocaFavorita)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    epocas.forEach { seleccion ->
                        DropdownMenuItem(
                            onClick = {
                                epocaFavorita = seleccion
                                expanded = false
                            },
                            text = { Text(text = seleccion) }
                        )
                    }
                }
            }

            Button(
                onClick = {
                    scope.launch {
                        var cambios = 0
                        if (epocaFavorita != dataStore.getFavoriteEpoch.first()) {
                            dataStore.saveFavoriteEpoch(epocaFavorita)
                            cambios++
                        }
                        if (cantanteFavorito != dataStore.getFavoriteSinger.first()) {
                            dataStore.saveFavoriteSinger(cantanteFavorito)
                            cambios++
                        }
                        if (temaOscuro != dataStore.getDarkTheme.first()) {
                            dataStore.saveDarkTheme(temaOscuro)
                            cambios++
                        }

                        val generosSeleccionadosList = generosSeleccionados.filter { it.value }.keys.toList()
                        val generosGuardados = dataStore.getSelectedGenres.first()
                        if (generosSeleccionadosList != generosGuardados) {
                            dataStore.saveSelectedGenres(generosSeleccionadosList)
                            cambios++
                        }

                        val text = "Guardando..."
                        val duration = if (cambios < 2) {
                            Toast.LENGTH_SHORT
                        } else {
                            Toast.LENGTH_LONG
                        }

                        Toast.makeText(context, text, duration).show()

                        navController.navigate("home")
                    }
                }
            ) {
                Text(stringResource(id = R.string.Guardar))
            }

        }
        item {
            Button(
                onClick = {
                    navController.navigate("home")
                }
            ) {
                Text(stringResource(id = R.string.Volver))
            }
        }
    }
}

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
