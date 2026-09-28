package com.ctma.miformacionctma.data.local.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ActividadConCompetencia(
    @Embedded val actividad: ActividadEntity,
    @Relation(
        parentColumn = "competenciaId",
        entityColumn = "id"
    )
    val competencia: CompetenciaEntity?
)
