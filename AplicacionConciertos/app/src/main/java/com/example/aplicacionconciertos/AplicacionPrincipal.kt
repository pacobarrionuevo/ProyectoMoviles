import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.aplicacionconciertos.viewmodel.authentication.AuthState
import com.example.aplicacionconciertos.viewmodel.authentication.ViewModelAuth
import com.example.aplicacionconciertos.viewmodel.TareasViewModel
import com.example.aplicacionconciertos.viewmodel.activities.ViewModelActivities
import com.example.aplicacionconciertos.viewmodel.authentication.DataStoreManager
import com.example.aplicacionconciertos.viewmodel.authentication.dataStoreAuth
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun AplicacionPrincipal(
    navController: NavHostController,
    authViewModel: ViewModelAuth,
    tareasViewModel: TareasViewModel
) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()
    val userDetails by authViewModel.userDetails.collectAsState()

    val refreshToken by DataStoreManager.getRefreshToken(context).collectAsState(initial = null)

    LaunchedEffect(refreshToken) {
        Log.d("TokenRefresh", "Ejecutando LaunchedEffect con refreshToken: $refreshToken")
        if (!refreshToken.isNullOrEmpty()) {
            authViewModel.refreshAndSaveToken()
            authViewModel.getUserDetails()
        }
    }


    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.inversePrimary)
                .padding(paddingValues)
                .padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                modifier = Modifier
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


            Text(
                text = if (authState is AuthState.Authenticated) {
                    "${stringResource(id = R.string.BienvenidaAppPrincipal)} ${userDetails?.email ?: ""} !"
                } else {
                    stringResource(id = R.string.BienvenidoSinIniciaSesion)
                } ,
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )



            Spacer(modifier = Modifier.height(20.dp))

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

            UserActionButton(navController, authViewModel)
        }
    }
}

@Composable
fun NavigationButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(text)
    }
}

@Composable
fun UserActionButton(navController: NavHostController, authViewModel: ViewModelAuth) {
    val authState by authViewModel.authState.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                if (authState is AuthState.Authenticated) {
                    authViewModel.signOut()
                } else {
                    navController.navigate(RutasNavegacion.InicioSesion.route)
                }
            },
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(
                imageVector = if (authState is AuthState.Authenticated)
                    Icons.AutoMirrored.Filled.ExitToApp
                else
                    Icons.Default.AccountCircle,
                contentDescription = if (authState is AuthState.Authenticated)
                    stringResource(R.string.CierraSesion)
                else
                    stringResource(R.string.IniciaSesion),
                modifier = Modifier.requiredSize(80.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (authState is AuthState.Authenticated)
                    stringResource(R.string.CierraSesion)
                else
                    stringResource(R.string.IniciaSesion)
            )
        }
    }
}
