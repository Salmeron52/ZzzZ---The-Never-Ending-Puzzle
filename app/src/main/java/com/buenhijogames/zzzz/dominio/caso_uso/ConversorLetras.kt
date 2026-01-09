package com.buenhijogames.zzzz.dominio.caso_uso

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Convierte valores enteros a representación de letras usando Base 26 Biyectiva.
 *
 * Ejemplos:
 * - 1 → "A"
 * - 26 → "Z"
 * - 27 → "AA"
 * - 52 → "AZ"
 * - 53 → "BA"
 * - 475254 → "ZzzZ" (caso especial, nombre del juego)
 */
@Singleton
class ConversorLetras @Inject constructor() {

    companion object {
        // Valor que corresponde a ZZZZ en base 26 biyectiva
        private const val VALOR_ZZZZ = 475254
    }

    /**
     * Convierte un valor entero a su representación en letras.
     *
     * @param valor El valor a convertir (debe ser > 0)
     * @return La representación en letras del valor
     */
    fun obtenerEtiqueta(valor: Int): String {
        if (valor <= 0) return ""
        
        // Caso especial: ZZZZ se muestra como ZzzZ (nombre del juego)
        if (valor == VALOR_ZZZZ) return "ZzzZ"
        
        val sb = StringBuilder()
        var n = valor
        while (n > 0) {
            n--
            sb.insert(0, (('A'.code + (n % 26)).toChar()))
            n /= 26
        }
        return sb.toString()
    }
}

