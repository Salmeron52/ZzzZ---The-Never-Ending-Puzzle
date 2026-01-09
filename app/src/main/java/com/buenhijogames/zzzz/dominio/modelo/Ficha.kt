package com.buenhijogames.zzzz.dominio.modelo

/**
 * Representa una ficha individual en el tablero del juego.
 *
 * @property id Identificador único de la ficha para animaciones
 * @property valor Valor numérico de la ficha (1 = A, 2 = B, ..., 26 = Z, 27 = AA, etc.)
 */
data class Ficha(
    val id: Long,
    val valor: Int
)
