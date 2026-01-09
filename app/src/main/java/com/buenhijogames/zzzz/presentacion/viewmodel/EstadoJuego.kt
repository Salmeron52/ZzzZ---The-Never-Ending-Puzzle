package com.buenhijogames.zzzz.presentacion.viewmodel

import com.buenhijogames.zzzz.dominio.modelo.Ficha
import com.buenhijogames.zzzz.dominio.modelo.NivelDificultad

/**
 * Snapshot del estado para poder deshacer.
 */
data class SnapshotEstado(
    val tablero: List<List<Ficha?>>,
    val puntuacion: Long
)

/**
 * Estado de la UI del juego.
 *
 * @property tablero Estado actual del tablero 4x4
 * @property puntuacion Puntuación actual
 * @property record Récord máximo alcanzado
 * @property finDelJuego Indica si el juego ha terminado
 * @property cargando Indica si se está cargando la partida
 * @property puedeDeshacer Indica si se puede deshacer el último movimiento
 * @property estadoAnterior Snapshot del estado antes del último movimiento
 * @property partidaId ID de la partida guardada actual (null si es nueva)
 * @property nivelActual Nivel de dificultad actual
 */
data class EstadoJuego(
    val tablero: List<List<Ficha?>> = emptyList(),
    val puntuacion: Long = 0L,
    val record: Long = 0L,
    val finDelJuego: Boolean = false,
    val cargando: Boolean = true,
    val puedeDeshacer: Boolean = false,
    val estadoAnterior: SnapshotEstado? = null,
    val partidaId: Long? = null,
    val nivelActual: NivelDificultad = NivelDificultad.NORMAL
)


