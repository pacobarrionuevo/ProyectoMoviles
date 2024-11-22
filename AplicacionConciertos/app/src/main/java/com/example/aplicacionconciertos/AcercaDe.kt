package com.example.aplicacionconciertos
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun AcercaDe(navController: NavController) {
    //LazyColumn es para crear una columna en la cual se pueda scrollear hacia abajo
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.inversePrimary)
            .padding(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //item es para crear un elemento en la lazycolumn
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.onPrimaryContainer),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.AcercaDeTitulo),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.surface
                )
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
        item {
            Image(
                painter = painterResource(id = R.drawable.concierto2),
                contentDescription = stringResource(id = R.string.concierto),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .width(400.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            Text(
                text = stringResource(id = R.string.ACercaDeText),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        item {
            Image(
                painter = painterResource(id = R.drawable.copyright),
                contentDescription = stringResource(id = R.string.concierto),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.primaryContainer)
                    .width(150.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))

        }
        item {
            Button(
                onClick = {
                    navController.navigate("AplicacionPrincipal")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(stringResource(id = R.string.Volver))
            }
        }}}