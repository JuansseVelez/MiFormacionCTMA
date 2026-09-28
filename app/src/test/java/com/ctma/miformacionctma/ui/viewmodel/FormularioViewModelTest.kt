package com.ctma.miformacionctma.ui.viewmodel

import com.ctma.miformacionctma.data.ActividadRepositoryImpl
import com.ctma.miformacionctma.domain.Prioridad
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class FormularioViewModelTest {

    private lateinit var repository: ActividadRepositoryImpl
    private lateinit var viewModel: FormularioViewModel

    @Before
    fun setup() {
        repository = ActividadRepositoryImpl()
        viewModel = FormularioViewModel(repository)
    }

    @Test
    fun estadoInicialEstaGuardandoEsFalse() {
        assertFalse(viewModel.estaGuardando.value)
    }

    @Test
    fun guardarActividadInsertaEnRepositorioYFinalizaGuardando() = runBlocking {
        viewModel.guardarActividad(
            titulo = "Nueva desde VM",
            descripcion = "Desc VM",
            fecha = "2026-11-20",
            prioridad = Prioridad.ALTA,
            progreso = 25
        )

        assertFalse(viewModel.estaGuardando.value)

        val lista = repository.obtenerActividades().first()
        assertEquals(1, lista.size)
        assertEquals("Nueva desde VM", lista.last().titulo)
    }
}
