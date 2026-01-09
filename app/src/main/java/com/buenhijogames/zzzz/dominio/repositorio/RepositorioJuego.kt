package com.buenhijogames.zzzz.dominio.repositorio

import com.buenhijogames.zzzz.dominio.modelo.Ficha

/**
 * Interfaz del repositorio para la persistencia del estado del juego.
 */
interface RepositorioJuego {

    /**
     * Guarda el estado actual del juego.
     *
     * @param tablero Estado actual del tablero
     * @param puntuacion Puntuación actual
     * @param record Récord máximo alcanzado
     */
    suspend fun guardarPartida(
        tablero: List<List<Ficha?>>,
        puntuacion: Long,
        record: Long
    )

    /**
     * Carga la partida guardada.
     *
     * @return Triple con tablero, puntuación y récord, o null si no hay partida guardada
     */
    suspend fun cargarPartida(): Triple<List<List<Ficha?>>, Long, Long>?

    /**
     * Elimina la partida guardada.
     */
    suspend fun eliminarPartida()
}
