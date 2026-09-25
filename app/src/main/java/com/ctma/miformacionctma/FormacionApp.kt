package com.ctma.miformacionctma

import android.app.Application
import androidx.room.Room
import com.ctma.miformacionctma.data.PreferenciasRepository
import com.ctma.miformacionctma.data.RoomActividadRepository
import com.ctma.miformacionctma.data.local.db.FormacionDatabase
import com.ctma.miformacionctma.data.local.db.Migraciones
import com.ctma.miformacionctma.domain.ActividadRepository

class FormacionApp : Application() {

    lateinit var database: FormacionDatabase
    lateinit var repository: ActividadRepository
    lateinit var preferenciasRepository: PreferenciasRepository

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            this,
            FormacionDatabase::class.java,
            "formacion-db"
        )
        .addMigrations(Migraciones.MIGRATION_1_2)
        .build()

        repository = RoomActividadRepository(database.formacionDao())
        preferenciasRepository = PreferenciasRepository(this)
    }
}
