package com.ctma.miformacionctma.ui.screens.crear

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ctma.miformacionctma.domain.Prioridad

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearActividad(
    onBackClick: () -> Unit,
    onGuardarExitoso: (String, String, Prioridad) -> Unit,
    modifier: Modifier = Modifier
) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var prioridad by rememberSaveable { mutableStateOf(Prioridad.BAJA) }
    var intentoGuardar by rememberSaveable { mutableStateOf(false) }
    var estaGuardando by remember { mutableStateOf(false) }

    val uiState = FormularioActividadUiState(
        titulo = titulo,
        descripcion = descripcion,
        prioridad = prioridad,
        intentoGuardar = intentoGuardar
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Actividad", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Regresar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Registrar Compromiso",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título de la actividad") },
                placeholder = { Text("Ej: Guía 4: Navegación") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errorTitulo != null,
                supportingText = {
                    if (uiState.errorTitulo != null) {
                        Text(text = uiState.errorTitulo!!, color = MaterialTheme.colorScheme.error)
                    } else {
                        Text("${titulo.length}/80 caracteres")
                    }
                },
                singleLine = true
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción (Opcional)") },
                placeholder = { Text("Breve detalle del compromiso...") },
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.errorDescripcion != null,
                supportingText = {
                    if (uiState.errorDescripcion != null) {
                        Text(text = uiState.errorDescripcion!!, color = MaterialTheme.colorScheme.error)
                    } else {
                        Text("${descripcion.length}/240 caracteres")
                    }
                },
                minLines = 3
            )

            Text(
                text = "Prioridad",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Column(Modifier.selectableGroup()) {
                Prioridad.values().forEach { nivel ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .selectable(
                                selected = (prioridad == nivel),
                                onClick = { prioridad = nivel },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (prioridad == nivel),
                            onClick = null
                        )
                        Text(
                            text = nivel.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (!estaGuardando) {
                        intentoGuardar = true
                        if (ValidadorFormulario.validarTitulo(titulo) == null &&
                            ValidadorFormulario.validarDescripcion(descripcion) == null
                        ) {
                            estaGuardando = true
                            onGuardarExitoso(titulo, descripcion, prioridad)
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !estaGuardando
            ) {
                if (estaGuardando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Guardar Actividad")
                }
            }
        }
    }
}
