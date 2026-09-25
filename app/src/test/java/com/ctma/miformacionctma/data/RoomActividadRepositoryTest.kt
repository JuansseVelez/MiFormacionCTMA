package com.ctma.miformacionctma.data

import com.ctma.miformacionctma.data.local.dao.FormacionDao
import com.ctma.miformacionctma.data.local.entities.ActividadConCompetencia
import com.ctma.miformacionctma.data.local.entities.ActividadEntity
import com.ctma.miformacionctma.data.local.entities.CompetenciaEntity
import com.ctma.miformacionctma.domain.ActividadFormativa
import com.ctma.miformacionctma.domain.Prioridad
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class RoomActividadRepositoryTest {

    private lateinit var fakeDao: FakeFormacionDao
    private lateinit var repository: RoomActividadRepository

    @Before
    fun setup() {
        fakeDao = FakeFormacionDao()
        repository = RoomActividadRepository(fakeDao)
    }

    @Test
    fun obtenerActividadesMapeaEntidadesADominio() = runBlocking {
        fakeDao.insertarActividad(
            ActividadEntity(
                id = 1L,
                competenciaId = null,
                titulo = "Actividad DB Fake",
                descripcion = "Desc Fake",
                progreso = 50,
                fechaLimiteMillis = System.currentTimeMillis(),
                prioridad = "ALTA"
            )
        )

        val lista = repository.obtenerActividades().first()
        assertEquals(1, lista.size)
        assertEquals("Actividad DB Fake", lista[0].titulo)
        assertEquals(Prioridad.ALTA, lista[0].prioridad)
    }

    @Test
    fun obtenerActividadPorIdRetornaEntidadMapeadaOTrue() = runBlocking {
        fakeDao.insertarActividad(
            ActividadEntity(
                id = 10L,
                competenciaId = null,
                titulo = "Busqueda por ID",
                descripcion = null,
                progreso = 10,
                fechaLimiteMillis = System.currentTimeMillis(),
                prioridad = "MEDIA"
            )
        )

        val encontrada = repository.obtenerActividadPorId(10L)
        assertNotNull(encontrada)
        assertEquals("Busqueda por ID", encontrada?.titulo)

        val inexistente = repository.obtenerActividadPorId(99L)
        assertNull(inexistente)
    }

    @Test
    fun agregarActividadInsertaEnDao() = runBlocking {
        val dominio = ActividadFormativa(
            id = 2L,
            titulo = "Nueva Actividad Repository",
            descripcion = "Desc",
            progreso = 0,
            diasRestantes = 5,
            prioridad = Prioridad.BAJA
        )

        repository.agregarActividad(dominio)

        val lista = repository.obtenerActividades().first()
        assertEquals(1, lista.size)
        assertEquals("Nueva Actividad Repository", lista[0].titulo)
    }
}

class FakeFormacionDao : FormacionDao {
    private val actividades = mutableListOf<ActividadEntity>()
    private val flow = MutableStateFlow<List<ActividadConCompetencia>>(emptyList())

    private fun notificar() {
        flow.value = actividades.map { ActividadConCompetencia(it, null) }
    }

    override fun obtenerTodasLasActividades(): Flow<List<ActividadConCompetencia>> = flow

    override suspend fun obtenerActividadPorId(id: Long): ActividadConCompetencia? {
        val entity = actividades.find { it.id == id } ?: return null
        return ActividadConCompetencia(entity, null)
    }

    override suspend fun insertarActividad(actividad: ActividadEntity) {
        actividades.removeAll { it.id == actividad.id }
        actividades.add(actividad)
        notificar()
    }

    override suspend fun insertarCompetencia(competencia: CompetenciaEntity) {}

    override suspend fun eliminarActividad(actividad: ActividadEntity) {
        actividades.removeAll { it.id == actividad.id }
        notificar()
    }

    override fun buscarActividadesPorTexto(query: String): Flow<List<ActividadConCompetencia>> = flow

    override fun buscarActividadesPorCompetencia(competenciaId: Long): Flow<List<ActividadConCompetencia>> = flow
}
