package com.buenhijogames.zzzz.datos.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Base de datos Room para el juego ZzzZ.
 */
@Database(
    entities = [PartidaEntidad::class, PartidaGuardadaEntidad::class],
    version = 5,
    exportSchema = false
)
abstract class BaseDatosJuego : RoomDatabase() {

    abstract fun partidaDao(): PartidaDao

    companion object {
        private const val NOMBRE_BD = "zzzz_database"

        @Volatile
        private var INSTANCIA: BaseDatosJuego? = null

        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE partida ADD COLUMN partidaId INTEGER DEFAULT NULL")
            }
        }

        fun obtenerInstancia(contexto: Context): BaseDatosJuego {
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    contexto.applicationContext,
                    BaseDatosJuego::class.java,
                    NOMBRE_BD
                )
                    .addMigrations(MIGRATION_4_5)
                    .build()
                INSTANCIA = instancia
                instancia
            }
        }
    }
}

