package com.buenhijogames.zzzz.dominio.caso_uso

import com.buenhijogames.zzzz.dominio.modelo.Ficha
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * Dirección de movimiento en el tablero.
 */
enum class Direccion {
    ARRIBA, ABAJO, IZQUIERDA, DERECHA
}

/**
 * Resultado de un movimiento en el juego.
 *
 * @property tablero El nuevo estado del tablero
 * @property puntuacionGanada Puntos ganados en este movimiento
 * @property huboMovimiento Indica si realmente hubo cambio en el tablero
 */
data class ResultadoMovimiento(
    val tablero: List<List<Ficha?>>,
    val puntuacionGanada: Long,
    val huboMovimiento: Boolean
)

/**
 * Contiene toda la lógica del juego: movimientos, fusiones y generación de fichas.
 */
@Singleton
class ReglasJuego @Inject constructor() {

    companion object {
        const val TAMANO_TABLERO = 4
        private const val PROBABILIDAD_FICHA_MAYOR = 0.10
    }

    private var contadorId: Long = 0L

    /**
     * Genera un ID único para cada ficha nueva.
     */
    private fun generarId(): Long = ++contadorId

    /**
     * Crea un tablero vacío de 4x4.
     */
    fun crearTableroVacio(): List<List<Ficha?>> {
        return List(TAMANO_TABLERO) { List(TAMANO_TABLERO) { null } }
    }

    /**
     * Inicializa un nuevo juego con dos fichas iniciales.
     */
    fun inicializarJuego(): List<List<Ficha?>> {
        var tablero = crearTableroVacio()
        tablero = agregarFichaAleatoria(tablero)
        tablero = agregarFichaAleatoria(tablero)
        return tablero
    }

    /**
     * Obtiene el valor mínimo presente en el tablero (ignorando celdas vacías).
     *
     * @return El valor mínimo o 1 si el tablero está vacío
     */
    fun obtenerValorMinimo(tablero: List<List<Ficha?>>): Int {
        val valores = tablero.flatten().filterNotNull().map { it.valor }
        return if (valores.isEmpty()) 1 else valores.minOrNull() ?: 1
    }

    /**
     * Agrega una ficha aleatoria en una posición vacía.
     * Las probabilidades dependen del nivel de dificultad.
     */
    fun agregarFichaAleatoria(
        tablero: List<List<Ficha?>>,
        nivelId: Int = 1
    ): List<List<Ficha?>> {
        val posicionesVacias = mutableListOf<Pair<Int, Int>>()
        for (fila in 0 until TAMANO_TABLERO) {
            for (columna in 0 until TAMANO_TABLERO) {
                if (tablero[fila][columna] == null) {
                    posicionesVacias.add(Pair(fila, columna))
                }
            }
        }

        if (posicionesVacias.isEmpty()) return tablero

        val (fila, columna) = posicionesVacias.random()
        val valorMinimo = obtenerValorMinimo(tablero)
        val valorNuevo = calcularValorNuevoFicha(valorMinimo, nivelId)

        return tablero.mapIndexed { f, filaLista ->
            filaLista.mapIndexed { c, ficha ->
                if (f == fila && c == columna) {
                    Ficha(id = generarId(), valor = valorNuevo, esNueva = true)
                } else {
                    // Limpiar flags de animación de fichas existentes
                    ficha?.copy(esNueva = false, fusionada = false)
                }
            }
        }
    }

