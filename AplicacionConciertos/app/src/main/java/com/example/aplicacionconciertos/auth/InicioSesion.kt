package com.example.aplicacionconciertos.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacionconciertos.R
import com.example.aplicacionconciertos.model.RutasNavegacion
import com.example.aplicacionconciertos.viewmodel.AuthState
import com.example.aplicacionconciertos.viewmodel.AuthViewModel

@Composable
fun InicioSesion(authViewModel: AuthViewModel, navController: NavController) {
    var email by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when(authState.value) {
            is AuthState.Authenticated -> navController.navigate(RutasNavegacion.Home.route)
            is AuthState.Error -> Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = (stringResource(id = R.string.IniciaSesion)),
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(text = (stringResource(id = R.string.Correo)))
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(text = (stringResource(id = R.string.Contrasena)))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button (onClick = {
            authViewModel.inicioSesion(email, password)
        },
            enabled = authState.value != AuthState.Loading
        ) {
            Text(text = (stringResource(id = R.string.IniciaSesionBoton)))
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {
            navController.navigate(RutasNavegacion.Registro.route)
        }) {
            Text(text = stringResource(id = R.string.MensajeLogin))
        }
    }
}

@Composable
fun PasswordVisibleIcon(
    passwordVisible: MutableState<Boolean>){
val image = if (passwordVisible.value)
    Icons.Default.VisibilityOff
    else Icons.Default.Visibility
    IconButton(onClick = {
        passwordVisible.value = !passwordVisible.value
    }){
        Icon(imageVector = image, contentDescription = "")
    }
}

@Composable
fun PasswordInput(
    passwordState: MutableState<String>,
    labelId: String,
    passwordVisible: MutableState<Boolean>) {
    val visualTransformation = if (passwordVisible.value)
        VisualTransformation.None
    else PasswordVisualTransformation()
    OutlinedTextField(
    value = passwordState.value,
        onValueChange = {passwordState.value = it},
        label = { Text(text = labelId)},
    singleLine=true,
    keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Password
    ),
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        visualTransformation = visualTransformation,
        trailingIcon = {if (passwordState.value.isNotBlank()){
            PasswordVisibleIcon(passwordVisible)
        } else null}
    )
}

@Composable
fun UserForm(isCreateAccount: Boolean=false, onDone:(String,String)->Unit= {email,pwd->} ){
val email = rememberSaveable {
    mutableStateOf("")
}
    val password = rememberSaveable {
        mutableStateOf("")
    }
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }
    val valido = remember(email.value, password.value){
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally){
    EmailInput(emailState = email)
    }
    PasswordInput(passwordState = password,
        labelId = "Contraseña",
        passwordVisible = passwordVisible)
    SubmitButton(textId = if (isCreateAccount) "Crear Cuenta" else "Iniciar Sesión",
        inputValido= valido){
        onDone(email.value.trim(),password.value.trim())
    }

}

@Composable
fun SubmitButton(textId: String, inputValido: Boolean, onClic: () -> Unit) {
Button(onClick = {onClic},
    modifier = Modifier.padding(3.dp).fillMaxWidth(),
    shape = CircleShape,enabled = inputValido) {
    Text(text = textId,
        modifier = Modifier.padding(5.dp))
}
}

@Composable
fun EmailInput(emailState: MutableState<String>, labelId: String = "Email") {
InputField(
    valueState = emailState,
    labelId = labelId,
    keyboardType = KeyboardType.Email,
)
}

@Composable
fun InputField(valueState: MutableState<String>,isSingleLine:Boolean=true, labelId: String, keyboardType: KeyboardType) {
OutlinedTextField(
    value = valueState.value,
    onValueChange = {valueState.value = it},
    label = { Text(text = labelId)},
    singleLine = isSingleLine,
    modifier = Modifier.padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
        .fillMaxWidth(),
    keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}
