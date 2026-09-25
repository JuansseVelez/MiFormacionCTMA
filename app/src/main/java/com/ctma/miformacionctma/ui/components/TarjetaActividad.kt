package com.ctma.miformacionctma.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.domain.Prioridad
import com.ctma.miformacionctma.ui.theme.Dimens
import com.ctma.miformacionctma.ui.theme.MiFormacionCTMATheme

@Composable
fun TarjetaActividad(
    actividad: ActividadFormativa,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.PaddingSmall),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimens.CardElevation)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.PaddingLarge)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = actividad.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = actividad.prioridad.name,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = Dimens.PaddingMedium)
                )
            }

            Spacer(modifier = Modifier.height(Dimens.PaddingSmall))

            Text(
                text = actividad.descripcion ?: "Sin descripción adicional",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(Dimens.PaddingLarge))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Días restantes: ${actividad.diasRestantes}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Progreso: ${actividad.progreso}%",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(Dimens.PaddingMedium))

            val progresoFloat = actividad.progreso / 100f
            LinearProgressIndicator(
                progress = { progresoFloat },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = "Progreso de la actividad"
                        stateDescription = "${actividad.progreso} por ciento completado"
                    }
            )
        }
    }
}

@Preview(showBackground = true, name = "Estado Normal")
@Composable
fun TarjetaActividadPreview() {
    MiFormacionCTMATheme {
        TarjetaActividad(
            actividad = ActividadFormativa(
                id = 1L,
                titulo = "Guía 3: Interfaces con Compose",
                descripcion = "Diseño de pantallas declarativas, accesibles y adaptables.",
                progreso = 45,
                diasRestantes = 2,
                prioridad = Prioridad.ALTA
            )
        )
    }
}

@Preview(showBackground = true, name = "Caso Límite: Título Largo y 0%")
@Composable
fun TarjetaActividadLimitePreview() {
    MiFormacionCTMATheme {
        TarjetaActividad(
            actividad = ActividadFormativa(
                id = 99L,
                titulo = "Esta es una actividad con un título extremadamente largo que debería ocupar al menos dos líneas en la interfaz para probar el truncado",
                descripcion = "Descripción también larga para validar que el contenedor maneja correctamente el flujo de texto sin romperse en pantallas pequeñas.",
                progreso = 0,
                diasRestantes = 99,
                prioridad = Prioridad.BAJA
            )
        )
    }
}
