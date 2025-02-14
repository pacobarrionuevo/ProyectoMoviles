package com.example.aplicacionconciertos

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aplicacionconciertos.model.activities.ActivityResponse
import com.example.aplicacionconciertos.model.activities.ParticipationResponse
import com.example.aplicacionconciertos.viewmodel.activities.ViewModelActivities
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActividadesScreen(navController: NavController, viewModel: ViewModelActivities = viewModel()) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = {10}
    )
    val snackbarHostState = remember { SnackbarHostState() }

    val actividades by viewModel.activities.collectAsState()
    val userActividades by viewModel.userActivities.collectAsState()

    val (loading, setLoading) = remember { mutableStateOf(true) }

    // Comprobar autenticación
    LaunchedEffect(Unit) {
        viewModel.loadCredentials()
        delay(500)  // Simulación de carga
        if (viewModel.accessToken.isNullOrEmpty()) {
            navController.navigate("login")
        } else {
            viewModel.getAllActivities()
            viewModel.getUserActivities()
            setLoading(false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Actividades") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
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

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> ActividadesLista(
                        actividades = actividades,
                        loading = loading,
                        onParticipate = { activityId ->
                            viewModel.createParticipation(activityId)
                            scope.launch {
                                snackbarHostState.showSnackbar("Te has apuntado a la actividad")
                            }
                        }
                    )
                    1 -> MisActividadesLista(
                        actividades = userActividades,
                        loading = loading,
                        onRemove = { participationId ->
                            viewModel.deleteParticipation(participationId)
                            scope.launch {
                                snackbarHostState.showSnackbar("Te has borrado de la actividad")
                            }
                        }
                    )
                }
            }


        }
    }
}

// Lista de todas las actividades con botón para apuntarse
@Composable
fun ActividadesLista(actividades: List<ActivityResponse>, loading: Boolean, onParticipate: (Long) -> Unit) {
    if (loading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        LazyColumn {
            items(actividades) { actividad ->
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
                        Column {
                            Text(actividad.name)
                            Text(actividad.description)
                        }
                        IconButton(onClick = { onParticipate(actividad.id) }) {
                            Icon(Icons.Filled.Check, contentDescription = "Apuntarse")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MisActividadesLista(actividades: List<ParticipationResponse>, loading: Boolean, onRemove: (Long) -> Unit) {
    if (loading) {
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else {
        LazyColumn {
            items(actividades, key = { it.id }) { actividad ->

                var visible by remember { mutableStateOf(true) }

                AnimatedVisibility(
                    visible = visible,
                    exit = fadeOut()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("actividad")
                            }
                            IconButton(onClick = {
                                visible = false
                                onRemove(actividad.id)
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
