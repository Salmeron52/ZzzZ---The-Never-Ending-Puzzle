package com.buenhijogames.zzzz.dominio.modelo

/**
 * Niveles de dificultad del juego.
 *
 * Cada nivel afecta las probabilidades de spawn de nuevas fichas:
 * - NORMAL: Spawn estándar (minVal o minVal+1)
 * - DIFICIL: 5% probabilidad de minVal-1
 * - EXPERTO: 10% probabilidad de minVal-1
 * - MAESTRO: 15% probabilidad de minVal-1
 * - IMPOSIBLE: 20% probabilidad de minVal-1
 */
enum class NivelDificultad(val id: Int, val nombreClave: String) {
    NORMAL(1, "normal"),
    DIFICIL(2, "dificil"),
    EXPERTO(3, "experto"),
    MAESTRO(4, "maestro"),
    IMPOSIBLE(5, "imposible")
}

