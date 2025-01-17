package com.example.aplicacionconciertos.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Task
import androidx.compose.material3.Badge
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.aplicacionconciertos.R
import com.example.aplicacionconciertos.model.RutasNavegacion
import com.example.aplicacionconciertos.viewmodel.TareasViewModel
import kotlinx.coroutines.launch

data class NavigationItems(
    val title: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String,
    var badgeCount: Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    navController: NavHostController,
    tareasViewModel: TareasViewModel,
    content: @Composable (paddingValues: PaddingValues) -> Unit
) {
    val initialItems = listOf(
        NavigationItems(
            title = stringResource(id = R.string.MenuPrincipal),
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            route = RutasNavegacion.Home.route
        ),
        NavigationItems(
            title = stringResource(id = R.string.TodosLosArtistas),
            selectedIcon = Icons.Filled.LibraryMusic,
            unselectedIcon = Icons.Outlined.LibraryMusic,
            route = RutasNavegacion.Artistas.route
        ),
        NavigationItems(
            title = stringResource(id = R.string.InicioSesion),
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            route = RutasNavegacion.InicioSesion.route
        ),
        NavigationItems(
            title = stringResource(id = R.string.Registro),
            selectedIcon = Icons.Filled.PersonAdd,
            unselectedIcon = Icons.Outlined.PersonAdd,
            route = RutasNavegacion.Registro.route
        ),
        NavigationItems(
            title = stringResource(id = R.string.ConfiguracionTituloBoton),
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            route = RutasNavegacion.Configuracion.route
        ),
        NavigationItems(
            title = stringResource(id = R.string.SobreNosotrosTitulo),
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,
            route = RutasNavegacion.SobreNosotros.route
        ),
        NavigationItems(
            title = stringResource(id = R.string.AcercaDeTitulo),
            selectedIcon = Icons.Filled.QuestionMark,
            unselectedIcon = Icons.Outlined.QuestionMark,
            route = RutasNavegacion.AcercaDe.route
        ),
        NavigationItems(
            title = stringResource(id = R.string.Tareas),
            selectedIcon = Icons.Filled.Task,
            unselectedIcon = Icons.Outlined.Task,
            route = RutasNavegacion.Tareas.route
        )
    )

    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val incompleteTaskCount by tareasViewModel.numeroTareasPendientes.collectAsState(initial = 0)
    var items by remember { mutableStateOf(initialItems) }

    LaunchedEffect(incompleteTaskCount) {
        items = items.map { item ->
            if (item.route == RutasNavegacion.Tareas.route) {
                item.copy(badgeCount = if (incompleteTaskCount > 0) incompleteTaskCount else null)
            } else {
                item
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = index == selectedItemIndex,
                        onClick = {
                            selectedItemIndex = index
                            navController.navigate(item.route) {
                                launchSingleTop = true
                                restoreState = true
                            }
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        badge = {
                            item.badgeCount?.let {
                                Badge {
                                    Text(text = it.toString())
                                }
                            }
                        },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.titulo_aplicacion)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            content(paddingValues)
        }
    }
}