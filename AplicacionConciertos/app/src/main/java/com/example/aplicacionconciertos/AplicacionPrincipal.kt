
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.example.aplicacionconciertos.R
import com.example.aplicacionconciertos.model.RutasNavegacion

@Composable
fun AplicacionPrincipal(navController: NavHostController) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.inversePrimary)
            .padding(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.onPrimaryContainer)
        ) {
            Text(
                text = stringResource(id = R.string.titulo_aplicacion),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.surface
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.concierto),
            contentDescription = stringResource(id = R.string.concierto),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .width(400.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(id = R.string.SobreNosotrosVistaPrincipal),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate(RutasNavegacion.SobreNosotros.route)
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )

        ) {
            Text(stringResource(id = R.string.SobreNosotrosTitulo))

        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate(RutasNavegacion.AcercaDe.route)
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(stringResource(id = R.string.AcercaDeTitulo))

        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate(RutasNavegacion.Configuracion.route)
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(stringResource(id = R.string.ConfiguracionTituloBoton))

        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                navController.navigate(RutasNavegacion.Artistas.route)
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(stringResource(id = R.string.TodosLosArtistas))

        }
        Button(
            onClick = {
                navController.navigate(RutasNavegacion.InicioSesion.route)
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(stringResource(id=R.string.InicioSesion))

        }


        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(stringResource(id = R.string.Salir))
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text(text = stringResource(id = R.string.ConfirmarSalida)) },
                text = { Text(text = stringResource(id = R.string.Salir)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            exitApp(context)
                        }
                    ) {
                        Text(stringResource(id = R.string.Aceptar))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                        }
                    ) {
                        Text(stringResource(id = R.string.Cancelar))
                    }
                }
            )
        }
    }
}

fun exitApp(context: Context) {
    ActivityCompat.finishAffinity(context as android.app.Activity)
}