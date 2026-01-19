package com.buenhijogames.zzzz.presentacion.pantalla.componentes

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.buenhijogames.zzzz.dominio.caso_uso.ConversorLetras
import com.buenhijogames.zzzz.dominio.caso_uso.Direccion
import com.buenhijogames.zzzz.dominio.modelo.Ficha
import com.buenhijogames.zzzz.ui.theme.FondoTablero
import com.buenhijogames.zzzz.ui.theme.FondoTableroOscuro

/**
 * Composable que representa el tablero 4x4.
 * La detección de gestos se maneja en PantallaJuego para cubrir toda la pantalla.
 */
@Composable
fun TableroComposable(
    tablero: List<List<Ficha?>>,
    conversorLetras: ConversorLetras,
    direccion: Direccion? = null,  // Dirección del movimiento para sincronizar por fila/columna
    modifier: Modifier = Modifier
) {
    val esOscuro = com.buenhijogames.zzzz.ui.theme.LocalEsModoOscuro.current
    val colorFondo = if (esOscuro) FondoTableroOscuro else FondoTablero

    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(colorFondo)
            .padding(8.dp)
    ) {
        val anchoTablero = maxWidth
        val altoTablero = maxHeight
        val espacio = 0.dp // Sin espacio entre celdas para estilo 2048 clásico compacto
        val tamanoCelda = (anchoTablero - (espacio * 3)) / 4 

        // 1. Dibujar el grid de fondo (celdas vacías fijas)
        Column(
            verticalArrangement = Arrangement.spacedBy(espacio)
        ) {
            repeat(4) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(espacio)
                ) {
                    repeat(4) {
                        Box(
                            modifier = Modifier
                                .size(tamanoCelda)
                                .padding(4.dp) // Padding interno para simular separación si espacio es 0
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (esOscuro) com.buenhijogames.zzzz.ui.theme.CeldaVaciaOscura 
                                    else com.buenhijogames.zzzz.ui.theme.CeldaVacia
                                )
                        )
                    }
                }
            }
        }

        // 2. Dibujar las fichas activas con animación de movimiento
        val fichasActivas = remember(tablero) {
            tablero.flatMapIndexed { rowIndex, row ->
                row.mapIndexedNotNull { colIndex, ficha ->
                    if (ficha != null) Triple(ficha, rowIndex, colIndex) else null
                }
            }
        }

        // Calcular duración máxima POR FILA (horizontal) o POR COLUMNA (vertical)
        // Cada fila/columna es un "tren" independiente que se sincroniza internamente
        val esMovimientoHorizontal = direccion == Direccion.IZQUIERDA || direccion == Direccion.DERECHA
        
        // Mapa de duración máxima: clave = índice de fila (horizontal) o columna (vertical)
        val maxDurationPorLinea = remember(fichasActivas, direccion) {
            val duraciones = mutableMapOf<Int, Int>()
            
            fichasActivas.forEach { (ficha, fila, col) ->
                if (ficha.origenesFusion.isNotEmpty()) {
                    // Calcular la distancia máxima de esta ficha
                    val duracionFicha = ficha.origenesFusion.maxOf { origen ->
                        val dist = kotlin.math.max(kotlin.math.abs(col - origen.columna), kotlin.math.abs(fila - origen.fila))
                        dist * 300  // No filtrar dist == 0, la duración mínima se aplica después
                    }
                    // Clave: fila si es horizontal, columna si es vertical
                    val clave = if (esMovimientoHorizontal) fila else col
                    duraciones[clave] = maxOf(duraciones[clave] ?: 0, duracionFicha)
                }
            }
            // Aplicar duración mínima de 300ms a cada línea para evitar animaciones instantáneas
            duraciones.mapValues { maxOf(it.value, 300) }
        }
        
        // Función auxiliar para obtener la duración de una ficha según su posición
        fun obtenerDuracionParaFicha(fila: Int, col: Int): Int {
            val clave = if (esMovimientoHorizontal) fila else col
            return maxDurationPorLinea[clave] ?: 300
        }

        fichasActivas.forEach { (ficha, fila, col) ->
            // Usamos coordenadas exactas de destino
            val targetX = (tamanoCelda + espacio) * col
            val targetY = (tamanoCelda + espacio) * fila

            // Lógica de Animación basada en Orígenes
            when (ficha.origenesFusion.size) {
                // CASO 1: FUSIÓN (2 orígenes) -> Mostrar Ghosts y luego la Real
                2 -> {
                    // Duración sincronizada para esta fila/columna (comportamiento de tren)
                    val duracionFusion = obtenerDuracionParaFicha(fila, col)
                    
                    // Estado para visibilidad secuencial
                    var mostrarGhosts by remember(ficha.id) { mutableStateOf(true) }
                    var mostrarReal by remember(ficha.id) { mutableStateOf(false) }

                    LaunchedEffect(ficha.id) {
                        kotlinx.coroutines.delay(duracionFusion.toLong()) 
                        mostrarGhosts = false
                        mostrarReal = true
                    }

                    if (mostrarGhosts) {
                        ficha.origenesFusion.forEachIndexed { index, origen ->
                            key("ghost_${ficha.id}_${origen.id}_${origen.fila}_${origen.columna}_$index") {
                                val initialX = (tamanoCelda + espacio) * origen.columna
                                val initialY = (tamanoCelda + espacio) * origen.fila
                                // Usar clave única que incluya posición de origen para reiniciar animación
                                val claveAnimacion = "${ficha.id}_${origen.id}_${origen.fila}_${origen.columna}"
                                val progress = remember(claveAnimacion) { androidx.compose.animation.core.Animatable(0f) }
                                
                                // Usar duracionFusion para que todos los ghosts de esta línea se muevan sincronizados
                                LaunchedEffect(claveAnimacion) {
                                    progress.snapTo(0f)  // Asegurar que empieza desde 0
                                    progress.animateTo(
                                        targetValue = 1f,
                                        animationSpec = tween(durationMillis = duracionFusion, easing = androidx.compose.animation.core.LinearEasing)
                                    )
                                }

                                val currentX = initialX + (targetX - initialX) * progress.value
                                val currentY = initialY + (targetY - initialY) * progress.value
                                val valorFantasma = if (ficha.valor > 1) ficha.valor - 1 else 1
                                val fichaFantasma = Ficha(id = origen.id, valor = valorFantasma)

                                Box(modifier = Modifier.zIndex(10f).offset(x = currentX, y = currentY)) {
                                    FichaComposable(ficha = fichaFantasma, conversorLetras = conversorLetras, modifier = Modifier.size(tamanoCelda))
                                }
                            }
                        }
                    }

                    if (mostrarReal) {
                        key(ficha.id) {
                            FichaComposable(
                                ficha = ficha,
                                conversorLetras = conversorLetras,
                                modifier = Modifier.size(tamanoCelda).offset(x = targetX, y = targetY)
                            )
                        }
                    }
                }

                // CASO 2: MOVIMIENTO SIMPLE (1 origen) -> Animar la ficha real desde origen
                1 -> {
                    val origen = ficha.origenesFusion[0]
                    key("move_${ficha.id}_${origen.fila}_${origen.columna}") {
                        val initialX = (tamanoCelda + espacio) * origen.columna
                        val initialY = (tamanoCelda + espacio) * origen.fila
                        
                        // Usar clave única que incluya posición de origen para reiniciar animación
                        val claveAnimacion = "${ficha.id}_${origen.fila}_${origen.columna}_${fila}_${col}"
                        val progress = remember(claveAnimacion) { androidx.compose.animation.core.Animatable(0f) }
                        
                        // Duración sincronizada para esta fila/columna (comportamiento de tren)
                        val duracionLinea = obtenerDuracionParaFicha(fila, col)
                        
                        LaunchedEffect(claveAnimacion) { 
                             progress.snapTo(0f)  // Asegurar que empieza desde 0
                             progress.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(durationMillis = duracionLinea, easing = androidx.compose.animation.core.LinearEasing)
                            )
                        }

                        val currentX = initialX + (targetX - initialX) * progress.value
                        val currentY = initialY + (targetY - initialY) * progress.value

                        FichaComposable(
                            ficha = ficha,
                            conversorLetras = conversorLetras,
                            modifier = Modifier.zIndex(5f).size(tamanoCelda).offset(x = currentX, y = currentY)
                        )
                    }
                }

                // CASO 3: NUEVA O ESTACIONARIA
                else -> {
                    key(ficha.id) {
                        if (ficha.esNueva) {
                            var visible by remember { mutableStateOf(false) }
                            LaunchedEffect(Unit) {
                                // Esperar al movimiento más largo de esta línea antes de aparecer
                                val delayTime = obtenerDuracionParaFicha(fila, col).toLong()
                                kotlinx.coroutines.delay(if (delayTime > 0) delayTime else 200L)
                                visible = true
                            }
                            
                            if (visible) {
                                FichaComposable(
                                    ficha = ficha,
                                    conversorLetras = conversorLetras,
                                    modifier = Modifier.size(tamanoCelda).offset(x = targetX, y = targetY)
                                )
                            }
                        } else {
                            FichaComposable(
                                ficha = ficha,
                                conversorLetras = conversorLetras,
                                modifier = Modifier.size(tamanoCelda).offset(x = targetX, y = targetY)
                            )
                        }
                    }
                }
            }
        }
    }
}

