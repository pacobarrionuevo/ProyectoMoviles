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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacionconciertos.model.tareas.ContenedorMisTareas
import com.example.aplicacionconciertos.model.tareas.MiTarea
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Tareas(navController: NavController) {
    val context = LocalContext.current
    val contenedor = remember { ContenedorMisTareas(context) }
    val misTareas = remember { mutableStateListOf<MiTarea>() }

    // Lanzar una corrutina para obtener las tareas del repositorio
    LaunchedEffect(Unit) {
        launch {
            contenedor.repositorioMisTareas.obtenerTodasLasTareas().collect { tareas ->
                misTareas.clear()
                misTareas.addAll(tareas)
            }
        }
    }

    // Mostrar la lista de tareas en un LazyColumn
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
    val scope = rememberCoroutineScope() // Coroutine scope for delete operation

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { /* Acción al hacer clic en la tarea */ },
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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = tarea.completada,
                    onCheckedChange = { /* Acción al cambiar el estado de la tarea */ }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (tarea.completada) "Completada" else "Pendiente",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(onClick = {
                scope.launch {
                    contenedor.repositorioMisTareas.eliminarTarea(tarea)
                }
            }) {
                Text("Borrar")
            }
        }
    }
}



@Composable
fun CrearTarea(contenedor: ContenedorMisTareas) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope() // Coroutine scope for create operation

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") }
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
            Text("Crear Tarea")
        }
    }
}