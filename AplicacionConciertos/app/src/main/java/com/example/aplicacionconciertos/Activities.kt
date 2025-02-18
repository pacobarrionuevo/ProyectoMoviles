package com.example.aplicacionconciertos

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aplicacionconciertos.model.activities.ActivitiesRepository
import com.example.aplicacionconciertos.model.activities.RetrofitInstance
import com.example.aplicacionconciertos.viewmodel.activities.ViewModelActivities
import kotlinx.coroutines.launch
import com.example.aplicacionconciertos.model.activities.CreateActivityDialog
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActividadesScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Repositorio de actividades
    val activitiesRepository = remember { ActivitiesRepository(RetrofitInstance.api) }
    // ViewModel
    val viewModel: ViewModelActivities = viewModel()

    // Estados
    val actividades by viewModel.activities.collectAsState()
    val userActividades by viewModel.userActivities.collectAsState()
    var loading by remember { mutableStateOf(true) }
    var showCreateDialog by remember { mutableStateOf(false) }

    // Cargar credenciales y datos al iniciar la pantalla
    LaunchedEffect(Unit) {
        viewModel.loadCredentials(context)
        viewModel.getAllActivities(activitiesRepository)
        viewModel.getUserActivities(activitiesRepository)
        loading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Actividades") })
        },
        floatingActionButton = {
            // Mostrar FAB en la pestaña "Todas" para crear actividad
            if (pagerState.currentPage == 0) {
                FloatingActionButton(onClick = { showCreateDialog = true }) {
                    Icon(Icons.Filled.Add, contentDescription = "Crear actividad")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // Pestañas
            val tabs = listOf(
                TabItem("Todas", Icons.Filled.List),
                TabItem("Mis Actividades", Icons.Filled.Person)
            )

            TabRow(selectedTabIndex = pagerState.currentPage) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = { Text(tab.title) },
                        icon = { Icon(tab.icon, contentDescription = tab.title) }
                    )
                }
            }

            // Contenido según pestaña
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> AllActivitiesList(
                        actividades = actividades,
                        loading = loading,
                        onParticipate = { activityId ->
                            scope.launch {
                                viewModel.createParticipation(activitiesRepository, activityId)
                                snackbarHostState.showSnackbar("Te has apuntado a la actividad")
                            }
                        },
                        onDeleteActivity = { activityId ->
                            scope.launch {
                                viewModel.deleteActivity(activitiesRepository, activityId)
                                snackbarHostState.showSnackbar("Actividad borrada")
                            }
                        }
                    )
                    1 -> MyActivitiesList(
                        actividades = userActividades,
                        loading = loading,
                        onRemove = { participationId ->
                            scope.launch {
                                viewModel.deleteParticipation(activitiesRepository, participationId)
                                snackbarHostState.showSnackbar("Te has borrado de la actividad")
                            }
                        }
                    )
                }
            }
        }
    }

    // Diálogo para crear una nueva actividad
    if (showCreateDialog) {
        CreateActivityDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { name, description, date, place, category ->
                viewModel.createActivity(activitiesRepository, name, description, date, place, category)
                showCreateDialog = false
                scope.launch {
                    snackbarHostState.showSnackbar("Actividad creada con éxito")
                }
            }
        )
    }
}

@Composable
fun AllActivitiesList(
    actividades: List<com.example.aplicacionconciertos.model.activities.ActivityResponse>,
    loading: Boolean,
    onParticipate: (Long) -> Unit,
    onDeleteActivity: (Long) -> Unit
) {
    if (loading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        LazyColumn {
            items(actividades, key = { it.id }) { actividad ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(actividad.name, style = MaterialTheme.typography.titleMedium)
                            Text(actividad.description, style = MaterialTheme.typography.bodyMedium)
                        }
                        // Botón para apuntarse
                        IconButton(onClick = { onParticipate(actividad.id) }) {
                            Icon(Icons.Filled.Check, contentDescription = "Apuntarse")
                        }
                        // Botón para borrar la actividad
                        IconButton(onClick = { onDeleteActivity(actividad.id) }) {
                            Icon(Icons.Filled.Close, contentDescription = "Borrar actividad")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun MyActivitiesList(
    actividades: List<com.example.aplicacionconciertos.model.activities.ParticipationResponse>,
    loading: Boolean,
    onRemove: (Long) -> Unit
) {
    if (loading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        LazyColumn {
            items(actividades, key = { it.id }) { participacion ->
                var visible by remember { mutableStateOf(true) }
                AnimatedVisibility(
                    visible = visible,
                    exit = fadeOut()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Actividad ID: ${participacion.activityId}")
                            IconButton(onClick = {
                                visible = false
                                onRemove(participacion.id)
                            }) {
                                Icon(Icons.Filled.Close, contentDescription = "Borrarse")
                            }
                        }
                    }
                }
            }
        }
    }
}

data class TabItem(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
