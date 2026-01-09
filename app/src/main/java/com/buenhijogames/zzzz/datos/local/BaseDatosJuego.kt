package com.buenhijogames.zzzz.datos.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Base de datos Room para el juego ZzzZ.
 */
@Database(
    entities = [PartidaEntidad::class],
    version = 1,
    exportSchema = false
)
abstract class BaseDatosJuego : RoomDatabase() {

    abstract fun partidaDao(): PartidaDao

    companion object {
        private const val NOMBRE_BD = "zzzz_database"

        @Volatile
        private var INSTANCIA: BaseDatosJuego? = null

        fun obtenerInstancia(contexto: Context): BaseDatosJuego {
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    contexto.applicationContext,
                    BaseDatosJuego::class.java,
                    NOMBRE_BD
                ).build()
                INSTANCIA = instancia
                instancia
            }
        }
    }
}
