package com.example.aplicacionconciertos.ui.NavigationDrawer

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.aplicacionconciertos.R

@Composable
fun NavigationDrawer() {
    val items = listOf(
        NavigationItems(
            title = stringResource(id = R.string.MenuPrincipal),
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        NavigationItems(
            title = stringResource(id = R.string.TodosLosArtistas),
            selectedIcon = Icons.Filled.LibraryMusic,
            unselectedIcon = Icons.Outlined.LibraryMusic
        ),
        NavigationItems(
            title = stringResource(id = R.string.InicioSesion),
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        ),
        NavigationItems(
            title = stringResource(id = R.string.Registro),
            selectedIcon = Icons.Filled.PersonAdd,
            unselectedIcon = Icons.Outlined.PersonAdd
        ),
        NavigationItems(
            title = stringResource(id = R.string.ConfiguracionTituloBoton),
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
        ),
        NavigationItems(
            title = stringResource(id = R.string.SobreNosotrosTitulo),
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info
        ),
        NavigationItems(
            title = stringResource(id = R.string.AcercaDeTitulo),
            selectedIcon = Icons.Filled.QuestionMark,
            unselectedIcon = Icons.Outlined.QuestionMark
        )
    )
}

