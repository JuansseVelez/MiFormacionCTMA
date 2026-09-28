package com.ctma.miformacionctma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.ui.navigation.Destino
import com.ctma.miformacionctma.ui.screens.PantallaActividades
import com.ctma.miformacionctma.ui.screens.crear.PantallaCrearActividad
import com.ctma.miformacionctma.ui.screens.detalle.PantallaDetalleActividad
import com.ctma.miformacionctma.ui.theme.MiFormacionCTMATheme
import com.ctma.miformacionctma.ui.viewmodel.ActividadesViewModel
import com.ctma.miformacionctma.ui.viewmodel.FormularioViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val app by lazy { application as FormacionApp }
    
    private val actividadesViewModel: ActividadesViewModel by viewModels {
        ActividadesViewModel.Factory(app.repository)
    }
    
    private val formularioViewModel: FormularioViewModel by viewModels {
        FormularioViewModel.Factory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MiFormacionCTMATheme {
                val navController = rememberNavController()
                val actividades by actividadesViewModel.actividades.collectAsState()
                val sincronizacionState by actividadesViewModel.sincronizacionState.collectAsState()

                NavHost(navController = navController, startDestination = Destino.Lista.ruta) {
                    composable(Destino.Lista.ruta) {
                        PantallaActividades(
                            actividades = actividades,
                            sincronizacionState = sincronizacionState,
                            onActividadClick = { id ->
                                navController.navigate(Destino.Detalle.crearRuta(id))
                            },
                            onAgregarClick = {
                                navController.navigate(Destino.Crear.ruta)
                            },
                            onSincronizar = {
                                actividadesViewModel.sincronizar()
                            }
                        )
                    }

                    composable(Destino.Crear.ruta) {
                        val estaGuardando by formularioViewModel.estaGuardando.collectAsState()
                        PantallaCrearActividad(
                            estaGuardando = estaGuardando,
                            onBack = { navController.popBackStack() },
                            onGuardar = { titulo, desc, fecha, prioridad, progreso ->
                                lifecycleScope.launch {
                                    formularioViewModel.guardarActividad(
                                        titulo, desc, fecha, prioridad, progreso
                                    )
                                    navController.popBackStack()
                                }
                            }
                        )
                    }

                    composable(
                        route = Destino.Detalle.ruta,
                        arguments = listOf(navArgument("id") { type = NavType.LongType })
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getLong("id") ?: 0L
                        var actividad by remember { mutableStateOf<ActividadFormativa?>(null) }
                        
                        LaunchedEffect(id) {
                            actividad = app.repository.obtenerActividadPorId(id)
                        }

                        PantallaDetalleActividad(
                            actividad = actividad,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
