package com.example.aplicacionconciertos

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
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
    var temaOscuro by remember { mutableStateOf(false) }
    var cantanteFavorito by remember { mutableStateOf("Kendrick Lamar") }
    var epocaFavorita by remember { mutableStateOf("80s") }
    val epocas = listOf(
        R.string.years80,
        R.string.years90,
        R.string.years2000,
        R.string.years2010,
        R.string.years2020
    ).map { stringResource(id = it) }
    var expanded by remember { mutableStateOf(false) }

    // Obtener el contexto y el lifecycleOwner
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Crear una instancia de AjustesConfiguracion
    val ajustesConfiguracion = remember { AjustesConfiguracion(context) }

    // a coroutine scope
    val scope = rememberCoroutineScope()

    // Lanzar un coroutine para observar los cambios en las preferencias
    LaunchedEffect(Unit) {
        ajustesConfiguracion.getDarkTheme.collect { isDarkTheme ->
            temaOscuro = isDarkTheme
        }
        ajustesConfiguracion.getFavoriteSinger.collect { singer ->
            cantanteFavorito = singer ?: "Kendrick Lamar"
        }
        ajustesConfiguracion.getFavoriteEpoch.collect { epoch ->
            epocaFavorita = epoch ?: "80s"
        }
    }

    // Observar los eventos del ciclo de vida para guardar las preferencias (opcional)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                scope.launch {
                    ajustesConfiguracion.saveDarkTheme(temaOscuro)
                    ajustesConfiguracion.saveFavoriteSinger(cantanteFavorito)
                    ajustesConfiguracion.saveFavoriteEpoch(epocaFavorita)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
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
                    scope.launch {
                        ajustesConfiguracion.saveFavoriteSinger(singer)
                    }
                }

            )
        }
        item {
            SeccionSwitch(
                titulo = stringResource(id = R.string.TemaOscuro),
                checked = temaOscuro,
                onCheckedChange = { isChecked ->
                    temaOscuro = isChecked
                    scope.launch {
                        ajustesConfiguracion.saveDarkTheme(isChecked)
                    }
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
                                scope.launch {
                                    ajustesConfiguracion.saveFavoriteEpoch(seleccion)
                                }
                            },
                            text = { Text(text = seleccion) }
                        )
                    }
                }
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
