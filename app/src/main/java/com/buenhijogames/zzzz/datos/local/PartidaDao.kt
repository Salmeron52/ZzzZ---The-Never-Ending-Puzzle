package com.buenhijogames.zzzz.datos.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Data Access Object para operaciones de base de datos de la partida.
 */
@Dao
interface PartidaDao {

    /**
     * Inserta o actualiza la partida guardada.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarPartida(partida: PartidaEntidad)

    /**
     * Obtiene la partida guardada.
     */
    @Query("SELECT * FROM partida WHERE id = 1")
    suspend fun obtenerPartida(): PartidaEntidad?

    /**
     * Elimina la partida guardada.
     */
    @Query("DELETE FROM partida WHERE id = 1")
    suspend fun eliminarPartida()
}
