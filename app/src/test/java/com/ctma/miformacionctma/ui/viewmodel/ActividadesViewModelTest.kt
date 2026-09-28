package com.ctma.miformacionctma.ui.viewmodel

import com.ctma.miformacionctma.data.FakeFormacionDao
import com.ctma.miformacionctma.data.RoomActividadRepository
import com.ctma.miformacionctma.data.local.entities.ActividadEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ActividadesViewModelTest {

    private lateinit var fakeDao: FakeFormacionDao
    private lateinit var repository: RoomActividadRepository

    @Before
    fun setup() {
        fakeDao = FakeFormacionDao()
        repository = RoomActividadRepository(fakeDao)
    }

    @Test
    fun actividadesStateFlowExponeListaInicialDelRepositorio() = runBlocking {
        val viewModel = ActividadesViewModel(repository)
        val lista = viewModel.actividades.value
        assertEquals(0, lista.size)
    }

    @Test
    fun actividadesStateFlowReflejaElementosEnRepositorio() = runBlocking {
        fakeDao.insertarActividad(
            ActividadEntity(1L, null, "Test VM Room", "Desc", 10, System.currentTimeMillis(), "MEDIA")
        )
        val lista = repository.obtenerActividades().first()
        assertEquals(1, lista.size)
        assertEquals("Test VM Room", lista[0].titulo)
    }
}
