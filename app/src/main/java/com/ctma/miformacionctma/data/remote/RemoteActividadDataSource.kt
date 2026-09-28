package com.ctma.miformacionctma.data.remote

import com.ctma.miformacionctma.data.remote.api.ActividadApiService
import com.ctma.miformacionctma.data.remote.dto.ActividadDto
import kotlinx.coroutines.CancellationException
import java.io.IOException
import java.net.UnknownHostException

class RemoteActividadDataSource(private val apiService: ActividadApiService) {

    suspend fun obtenerActividadesRemotas(): Result<List<ActividadDto>> {
        return try {
            val response = apiService.obtenerActividades()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Error HTTP: ${response.code()}"))
            }
        } catch (e: CancellationException) {
            throw e
        } catch (_: UnknownHostException) {
            Result.failure(Exception("Servidor remoto no disponible"))
        } catch (_: IOException) {
            Result.failure(Exception("Error de conexión a la red"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
