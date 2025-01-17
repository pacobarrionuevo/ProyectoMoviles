package com.example.aplicacionconciertos

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aplicacionconciertos.model.tareas.ContenedorMisTareas
import com.example.aplicacionconciertos.model.tareas.MiTarea
import com.example.aplicacionconciertos.viewmodel.TareasViewModel
import kotlinx.coroutines.launch

@Composable
fun Tareas(navController: NavController) {
    val context = LocalContext.current
    val contenedor = remember { ContenedorMisTareas(context) }
    val viewModel: TareasViewModel = viewModel {
        TareasViewModel(contenedor.repositorioMisTareas)
    }
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
            .padding(top = 56.dp)
    ) {
        items(misTareas, key = { tarea -> tarea.id }) { tarea ->
            TareaItem(tarea, viewModel)
        }
        item {
            CrearTarea(viewModel)
        }
    }
}

@Composable
fun TareaItem(tarea: MiTarea, viewModel: TareasViewModel) {
    val scope = rememberCoroutineScope()
    var checkedState by remember { mutableStateOf(tarea.completada) }

    Spacer(modifier = Modifier.width(30.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checkedState,
                onCheckedChange = { isChecked ->
                    checkedState = isChecked
                    viewModel.actualizarTarea(tarea.copy(completada = isChecked))
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        checkedState = !checkedState
                        viewModel.actualizarTarea(tarea.copy(completada = checkedState))
                    }
            ) {
                Text(
                    text = tarea.titulo,
                    style = if (checkedState) MaterialTheme.typography.titleMedium.copy(textDecoration = TextDecoration.LineThrough)
                    else MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = tarea.descripcion,
                    style = if (checkedState) MaterialTheme.typography.bodyMedium.copy(textDecoration = TextDecoration.LineThrough)
                    else MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            IconButton(onClick = {
                scope.launch {
                    viewModel.eliminarTarea(tarea)
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.BorrarTarea)
                )
            }
        }
    }
}

@Composable
fun CrearTarea(viewModel: TareasViewModel) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

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
            label = { Text(stringResource(id = R.string.Descripcion))}
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val nuevaTarea = MiTarea(titulo = titulo, descripcion = descripcion)
            viewModel.insertarTarea(nuevaTarea)
            titulo = ""
            descripcion = ""
        }) {
            Text(stringResource(id = R.string.CrearTarea))
        }
    }
}
