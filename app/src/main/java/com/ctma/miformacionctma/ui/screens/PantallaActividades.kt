package com.ctma.miformacionctma.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.domain.Prioridad
import com.ctma.miformacionctma.ui.components.TarjetaActividad
import com.ctma.miformacionctma.ui.theme.Dimens
import com.ctma.miformacionctma.ui.theme.MiFormacionCTMATheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaActividades(
    actividades: List<ActividadFormativa>,
    sincronizacionState: SincronizacionUiState = SincronizacionUiState(),
    onActividadClick: (Long) -> Unit,
    onAgregarClick: () -> Unit,
    onSincronizar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Mi Formación CTMA", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregarClick) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Actividad")
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Dimens.PaddingLarge)
                    .widthIn(max = Dimens.MaxContentWidth)
            ) {
                if (sincronizacionState.estaSincronizando) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                Spacer(modifier = Modifier.height(Dimens.SpacingBetweenCards))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Resumen de Compromisos",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = onSincronizar, enabled = !sincronizacionState.estaSincronizando) {
                        Text(if (sincronizacionState.estaSincronizando) "Sincronizando..." else "Actualizar")
                    }
                }

                sincronizacionState.errorMensaje?.let { error ->
                    Spacer(modifier = Modifier.height(Dimens.PaddingSmall))
                    Text(
                        text = "Aviso: $error (Mostrando caché local)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                sincronizacionState.ultimaSincronizacion?.let { hora ->
                    Text(
                        text = "Última sincronización: $hora",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }

                Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

                if (actividades.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "No tienes actividades registradas por el momento.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))
                            Button(onClick = onSincronizar, enabled = !sincronizacionState.estaSincronizando) {
                                Text("Sincronizar con Servidor")
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = Dimens.PaddingLarge),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(
                            items = actividades,
                            key = { actividad -> actividad.id },
                        ) { actividad ->
                            TarjetaActividad(
                                actividad = actividad,
                                onClick = { onActividadClick(actividad.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaActividadesPreview() {
    MiFormacionCTMATheme {
        PantallaActividades(
            actividades = listOf(
                ActividadFormativa(1L, "Taller 1: Kotlin", "Modelado de datos", 100, 0, Prioridad.ALTA),
                ActividadFormativa(2L, "Guía 2: Repositorio", "Persistencia simulada en memoria", 80, 1, Prioridad.MEDIA),
                ActividadFormativa(3L, "Guía 3: Jetpack Compose", "Creación de vistas accesibles", 30, 4, Prioridad.ALTA)
            ),
            sincronizacionState = SincronizacionUiState(),
            onActividadClick = {},
            onAgregarClick = {},
            onSincronizar = {}
        )
    }
}
