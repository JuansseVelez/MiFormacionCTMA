package com.ctma.miformacionctma.ui.screens.crear

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ctma.miformacionctma.domain.Prioridad
import com.ctma.miformacionctma.ui.theme.Dimens
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCrearActividad(
    estaGuardando: Boolean,
    onBack: () -> Unit,
    onGuardar: (titulo: String, desc: String, fecha: String, prioridad: Prioridad, progreso: Int) -> Unit,
) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var fecha by rememberSaveable { mutableStateOf("") }
    var prioridad by rememberSaveable { mutableStateOf(Prioridad.BAJA) }
    var progreso by rememberSaveable { mutableFloatStateOf(0f) }

    // Estados de interacción (para no mostrar errores al inicio)
    var tituloTouched by rememberSaveable { mutableStateOf(false) }
    var fechaTouched by rememberSaveable { mutableStateOf(false) }

    val uiState = remember(titulo, descripcion, fecha, prioridad, progreso) {
        validarFormulario(titulo, descripcion, fecha, prioridad, progreso.toInt())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Actividad") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.PaddingLarge)
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
                Column(modifier = Modifier.widthIn(max = Dimens.MaxContentWidth)) {
                    // Campo: Título
                    OutlinedTextField(
                        value = titulo,
                        onValueChange = { 
                            titulo = it
                            tituloTouched = true
                        },
                        label = { Text("Título *") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = (tituloTouched && uiState.errorTitulo != null),
                        supportingText = { 
                            if (tituloTouched) {
                                uiState.errorTitulo?.let { Text(it) }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                    // Campo: Descripción
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción (Opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = uiState.errorDescripcion != null,
                        supportingText = { uiState.errorDescripcion?.let { Text(it) } }
                    )

                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                    // Campo: Fecha
                    OutlinedTextField(
                        value = fecha,
                        onValueChange = { 
                            fecha = it
                            fechaTouched = true
                        },
                        label = { Text("Fecha Límite (AAAA-MM-DD) *") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = fechaTouched && uiState.errorFecha != null,
                        supportingText = { 
                            if (fechaTouched) {
                                uiState.errorFecha?.let { Text(it) } ?: Text("Formato: AAAA-MM-DD")
                            } else {
                                Text("Formato: AAAA-MM-DD")
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                    // Selección de Prioridad
                    Text("Prioridad", style = MaterialTheme.typography.labelLarge)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Prioridad.entries.forEach { p ->
                            RadioButton(selected = prioridad == p, onClick = { prioridad = p })
                            Text(p.name, modifier = Modifier.padding(end = Dimens.PaddingMedium))
                        }
                    }

                    Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

                    // Campo: Progreso (Slider Interactivo)
                    Text(
                        text = "Progreso: ${progreso.toInt()}%",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Slider(
                        value = progreso,
                        onValueChange = { progreso = it },
                        valueRange = 0f..100f,
                        steps = 100,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(Dimens.PaddingExtraLarge))

                    // Botón Guardar
                    Button(
                        onClick = {
                            if (!uiState.puedeGuardar) {
                                // Si intenta guardar pero hay errores, los mostramos todos
                                tituloTouched = true
                                fechaTouched = true
                            } else if (!estaGuardando) {
                                onGuardar(titulo, descripcion, fecha, prioridad, progreso.toInt())
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        // Habilitamos siempre el botón pero validamos al hacer clic para dar feedback
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
    }
}

private fun validarFormulario(
    titulo: String,
    descripcion: String,
    fecha: String,
    prioridad: Prioridad,
    progreso: Int
): FormularioActividadUiState {
    val errorTitulo = when {
        titulo.isBlank() -> "El título es obligatorio"
        titulo.length < 3 -> "Mínimo 3 caracteres"
        titulo.length > 80 -> "Máximo 80 caracteres"
        else -> null
    }

    val errorDescripcion = if (descripcion.length > 240) "Máximo 240 caracteres" else null

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply { isLenient = false }
    val errorFecha = try {
        val parsedDate = sdf.parse(fecha)
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
        
        if (parsedDate != null && parsedDate.before(today)) {
            "La fecha no puede ser anterior a hoy"
        } else null
    } catch (_: Exception) {
        if (fecha.isNotBlank()) "Formato inválido (AAAA-MM-DD)" else "La fecha es obligatoria"
    }

    val errorProgreso = if (progreso !in 0..100) "Rango: 0-100" else null

    val puedeGuardar = errorTitulo == null && errorFecha == null && 
                       errorDescripcion == null && errorProgreso == null &&
                       titulo.isNotBlank() && fecha.isNotBlank()

    return FormularioActividadUiState(
        titulo = titulo,
        descripcion = descripcion,
        fecha = fecha,
        prioridad = prioridad,
        progreso = progreso,
        errorTitulo = errorTitulo,
        errorDescripcion = errorDescripcion,
        errorFecha = errorFecha,
        errorProgreso = errorProgreso,
        puedeGuardar = puedeGuardar
    )
}
