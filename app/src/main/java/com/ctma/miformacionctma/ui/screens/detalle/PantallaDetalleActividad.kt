package com.ctma.miformacionctma.ui.screens.detalle

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.domain.Prioridad
import com.ctma.miformacionctma.ui.theme.Dimens
import com.ctma.miformacionctma.ui.theme.MiFormacionCTMATheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalleActividad(
    actividad: ActividadFormativa?,
    onBack: () -> Unit
) {
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
        ) {
            if (actividad == null) {
                Text("Cargando actividad o no encontrada...")
            } else {
                Text(
                    text = actividad.titulo,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))
                
                Text(
                    text = "Prioridad: ${actividad.prioridad.name}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

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
            onBack = {}
        )
    }
}
