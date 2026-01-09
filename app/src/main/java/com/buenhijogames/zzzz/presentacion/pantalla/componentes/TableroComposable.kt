package com.buenhijogames.zzzz.presentacion.pantalla.componentes

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
import com.buenhijogames.zzzz.dominio.caso_uso.ConversorLetras
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
    modifier: Modifier = Modifier
) {
    val esOscuro = isSystemInDarkTheme()
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

        fichasActivas.forEach { (ficha, fila, col) ->
            // Determinar si es una fusión para controlar la visibilidad secuencial
            val esFusion = ficha.idsFusionados.isNotEmpty()
            
            // Estado para visibilidad: 
            // - Ghosts: visibles solo durante la animación (0-600ms)
            // - Real: visible solo después de la animación (600ms+)
            var mostrarGhosts by remember(ficha.id) { mutableStateOf(esFusion) }
            var mostrarReal by remember(ficha.id) { mutableStateOf(!esFusion) }

            if (esFusion) {
                LaunchedEffect(ficha.id) {
                    // Esperar lo que dura el movimiento
                    kotlinx.coroutines.delay(600)
                    mostrarGhosts = false
                    mostrarReal = true
                }
            }

            // 1. Renderizar Fichas "Fantasma"
            if (esFusion && mostrarGhosts) {
                ficha.idsFusionados.forEach { idFantasma ->
                    key(idFantasma) { 
                        val targetX = (tamanoCelda + espacio) * col
                        val targetY = (tamanoCelda + espacio) * fila

                        val animatedOffsetX by animateDpAsState(
                            targetValue = targetX,
                            animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing),
                            label = "ghostOffsetX"
                        )
                        
                        val animatedOffsetY by animateDpAsState(
                            targetValue = targetY,
                            animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing),
                            label = "ghostOffsetY"
                        )

                        val valorFantasma = if (ficha.valor > 1) ficha.valor - 1 else 1
                        val fichaFantasma = Ficha(id = idFantasma, valor = valorFantasma)

                        Box(modifier = Modifier.offset(x = animatedOffsetX, y = animatedOffsetY)) {
                             FichaComposable(
                                ficha = fichaFantasma,
                                conversorLetras = conversorLetras,
                                modifier = Modifier.size(tamanoCelda)
                            )
                        }
                    }
                }
            }

            // 2. Renderizar la Ficha Real (Resultado)
            if (mostrarReal) {
                key(ficha.id) {
                    val targetX = (tamanoCelda + espacio) * col
                    val targetY = (tamanoCelda + espacio) * fila

                    val animatedOffsetX by animateDpAsState(
                        targetValue = targetX,
                        animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing),
                        label = "offsetX"
                    )
                    
                    val animatedOffsetY by animateDpAsState(
                        targetValue = targetY,
                        animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing),
                        label = "offsetY"
                    )

                    FichaComposable(
                        ficha = ficha,
                        conversorLetras = conversorLetras,
                        modifier = Modifier
                            .size(tamanoCelda)
                            .offset(x = animatedOffsetX, y = animatedOffsetY)
                    )
                }
            }
        }
    }
}

