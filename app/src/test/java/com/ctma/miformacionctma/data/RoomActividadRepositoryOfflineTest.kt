package com.ctma.miformacionctma.data

import com.ctma.miformacionctma.data.local.dao.FormacionDao
import com.ctma.miformacionctma.data.local.entities.ActividadConCompetencia
import com.ctma.miformacionctma.data.local.entities.ActividadEntity
import com.ctma.miformacionctma.data.local.entities.CompetenciaEntity
import com.ctma.miformacionctma.data.remote.RemoteActividadDataSource
import com.ctma.miformacionctma.data.remote.api.ActividadApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class RoomActividadRepositoryOfflineTest {

    private lateinit var fakeDao: FakeFormacionDaoForOffline
    private lateinit var fakeApiService: FakeApiService
    private lateinit var remoteDataSource: RemoteActividadDataSource
    private lateinit var repository: RoomActividadRepository

    @Before
    fun setup() {
        fakeDao = FakeFormacionDaoForOffline()
        fakeApiService = FakeApiService()
        remoteDataSource = RemoteActividadDataSource(fakeApiService)
        repository = RoomActividadRepository(fakeDao, remoteDataSource)
    }

    @Test
    fun sincronizacionConFalloDeRedConservaCacheLocal() = runBlocking {
        // 1. Insertar datos en caché local (Room)
        fakeDao.insertarActividad(
            ActividadEntity(1L, null, "Actividad en Caché", "Desc", 50, System.currentTimeMillis(), "ALTA", false)
        )
        val antes = repository.obtenerActividades().first()
        assertEquals(1, antes.size)

        // 2. Simular fallo de red (IOException)
        fakeApiService.lanzarExcepcion = true

        val resultado = repository.sincronizarConServidor()

        // 3. Verificar que la sincronización falló pero el caché local sigue intacto
        assertTrue(resultado.isFailure)
        val despues = repository.obtenerActividades().first()
        assertEquals(1, despues.size)
        assertEquals("Actividad en Caché", despues[0].titulo)
    }
}

class FakeApiService : ActividadApiService {
    var lanzarExcepcion = false

    override suspend fun obtenerActividades(): Response<List<com.ctma.miformacionctma.data.remote.dto.ActividadDto>> {
        if (lanzarExcepcion) {
            throw IOException("Sin conexión a Internet")
        }
        return Response.success(emptyList())
    }

    override suspend fun crearActividad(actividad: com.ctma.miformacionctma.data.remote.dto.ActividadDto): Response<com.ctma.miformacionctma.data.remote.dto.ActividadDto> {
        return Response.success(actividad)
    }
}

class FakeFormacionDaoForOffline : FormacionDao {
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
