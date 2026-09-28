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
import com.ctma.miformacionctma.data.AuthRepository
import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.ui.navigation.Destino
import com.ctma.miformacionctma.ui.screens.PantallaActividades
import com.ctma.miformacionctma.ui.screens.auth.PantallaLogin
import com.ctma.miformacionctma.ui.screens.auth.PantallaRegistro
import com.ctma.miformacionctma.ui.screens.crear.PantallaCrearActividad
import com.ctma.miformacionctma.ui.screens.detalle.PantallaDetalleActividad
import com.ctma.miformacionctma.ui.theme.MiFormacionCTMATheme
import com.ctma.miformacionctma.ui.viewmodel.ActividadesViewModel
import com.ctma.miformacionctma.ui.viewmodel.AuthViewModel
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

    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModel.Factory(AuthRepository(app.preferenciasRepository), app.preferenciasRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MiFormacionCTMATheme {
                val navController = rememberNavController()
                val actividades by actividadesViewModel.actividades.collectAsState()
                val sincronizacionState by actividadesViewModel.sincronizacionState.collectAsState()
                val usuario by authViewModel.usuarioActual.collectAsState()

                val startDestination = if (usuario != null) Destino.Lista.ruta else Destino.Login.ruta

                NavHost(navController = navController, startDestination = startDestination) {
                    composable(Destino.Login.ruta) {
                        PantallaLogin(
                            onLoginExitoso = {
                                navController.navigate(Destino.Lista.ruta) {
                                    popUpTo(Destino.Login.ruta) { inclusive = true }
                                }
                            },
                            onIrARegistro = {
                                navController.navigate(Destino.Registro.ruta)
                            },
                            onLoginAction = { email, pass, callback ->
                                authViewModel.iniciarSesion(email, pass, callback)
                            }
                        )
                    }

                    composable(Destino.Registro.ruta) {
                        PantallaRegistro(
                            onRegistroExitoso = {
                                navController.navigate(Destino.Lista.ruta) {
                                    popUpTo(Destino.Login.ruta) { inclusive = true }
                                }
                            },
                            onBack = { navController.popBackStack() },
                            onRegistroAction = { nombre, email, pass, rol, callback ->
                                authViewModel.registrarse(nombre, email, pass, rol, callback)
                            }
                        )
                    }

                    composable(Destino.Lista.ruta) {
                        PantallaActividades(
                            actividades = actividades,
                            sincronizacionState = sincronizacionState,
                            usuario = usuario,
                            onActividadClick = { id ->
                                navController.navigate(Destino.Detalle.crearRuta(id))
                            },
                            onAgregarClick = {
                                navController.navigate(Destino.Crear.ruta)
                            },
                            onSincronizar = {
                                actividadesViewModel.sincronizar()
                            },
                            onCerrarSesion = {
                                authViewModel.cerrarSesion()
                                navController.navigate(Destino.Login.ruta) {
                                    popUpTo(Destino.Lista.ruta) { inclusive = true }
                                }
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
                            usuario = usuario,
                            onActualizarActividad = { actActualizada ->
                                lifecycleScope.launch {
                                    app.repository.agregarActividad(actActualizada)
                                    navController.popBackStack()
                                }
                            },
                            onEliminarActividad = { actAEliminar ->
                                lifecycleScope.launch {
                                    app.repository.eliminarActividad(actAEliminar)
                                    navController.popBackStack()
                                }
                            },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
