package com.ctma.miformacionctma.data.remote

import com.ctma.miformacionctma.data.remote.api.ActividadApiService
import com.ctma.miformacionctma.data.remote.dto.ActividadDto
import kotlinx.coroutines.CancellationException
import java.io.IOException

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
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
