package com.buenhijogames.zzzz.dominio.repositorio

import com.buenhijogames.zzzz.dominio.modelo.Ficha
import kotlinx.coroutines.flow.Flow

/**
 * Modelo de dominio para partida guardada.
 */
data class PartidaGuardada(
    val id: Long,
    val nombre: String,
    val tablero: List<List<Ficha?>>,
    val puntuacion: Long,
    val record: Long,
    val fichaMaxima: Int,
    val fechaCreacion: Long,
    val fechaModificacion: Long,
    val nivelId: Int = 1
)

/**
 * Interfaz del repositorio para la persistencia del estado del juego.
 */
interface RepositorioJuego {

    // ========== Partida Actual ==========

    /**
     * Guarda el estado actual del juego.
     */
    suspend fun guardarPartida(
        tablero: List<List<Ficha?>>,
        puntuacion: Long,
        record: Long,
        nivelId: Int = 1
    )

    /**
     * Carga la partida actual.
     * Retorna: Triple(Tablero, Puntuacion, Record) + NivelId -> Vamos a usar una data class o Cuarteto si fuera posible,
     * pero mejor cambiemos el retorno a un objeto simple o añadimos el Int al final.
     * Para mantener compatibilidad simple, retornaremos un objeto con todo.
     * O mejor, Quadruple no existe. Usaremos:
     * Pair<Triple<List<List<Ficha?>>, Long, Long>, Int>?
     * No, es muy feo.
     * Vamos a retornar un objeto PartidaActual?
     * O simplemente Triple y asumimos nivel? No, el bug es ese.
     * Vamos a cambiar a:
     * suspend fun cargarPartida(): DatosPartidaActual?
     */
    data class DatosPartidaActual(
        val tablero: List<List<Ficha?>>,
        val puntuacion: Long,
        val record: Long,
        val nivelId: Int
    )

    suspend fun cargarPartida(): DatosPartidaActual?

    /**
     * Elimina la partida actual.
     */
    suspend fun eliminarPartida()

    // ========== Partidas Guardadas Múltiples ==========

    /**
     * Guarda la partida actual como una partida guardada nueva o actualiza una existente.
     */
    suspend fun guardarPartidaComoGuardada(
        tablero: List<List<Ficha?>>,
        puntuacion: Long,
        record: Long,
        nivelId: Int = 1,
        partidaId: Long? = null
    ): Long

    /**
     * Obtiene todas las partidas guardadas como Flow.
     */
    fun obtenerPartidasGuardadas(): Flow<List<PartidaGuardada>>

    /**
     * Carga una partida guardada por ID.
     */
    suspend fun cargarPartidaGuardada(id: Long): PartidaGuardada?

    /**
     * Elimina una partida guardada.
     */
    suspend fun eliminarPartidaGuardada(id: Long)

    /**
     * Actualiza la fecha de modificación de una partida guardada (para ponerla al principio).
     */
    suspend fun actualizarFechaUltimoAcceso(id: Long)
}

