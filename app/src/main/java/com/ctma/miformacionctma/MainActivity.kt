package com.ctma.miformacionctma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ctma.miformacionctma.data.ActividadRepositoryImpl
import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.domain.Prioridad
import com.ctma.miformacionctma.ui.theme.MiFormacionCTMATheme

class MainActivity : ComponentActivity() {

    private val repository = ActividadRepositoryImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repository.agregarActividad(
            ActividadFormativa(
                id = 1L,
                titulo = "Taller: Primer proyecto Android",
                descripcion = "Construcción del modelo y repositorio",
                progreso = 100,
                diasRestantes = 0,
                prioridad = Prioridad.ALTA
            )
        )

        enableEdgeToEdge()
        setContent {
            MiFormacionCTMATheme {
                val actividades = repository.obtenerActividades()
                val proximaActividad = actividades.firstOrNull()
                PantallaInicio(actividad = proximaActividad)
            }
        }
    }
}

@Composable
fun PantallaInicio(nombre: String = "Aprendiz", actividad: ActividadFormativa? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Mi Formación CTMA",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(
            modifier = Modifier.height(12.dp)
        )
        Text(
            text = "Hola, $nombre"
        )
        Text(
            text = "Aquí organizarás actividades y evidencias"
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Próximo compromiso formativo",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                if (actividad != null) {
                    Text(text = "Taller: ${actividad.titulo}")
                    Text(text = "Días restantes: ${actividad.diasRestantes}")
                    Text(text = "Prioridad: ${actividad.prioridad}")
                    Text(text = "Progreso: ${actividad.progreso}%")
                } else {
                    Text(text = "No hay actividades registradas")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaInicioPreview() {
    MiFormacionCTMATheme {
        PantallaInicio(
            actividad = ActividadFormativa(
                id = 1L,
                titulo = "Taller: Primer proyecto Android",
                descripcion = "Vista previa",
                progreso = 50,
                diasRestantes = 3,
                prioridad = Prioridad.MEDIA
            )
        )
    }
}