    /**
     * Calcula el valor de la nueva ficha según el nivel de dificultad.
     *
     * Nivel 1 (Normal): 90% minVal, 10% minVal+1
     * Nivel 2 (Difícil): 85% minVal, 5% minVal-1, 10% minVal+1
     * Nivel 3 (Experto): 80% minVal, 10% minVal-1, 10% minVal+1
     * Nivel 4 (Maestro): 75% minVal, 15% minVal-1, 10% minVal+1
     * Nivel 5 (Imposible): 70% minVal, 20% minVal-1, 10% minVal+1
     */
    private fun calcularValorNuevoFicha(valorMinimo: Int, nivelId: Int): Int {
        val probabilidad = Random.nextDouble()

        return when (nivelId) {
            1 -> {
                // Nivel Normal: 90% minVal, 10% minVal+1
                if (probabilidad < 0.90) valorMinimo else valorMinimo + 1
            }
            2 -> {
                // Nivel Difícil: 85% minVal, 5% minVal-1, 10% minVal+1
                when {
                    probabilidad < 0.85 -> valorMinimo
                    probabilidad < 0.90 && valorMinimo > 1 -> valorMinimo - 1
                    probabilidad < 0.90 -> valorMinimo
                    else -> valorMinimo + 1
                }
            }
            3 -> {
                // Nivel Experto: 80% minVal, 10% minVal-1, 10% minVal+1
                when {
                    probabilidad < 0.80 -> valorMinimo
                    probabilidad < 0.90 && valorMinimo > 1 -> valorMinimo - 1
                    probabilidad < 0.90 -> valorMinimo
                    else -> valorMinimo + 1
                }
            }
            4 -> {
                // Nivel Maestro: 75% minVal, 15% minVal-1, 10% minVal+1
                when {
                    probabilidad < 0.75 -> valorMinimo
                    probabilidad < 0.90 && valorMinimo > 1 -> valorMinimo - 1
                    probabilidad < 0.90 -> valorMinimo
                    else -> valorMinimo + 1
                }
            }
            5 -> {
                // Nivel Imposible: 70% minVal, 20% minVal-1, 10% minVal+1
                when {
                    probabilidad < 0.70 -> valorMinimo
                    probabilidad < 0.90 && valorMinimo > 1 -> valorMinimo - 1
                    probabilidad < 0.90 -> valorMinimo
                    else -> valorMinimo + 1
                }
            }
            else -> valorMinimo
        }
    }

    /**
     * Ejecuta un movimiento en la dirección especificada.
     */
    /**
     * Compara si dos fichas son funcionalmente diferentes (ignora flags de animación).
     * Retorna true si hubo un cambio real (movimiento o fusión).
     */
    private fun fichasSonDiferentes(f1: Ficha?, f2: Ficha?): Boolean {
        if (f1 == null && f2 == null) return false
        if (f1 == null || f2 == null) return true
        // Si el ID es el mismo, es la misma ficha (aunque hayan cambiado flags)
        // Si hubo fusión, el ID cambia, así que retornará true
        return f1.id != f2.id || f1.valor != f2.valor
    }

