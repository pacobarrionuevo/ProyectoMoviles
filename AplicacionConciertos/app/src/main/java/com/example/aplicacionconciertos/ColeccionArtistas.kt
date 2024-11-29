package com.example.aplicacionconciertos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.aplicacionconciertos.viewmodel.ViewModelArtistas

@Composable
fun ColeccionArtistas(navController: NavHostController, viewModel: ViewModelArtistas) {

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
                    Column {
                        AsyncImage(
                            model = artista.urlImagen,
                            contentDescription = artista.nombre,
                            modifier = Modifier.fillMaxWidth().height(200.dp).clip(
                                RoundedCornerShape(8.dp)
                            )
                        )
                    }
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