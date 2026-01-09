package com.buenhijogames.zzzz.datos.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object para operaciones de base de datos.
 */
@Dao
interface PartidaDao {

    // ========== Partida Actual (ID = 1) ==========

    /**
     * Inserta o actualiza la partida actual.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarPartida(partida: PartidaEntidad)

    /**
     * Obtiene la partida actual.
     */
    @Query("SELECT * FROM partida WHERE id = 1")
    suspend fun obtenerPartida(): PartidaEntidad?

    /**
     * Elimina la partida actual.
     */
    @Query("DELETE FROM partida WHERE id = 1")
    suspend fun eliminarPartida()

    // ========== Partidas Guardadas Múltiples ==========

    /**
     * Inserta una nueva partida guardada.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarPartidaGuardada(partida: PartidaGuardadaEntidad): Long

    /**
     * Obtiene todas las partidas guardadas ordenadas por fecha de modificación.
     */
    @Query("SELECT * FROM partidas_guardadas ORDER BY fechaModificacion DESC")
    fun obtenerTodasLasPartidas(): Flow<List<PartidaGuardadaEntidad>>

    /**
     * Obtiene una partida guardada por ID.
     */
    @Query("SELECT * FROM partidas_guardadas WHERE id = :id")
    suspend fun obtenerPartidaGuardadaPorId(id: Long): PartidaGuardadaEntidad?

    /**
     * Actualiza una partida guardada existente.
     */
    @Query("""
        UPDATE partidas_guardadas 
        SET tableroJson = :tableroJson, 
            puntuacion = :puntuacion, 
            record = :record, 
            fichaMaxima = :fichaMaxima,
            fechaModificacion = :fechaModificacion,
            contadorFichas = :contadorFichas,
            nivelId = :nivelId
        WHERE id = :id
    """)
    suspend fun actualizarPartidaGuardada(
        id: Long,
        tableroJson: String,
        puntuacion: Long,
        record: Long,
        fichaMaxima: Int,
        fechaModificacion: Long,
        contadorFichas: Long,
        nivelId: Int
    )

    /**
     * Elimina una partida guardada por ID.
     */
    @Query("DELETE FROM partidas_guardadas WHERE id = :id")
    suspend fun eliminarPartidaGuardada(id: Long)

    /**
     * Cuenta el número de partidas guardadas.
     */
    @Query("SELECT COUNT(*) FROM partidas_guardadas")
    suspend fun contarPartidasGuardadas(): Int
}

