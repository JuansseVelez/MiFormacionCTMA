package com.ctma.miformacionctma.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.ctma.miformacionctma.ui.theme.Dimens
import com.ctma.miformacionctma.ui.theme.MiFormacionCTMATheme
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLogin(
    onLoginExitoso: () -> Unit,
    onIrARegistro: () -> Unit,
    onLoginAction: (String, String, (Boolean) -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var errorMensaje by rememberSaveable { mutableStateOf<String?>(null) }
    var cargando by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Formación CTMA — Acceso") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimens.PaddingLarge)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(modifier = Modifier.widthIn(max = Dimens.MaxContentWidth)) {
                    Text(
                        text = "Iniciar Sesión",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(Dimens.SpacingBetweenCards))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(Dimens.PaddingMedium)) {
                            Text(
                                text = "Credenciales de prueba:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "• Admin (Instructor): instructor@formacion.ctma / instructor123",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "• Usuario (Aprendiz): aprendiz@formacion.ctma / aprendiz123",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo electrónico") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true
                    )

                    errorMensaje?.let { err ->
                        Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                        Text(text = err, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

                    Button(
                        onClick = {
                            if (email.isBlank() || password.isBlank()) {
                                errorMensaje = "Completa todos los campos"
                            } else {
                                cargando = true
                                errorMensaje = null
                                onLoginAction(email, password) { exito ->
                                    cargando = false
                                    if (exito) {
                                        onLoginExitoso()
                                    } else {
                                        errorMensaje = "Credenciales incorrectas"
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !cargando
                    ) {
                        if (cargando) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                        } else {
                            Text("Entrar")
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                    TextButton(
                        onClick = onIrARegistro,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("¿No tienes cuenta? Regístrate aquí")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaLoginPreview() {
    MiFormacionCTMATheme {
        PantallaLogin(
            onLoginExitoso = {},
            onIrARegistro = {},
            onLoginAction = { _, _, callback -> callback(true) }
        )
    }
}
