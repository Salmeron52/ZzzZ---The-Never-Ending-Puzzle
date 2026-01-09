package com.buenhijogames.zzzz.presentacion.viewmodel

import com.buenhijogames.zzzz.dominio.modelo.Ficha

/**
 * Estado de la UI del juego.
 *
 * @property tablero Estado actual del tablero 4x4
 * @property puntuacion Puntuación actual
 * @property record Récord máximo alcanzado
 * @property finDelJuego Indica si el juego ha terminado
 * @property cargando Indica si se está cargando la partida
 */
data class EstadoJuego(
    val tablero: List<List<Ficha?>> = emptyList(),
    val puntuacion: Long = 0L,
    val record: Long = 0L,
    val finDelJuego: Boolean = false,
    val cargando: Boolean = true
)
