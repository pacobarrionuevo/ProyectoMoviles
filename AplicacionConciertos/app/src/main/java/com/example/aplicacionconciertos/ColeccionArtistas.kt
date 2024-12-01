package com.example.aplicacionconciertos

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
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

    LaunchedEffect(Unit) {
        viewModel.fetchArtistas()
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (artistas.isEmpty()) {
            Text(text = "Loading...")
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(artistas) { artista ->
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.inversePrimary)
                            .padding(20.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
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
                                .clip(MaterialTheme.shapes.medium
                            )
                        )

                        Column (
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.inversePrimary)
                                .padding(10.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "· ${stringResource(id = R.string.Id)}: ${artista.id}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "· ${stringResource(id = R.string.Nombre)}: ${artista.name}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "· ${stringResource(id = R.string.Generos)}: ${artista.genres.joinToString(", ")}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "· ${stringResource(id = R.string.anoNacimiento)}: ${artista.birth_year}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "· ${stringResource(id = R.string.PrecioEntrada)}: ${artista.precio_entrada}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "· ${stringResource(id = R.string.LugaresConcierto)}: ${artista.concert_locations.joinToString(", ")}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = "· ${stringResource(id = R.string.Discografia)}: ${artista.albums.joinToString(", ")}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                        }
                    }

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