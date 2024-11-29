package com.example.aplicacionconciertos.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import com.example.aplicacionconciertos.viewmodel.ViewModelArtistas

@Composable
fun ArtistScreen(viewModel: ViewModelArtistas) {

    val artistas = viewModel.artistas.observeAsState(emptyList())

    LaunchedEffect(Unit) {
        viewModel.fetchArtistas()
    }

    Column {
        if (artistas.value.isEmpty()) {

            Text(text = "Loading...")
        } else {
            LazyColumn {
                items(artistas.value) { artista ->
                    Text(text = "ID: ${artista.id}")
                    Text(text = "Nombre: ${artista.nombre}")
                    Text(text = "Géneros: ${artista.generosMusicales.joinToString(", ")}")
                    Text(text = "URL Imagen: ${artista.urlImagen}")
                    Text(text = "Año de nacimiento: ${artista.birthYear}")
                    Text(text = "Precio de entrada: ${artista.precioEntrada}")
                    Text(text = "Lugar de conciertos: ${artista.lugarConciertos.joinToString(", ")}")
                    HorizontalDivider()
                }
            }
        }
    }
}