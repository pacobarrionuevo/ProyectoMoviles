package com.example.aplicacionconciertos

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.aplicacionconciertos.model.RutasNavegacion
import com.example.aplicacionconciertos.viewmodel.ViewModelArtistas

@Composable
fun ColeccionArtistas(viewModel: ViewModelArtistas, navController: NavHostController) {

    val artistas by viewModel.artistas.observeAsState(emptyList())
    Log.d("ColeccionArtistas", "Artistas observados: ${artistas}")

    LaunchedEffect(Unit) {
        viewModel.fetchArtistas()
        Log.d("ColeccionArtistas", "Artistas cargados: ${artistas.size}")
    }

    Column {
        if (artistas.isEmpty()) {
            //Progreso circular
            Text(text = "Loading...")
        } else {
            LazyColumn {
                items(artistas) { artista ->
                    Column {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(artista.image_url.replace("http://", "https://"))
                                .crossfade(true)
                                .build(),
                            contentDescription = artista.name,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(8.dp)
                            )
                        )
                    }
                    Text(text = "ID: ${artista.id}")
                    Text(text = "Géneros: ${artista.genres.joinToString(", ")}")
                    Text(text = "Año de nacimiento: ${artista.birth_year}")
                    Text(text = "Precio de entrada: ${artista.precio_entrada}")
                    Text(text = "Lugar de conciertos: ${artista.concert_locations.joinToString(", ")}")
                    Text(text = "Álbumes: ${artista.albums.joinToString(", ")}")
                    HorizontalDivider()
                }
            }
        }



        Button(
            onClick = {
                navController.navigate(RutasNavegacion.Home.route )
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )

        ) {
            Text(stringResource(id = R.string.Volver))
        }
    }
}