package com.example.aplicacionconciertos

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aplicacionconciertos.model.activities.ActivityResponse
import com.example.aplicacionconciertos.viewmodel.activities.ViewModelActivities
import com.example.aplicacionconciertos.viewmodel.authentication.DataStoreManager
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesScreen(viewModel: ViewModelActivities = viewModel(), navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var accessToken by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        accessToken = DataStoreManager.getAccessTokenSync(context)
        if (accessToken.isNullOrEmpty()) {
            Toast.makeText(context, "Debes iniciar sesión", Toast.LENGTH_LONG).show()
            return@LaunchedEffect
        }
    }
    
    val tabItems = listOf(
        TabItem("Todas", Icons.Default.EventAvailable),
        TabItem("Apuntado", Icons.Default.EventBusy)
    )
    var selectedTabIndex by remember { mutableStateOf(0) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text("Actividades") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabItems.forEachIndexed { index, tabItem ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(tabItem.title) },
                        icon = { Icon(tabItem.icon, contentDescription = null) }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> AllActivitiesTab(viewModel, snackbarHostState)
                1 -> UserActivitiesTab(viewModel, snackbarHostState)
            }
        }
    }
}

@Composable
fun AllActivitiesTab(viewModel: ViewModelActivities, snackbarHostState: SnackbarHostState) {
    val activities by viewModel.activities.collectAsState()
    val isLoading by remember { derivedStateOf { activities.isEmpty() } }

    LaunchedEffect(Unit) {
        viewModel.getAllActivities()
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    } else {
        LazyColumn {
            items(activities.size) { index ->
                ActivityItem(
                    activity = activities[index],
                    buttonIcon = Icons.Default.Add,
                    buttonAction = {
                        viewModel.createParticipation("userId", activities[index].id)
                    },
                    snackbarHostState = snackbarHostState,
                    message = "Te has apuntado"
                )
            }
        }
    }
}

@Composable
fun UserActivitiesTab(viewModel: ViewModelActivities, snackbarHostState: SnackbarHostState) {
    val userActivities by viewModel.userParticipations.collectAsState()
    val allActivities by viewModel.activities.collectAsState()
    val isLoading by remember { derivedStateOf { userActivities.isEmpty() } }
    var visibleActivities by remember { mutableStateOf(userActivities) }

    LaunchedEffect(Unit) {
        viewModel.getUserParticipations("userId")
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    } else {
        LazyColumn {
            items(userActivities.size) { index ->
                val participation = userActivities[index]
                val activity = allActivities.find { it.id == participation.activityId }

                if (activity != null) {
                    AnimatedVisibility(
                        visible = participation in visibleActivities,
                        exit = slideOutHorizontally { it } + fadeOut()
                    ) {
                        ActivityItem(
                            activity = activity,
                            buttonIcon = Icons.Default.EventBusy,
                            buttonAction = {
                                viewModel.deleteParticipation(participation.id, "userId")
                                visibleActivities = visibleActivities - participation
                            },
                            snackbarHostState = snackbarHostState,
                            message = "Te has borrado de la actividad"
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun ActivityItem(
    activity: ActivityResponse,
    buttonIcon: androidx.compose.ui.graphics.vector.ImageVector,
    buttonAction: () -> Unit,
    snackbarHostState: SnackbarHostState,
    message: String
) {
    val scope = rememberCoroutineScope()

    Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(activity.name)
            IconButton(onClick = {
                buttonAction()
                scope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }) {
                Icon(buttonIcon, contentDescription = null)
            }
        }
    }
}

data class TabItem(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
