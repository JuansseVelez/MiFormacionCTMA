package com.ctma.miformacionctma.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ActividadDto(
    @SerializedName("id") val id: Long = 0L,
    @SerializedName("competenciaId") val competenciaId: Long? = null,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String? = null,
    @SerializedName("progreso") val progreso: Int = 0,
    @SerializedName("fechaLimiteMillis") val fechaLimiteMillis: Long = 0L,
    @SerializedName("prioridad") val prioridad: String = "BAJA",
    @SerializedName("completada") val completada: Boolean = false,
)
