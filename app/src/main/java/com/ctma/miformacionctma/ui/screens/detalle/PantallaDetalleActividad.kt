package com.ctma.miformacionctma.ui.screens.detalle

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.domain.Prioridad
import com.ctma.miformacionctma.domain.RolUsuario
import com.ctma.miformacionctma.domain.Usuario
import com.ctma.miformacionctma.ui.theme.Dimens
import com.ctma.miformacionctma.ui.theme.MiFormacionCTMATheme
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleActividad(
    actividad: ActividadFormativa?,
    usuario: Usuario? = null,
    onActualizarActividad: (ActividadFormativa) -> Unit = {},
    onBack: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var tempPhotoUri by remember { mutableStateOf<Uri?>(null) }
    var comentarioRechazo by rememberSaveable { mutableStateOf("") }
    var mostrarCampoRechazo by rememberSaveable { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempPhotoUri != null && actividad != null) {
            val actividadActualizada = actividad.copy(
                evidenciaUri = tempPhotoUri.toString()
            )
            onActualizarActividad(actividadActualizada)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Actividad") },
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
            if (actividad == null) {
                Text("Cargando actividad o no encontrada...")
            } else {
                val esAprendiz = usuario?.rol == RolUsuario.APRENDIZ
                val esInstructor = usuario?.rol == RolUsuario.INSTRUCTOR

                Text(
                    text = actividad.titulo,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = "Prioridad: ${actividad.prioridad.name}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Badge(
                        containerColor = when (actividad.estadoEntrega) {
                            "APROBADA" -> MaterialTheme.colorScheme.primary
                            "ENTREGADA" -> MaterialTheme.colorScheme.tertiary
                            "NO_APROBADA" -> MaterialTheme.colorScheme.error
                            else -> MaterialTheme.colorScheme.secondary
                        }
                    ) {
                        Text(text = "Estado: ${actividad.estadoEntrega}", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = actividad.descripcion ?: "Sin descripción adicional.",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

                Text(
                    text = "Fecha Límite: ${actividad.fechaLimite ?: "No definida"}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                Text(
                    text = "Progreso: ${actividad.progreso}%",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                
                LinearProgressIndicator(
                    progress = { actividad.progreso / 100f },
                    modifier = Modifier.fillMaxWidth().padding(top = Dimens.PaddingSmall)
                )

                if (!actividad.comentariosInstructor.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(Dimens.PaddingMedium)) {
                            Text(
                                text = "Observaciones del Instructor:",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = actividad.comentariosInstructor,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
                Text(
                    text = "Evidencia Fotográfica",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                
                if (actividad.evidenciaUri != null) {
                    Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(Dimens.PaddingMedium)) {
                            Text(text = "Foto adjunta: ${actividad.evidenciaUri}", style = MaterialTheme.typography.bodySmall)
                            Text(text = "Estado: Subida con éxito", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

                if (esAprendiz) {
                    Button(
                        onClick = {
                            val photoFile = File(context.filesDir, "images").apply { mkdirs() }
                            val imageFile = File(photoFile, "evidencia_${System.currentTimeMillis()}.jpg")
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                imageFile
                            )
                            tempPhotoUri = uri
                            cameraLauncher.launch(uri)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (actividad.evidenciaUri == null) "Tomar Foto con Cámara" else "Reemplazar Foto de Evidencia")
                    }

                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                    Button(
                        onClick = {
                            val actualizada = actividad.copy(
                                estadoEntrega = "ENTREGADA",
                                progreso = 50
                            )
                            onActualizarActividad(actualizada)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                    ) {
                        Text("Marcar como Entregada (50%)")
                    }
                }

                if (esInstructor) {
                    Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                    Text(
                        text = "Panel de Calificación (Instructor)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(Dimens.PaddingSmall))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.PaddingMedium)
                    ) {
                        Button(
                            onClick = {
                                val aprobada = actividad.copy(
                                    estadoEntrega = "APROBADA",
                                    progreso = 100,
                                    comentariosInstructor = null
                                )
                                onActualizarActividad(aprobada)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Aprobar (100%)")
                        }

                        OutlinedButton(
                            onClick = { mostrarCampoRechazo = !mostrarCampoRechazo },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("No Aprobado")
                        }
                    }

                    if (mostrarCampoRechazo) {
                        Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                        OutlinedTextField(
                            value = comentarioRechazo,
                            onValueChange = { comentarioRechazo = it },
                            label = { Text("Motivo del rechazo / Correcciones") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                        Button(
                            onClick = {
                                val rechazada = actividad.copy(
                                    estadoEntrega = "NO_APROBADA",
                                    progreso = 20,
                                    comentariosInstructor = comentarioRechazo
                                )
                                onActualizarActividad(rechazada)
                                mostrarCampoRechazo = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Enviar Observaciones al Aprendiz")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaDetalleActividadPreview() {
    MiFormacionCTMATheme {
        PantallaDetalleActividad(
            actividad = ActividadFormativa(
                id = 1L,
                titulo = "Taller 1: Kotlin y Compose",
                descripcion = "Ejemplo de vista previa en detalle de actividad.",
                progreso = 75,
                diasRestantes = 3,
                prioridad = Prioridad.ALTA,
                fechaLimite = "2026-10-30"
            ),
            usuario = Usuario("1", "Instructor Admin", "instructor@ctma.com", RolUsuario.INSTRUCTOR),
            onBack = {}
        )
    }
}
