package com.example.aplicacionconciertos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacionconciertos.model.tareas.ContenedorMisTareas
import com.example.aplicacionconciertos.model.tareas.MiTarea
import kotlinx.coroutines.launch


@Composable
fun Tareas(navController: NavController) {
    val context = LocalContext.current
    val contenedor = remember { ContenedorMisTareas(context) }
    val misTareas = remember { mutableStateListOf<MiTarea>() }

    LaunchedEffect(Unit) {
        launch {
            contenedor.repositorioMisTareas.obtenerTodasLasTareas().collect { tareas ->
                misTareas.clear()
                misTareas.addAll(tareas)
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(misTareas) { tarea ->
            TareaItem(tarea, contenedor)
        }
        item {
            CrearTarea(contenedor)
        }
    }
}

@Composable
fun TareaItem(tarea: MiTarea, contenedor: ContenedorMisTareas) {
    val scope = rememberCoroutineScope()

    Spacer(modifier = Modifier.height(60.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = tarea.titulo,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = tarea.descripcion,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                scope.launch {
                    contenedor.repositorioMisTareas.eliminarTarea(tarea)
                }
            }) {
                Text(stringResource(id = R.string.Borrar))
            }
        }
    }
}



@Composable
fun CrearTarea(contenedor: ContenedorMisTareas) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    Spacer(modifier = Modifier.height(60.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text(stringResource(id = R.string.Titulo)) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text(stringResource(id = R.string.Descripcion)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val nuevaTarea = MiTarea(titulo = titulo, descripcion = descripcion)
            scope.launch {
                contenedor.repositorioMisTareas.insertarTarea(nuevaTarea)
            }
            titulo = ""
            descripcion = ""
        }) {
            Text(stringResource(id = R.string.CrearTarea))
        }
    }
}