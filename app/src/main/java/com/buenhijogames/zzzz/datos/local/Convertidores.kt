package com.buenhijogames.zzzz.datos.local

import com.buenhijogames.zzzz.dominio.caso_uso.ReglasJuego
import com.buenhijogames.zzzz.dominio.modelo.Ficha

/**
 * Convertidor para serializar/deserializar el tablero a JSON.
 * Usa formato simple sin dependencias externas.
 */
object Convertidores {

    /**
     * Convierte el tablero a una representación JSON simple.
     * Formato: filas separadas por "|", celdas por ",", cada celda es "id:valor" o "0" para vacío.
     */
    fun tableroAJson(tablero: List<List<Ficha?>>): String {
        return tablero.joinToString("|") { fila ->
            fila.joinToString(",") { ficha ->
                if (ficha != null) {
                    "${ficha.id}:${ficha.valor}"
                } else {
                    "0"
                }
            }
        }
    }

    /**
     * Convierte una representación JSON al tablero.
     */
    fun jsonATablero(json: String): List<List<Ficha?>> {
        if (json.isEmpty()) {
            return List(ReglasJuego.TAMANO_TABLERO) { List(ReglasJuego.TAMANO_TABLERO) { null } }
        }

        return runCatching {
            json.split("|").map { filaStr ->
                filaStr.split(",").map { celdaStr ->
                    if (celdaStr == "0") {
                        null
                    } else {
                        val partes = celdaStr.split(":")
                        Ficha(
                            id = partes[0].toLong(),
                            valor = partes[1].toInt()
                        )
                    }
                }
            }
        }.getOrElse {
            List(ReglasJuego.TAMANO_TABLERO) { List(ReglasJuego.TAMANO_TABLERO) { null } }
        }
    }

    /**
     * Obtiene el máximo ID de ficha en el tablero para restaurar el contador.
     */
    fun obtenerMaximoId(tablero: List<List<Ficha?>>): Long {
        return tablero.flatten().filterNotNull().maxOfOrNull { it.id } ?: 0L
    }
}