    /**
     * Ejecuta un movimiento en la dirección especificada.
     */
    fun mover(tablero: List<List<Ficha?>>, direccion: Direccion): ResultadoMovimiento {
        val tableroMutable = tablero.map { it.toMutableList() }.toMutableList()
        var puntuacionGanada = 0L
        var huboMovimiento = false

        when (direccion) {
            Direccion.ARRIBA -> {
                for (columna in 0 until TAMANO_TABLERO) {
                    val resultado = procesarLinea(
                        (0 until TAMANO_TABLERO).map { tableroMutable[it][columna] }
                    )
                    for (fila in 0 until TAMANO_TABLERO) {
                        if (fichasSonDiferentes(tableroMutable[fila][columna], resultado.linea[fila])) {
                            huboMovimiento = true
                        }
                        tableroMutable[fila][columna] = resultado.linea[fila]
                    }
                    puntuacionGanada += resultado.puntuacion
                }
            }
            Direccion.ABAJO -> {
                for (columna in 0 until TAMANO_TABLERO) {
                    val resultado = procesarLinea(
                        (TAMANO_TABLERO - 1 downTo 0).map { tableroMutable[it][columna] }
                    )
                    for ((indice, fila) in (TAMANO_TABLERO - 1 downTo 0).withIndex()) {
                        if (fichasSonDiferentes(tableroMutable[fila][columna], resultado.linea[indice])) {
                            huboMovimiento = true
                        }
                        tableroMutable[fila][columna] = resultado.linea[indice]
                    }
                    puntuacionGanada += resultado.puntuacion
                }
            }
            Direccion.IZQUIERDA -> {
                for (fila in 0 until TAMANO_TABLERO) {
                    val resultado = procesarLinea(tableroMutable[fila].toList())
                    for (columna in 0 until TAMANO_TABLERO) {
                        if (fichasSonDiferentes(tableroMutable[fila][columna], resultado.linea[columna])) {
                            huboMovimiento = true
                        }
                    }
                    // if (tableroMutable[fila] != resultado.linea) { ... } // Reemplazado por loop para precisión
                    tableroMutable[fila] = resultado.linea.toMutableList()
                    puntuacionGanada += resultado.puntuacion
                }
            }
            Direccion.DERECHA -> {
                for (fila in 0 until TAMANO_TABLERO) {
                    val resultado = procesarLinea(tableroMutable[fila].reversed())
                    val lineaInvertida = resultado.linea.reversed()
                    for (columna in 0 until TAMANO_TABLERO) {
                         if (fichasSonDiferentes(tableroMutable[fila][columna], lineaInvertida[columna])) {
                            huboMovimiento = true
                        }
                    }
                    tableroMutable[fila] = lineaInvertida.toMutableList()
                    puntuacionGanada += resultado.puntuacion
                }
            }
        }

        return ResultadoMovimiento(
            tablero = tableroMutable.map { it.toList() },
            puntuacionGanada = puntuacionGanada,
            huboMovimiento = huboMovimiento
        )
    }

    /**
     * Procesa una línea (fila o columna) para mover y fusionar fichas.
     */
    private data class ResultadoLinea(val linea: List<Ficha?>, val puntuacion: Long)

    private fun procesarLinea(linea: List<Ficha?>): ResultadoLinea {
        // Filtrar las fichas no nulas
        val fichas = linea.filterNotNull().toMutableList()
        val resultado = mutableListOf<Ficha?>()
        var puntuacion = 0L
        var i = 0

        while (i < fichas.size) {
            if (i + 1 < fichas.size && fichas[i].valor == fichas[i + 1].valor) {
                // Fusionar las dos fichas
                val nuevoValor = fichas[i].valor + 1
                resultado.add(Ficha(id = generarId(), valor = nuevoValor, fusionada = true))
                puntuacion += nuevoValor.toLong()
                i += 2
            } else {
                // Mover ficha sin fusionar (limpiar flags de animación)
                resultado.add(fichas[i].copy(esNueva = false, fusionada = false))
                i++
            }
        }

        // Rellenar con nulos hasta completar el tamaño
        while (resultado.size < TAMANO_TABLERO) {
            resultado.add(null)
        }

        return ResultadoLinea(resultado, puntuacion)
    }

    /**
     * Verifica si el juego ha terminado (no hay movimientos posibles).
     */
    fun esFinDeJuego(tablero: List<List<Ficha?>>): Boolean {
        // Si hay celdas vacías, el juego continúa
        if (tablero.any { fila -> fila.any { it == null } }) {
            return false
        }

        // Verificar si hay fusiones posibles en filas
        for (fila in 0 until TAMANO_TABLERO) {
            for (columna in 0 until TAMANO_TABLERO - 1) {
                if (tablero[fila][columna]?.valor == tablero[fila][columna + 1]?.valor) {
                    return false
                }
            }
        }

        // Verificar si hay fusiones posibles en columnas
        for (columna in 0 until TAMANO_TABLERO) {
            for (fila in 0 until TAMANO_TABLERO - 1) {
                if (tablero[fila][columna]?.valor == tablero[fila + 1][columna]?.valor) {
                    return false
                }
            }
        }

        return true
    }

    /**
     * Reinicia el contador de IDs (usar al cargar partida guardada).
     */
    fun establecerContadorId(nuevoContador: Long) {
        contadorId = nuevoContador
    }
}
