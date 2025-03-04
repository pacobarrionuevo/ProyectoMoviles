package com.example.aplicacionconciertos

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aplicacionconciertos.model.activities.ActivitiesClient
import com.example.aplicacionconciertos.model.activities.ActivitiesRepository
import com.example.aplicacionconciertos.model.activities.ActivityResponse
import com.example.aplicacionconciertos.model.activities.RetrofitInstance
import com.example.aplicacionconciertos.viewmodel.activities.ViewModelActivities
import com.example.aplicacionconciertos.viewmodel.authentication.AuthState
import com.example.aplicacionconciertos.viewmodel.authentication.DataStoreManager
import com.example.aplicacionconciertos.viewmodel.authentication.ViewModelAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesScreen(navController: NavController, viewModelAuth: ViewModelAuth) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val activitiesClient = RetrofitInstance.api
    val repository = ActivitiesRepository(activitiesClient)

    val actividadesViewModel = remember { ViewModelActivities(repository, viewModelAuth, context) }

    val authState by viewModelAuth.authState.collectAsState()

    val userDetails by viewModelAuth.userDetails.collectAsState()

    LaunchedEffect(authState) {
        Log.d("ActivitiesScreen", "LaunchedEffect authState: $authState")
        when (authState) {
            is AuthState.Authenticated -> {
                Log.d("ActivitiesScreen", "Usuario autenticado. Obteniendo detalles del usuario...")
                viewModelAuth.getUserDetails()
            }
            is AuthState.SignedOut, is AuthState.Error, AuthState.Idle -> {
                Log.w("ActivitiesScreen", "Estado de autenticación inválido: $authState. Navegando a Login")
                Toast.makeText(context, "Debes iniciar sesión", Toast.LENGTH_LONG).show()
                navController.navigate("Login")
            }
            else -> {
                Log.d("ActivitiesScreen", "Estado de autenticación desconocido: $authState")
            }
        }
    }

    val userId = userDetails?.id

    val tabItems = listOf(
        TabItem(stringResource(id = R.string.TodosConciertos), Icons.Default.EventAvailable),
        TabItem(stringResource(id = R.string.MisConciertos), Icons.Default.EventBusy)
    )
    var selectedTabIndex by remember { mutableStateOf(0) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.Conciertos)) })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
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
                0 -> {
                    if (userId != null) {
                        AllActivitiesTab(
                            viewModel = actividadesViewModel,
                            snackbarHostState = snackbarHostState,
                            userId = userId
                        )
                    } else {
                        Text(stringResource(id = R.string.Cargando))
                    }
                }
                1 -> {
                    if (userId != null) {
                        UserActivitiesTab(
                            viewModel = actividadesViewModel,
                            snackbarHostState = snackbarHostState,
                            userId = userId
                        )
                    } else {
                        Text(stringResource(id = R.string.Cargando))
                    }
                }
            }

        }
    }
}



@Composable
fun AllActivitiesTab(viewModel: ViewModelActivities, snackbarHostState: SnackbarHostState, userId: String) {
    val context = LocalContext.current
    val activities by viewModel.activities.collectAsState()
    val isLoading = activities.isEmpty()

    val currentToken by DataStoreManager.getAccessToken(context).collectAsState(initial = null)

    LaunchedEffect(Unit) {

        viewModel.getAllActivities(accessToken = currentToken.toString())
        Log.d("ViewModelActivities", "Llamando getAllActivities con token: '$currentToken'")
        Log.d("ViewModelActivities", "Recibidas ${viewModel.getAllActivities(accessToken = currentToken.toString())} actividades")
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
                        viewModel.createParticipation(userId, activities[index].id, currentToken.toString())
                    },
                    snackbarHostState = snackbarHostState,
                    message = stringResource(id = R.string.ConciertoAñadido)
                )
            }
        }
    }
}

@Composable
fun UserActivitiesTab(viewModel: ViewModelActivities, snackbarHostState: SnackbarHostState, userId: String) {
    val userActivities by viewModel.userParticipations.collectAsState()
    val allActivities by viewModel.activities.collectAsState()
    val isLoading by remember { derivedStateOf { userActivities.isEmpty() } }
    var visibleActivities by remember { mutableStateOf(userActivities) }
    val context = LocalContext.current
    val currentToken by DataStoreManager.getAccessToken(context).collectAsState(initial = null)

    LaunchedEffect(Unit) {
        viewModel.getUserParticipations(userId, currentToken.toString())
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
    } else {
        LazyColumn {
            items(userActivities.size) { index ->
                val participation = userActivities[index]
                val activity = allActivities.find { it.id == participation.activityId }

                if (activity != null) {
                    val isVisible by remember { derivedStateOf { visibleActivities.contains(participation) } }
                    AnimatedVisibility(
                        visible = isVisible,
                        exit = slideOutHorizontally { it } + fadeOut()
                    ) {
                        ActivityItem(
                            activity = activity,
                            buttonIcon = Icons.Default.EventBusy,
                            buttonAction = {
                                viewModel.deleteParticipation(participation.id, userId, currentToken.toString())
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
