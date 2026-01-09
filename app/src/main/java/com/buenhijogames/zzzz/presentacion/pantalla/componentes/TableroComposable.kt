package com.buenhijogames.zzzz.presentacion.pantalla.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.buenhijogames.zzzz.dominio.caso_uso.ConversorLetras
import com.buenhijogames.zzzz.dominio.caso_uso.Direccion
import com.buenhijogames.zzzz.dominio.modelo.Ficha
import com.buenhijogames.zzzz.ui.theme.FondoTablero
import com.buenhijogames.zzzz.ui.theme.FondoTableroOscuro
import kotlin.math.abs

/**
 * Composable que representa el tablero 4x4 con detección de gestos de deslizamiento.
 */
@Composable
fun TableroComposable(
    tablero: List<List<Ficha?>>,
    conversorLetras: ConversorLetras,
    onMover: (Direccion) -> Unit,
    modifier: Modifier = Modifier
) {
    val esOscuro = isSystemInDarkTheme()
    val colorFondo = if (esOscuro) FondoTableroOscuro else FondoTablero
    
    var acumuladoX by remember { mutableFloatStateOf(0f) }
    var acumuladoY by remember { mutableFloatStateOf(0f) }
    
    val umbralGesto = 50f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colorFondo)
            .padding(8.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        acumuladoX = 0f
                        acumuladoY = 0f
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        acumuladoX += dragAmount.x
                        acumuladoY += dragAmount.y
                    },
                    onDragEnd = {
                        val absX = abs(acumuladoX)
                        val absY = abs(acumuladoY)
                        
                        if (absX > umbralGesto || absY > umbralGesto) {
                            val direccion = if (absX > absY) {
                                if (acumuladoX > 0) Direccion.DERECHA else Direccion.IZQUIERDA
                            } else {
                                if (acumuladoY > 0) Direccion.ABAJO else Direccion.ARRIBA
                            }
                            onMover(direccion)
                        }
                    }
                )
            }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            tablero.forEach { fila ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    fila.forEach { ficha ->
                        FichaComposable(
                            ficha = ficha,
                            conversorLetras = conversorLetras,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
