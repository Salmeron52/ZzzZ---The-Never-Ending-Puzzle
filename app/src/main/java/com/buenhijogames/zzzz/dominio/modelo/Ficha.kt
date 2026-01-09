package com.buenhijogames.zzzz.dominio.modelo

/**
 * Representa una ficha individual en el tablero del juego.
 *
 * @property id Identificador único de la ficha para animaciones
 * @property valor Valor numérico de la ficha (1 = A, 2 = B, ..., 26 = Z, 27 = AA, etc.)
 * @property esNueva Indica si la ficha acaba de ser creada (para animación de aparición)
 * @property fusionada Indica si la ficha es resultado de una fusión (para animación de pop)
 */
data class Ficha(
    val id: Long,
    val valor: Int,
    val esNueva: Boolean = false,
    val fusionada: Boolean = false,
    val idsFusionados: List<Long> = emptyList() // IDs de las fichas que se fusionaron para crear esta
)

