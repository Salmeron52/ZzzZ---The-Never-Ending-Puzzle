package com.buenhijogames.zzzz.presentacion.pantalla.componentes

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buenhijogames.zzzz.dominio.caso_uso.ConversorLetras
import com.buenhijogames.zzzz.dominio.modelo.Ficha
import com.buenhijogames.zzzz.ui.theme.CeldaVacia
import com.buenhijogames.zzzz.ui.theme.CeldaVaciaOscura
import com.buenhijogames.zzzz.ui.theme.obtenerColorFicha
import com.buenhijogames.zzzz.ui.theme.obtenerColorTextoFicha

/**
 * Composable que representa una celda del tablero (vacía o con ficha).
 * Incluye animaciones de aparición para fichas nuevas y efecto pop para fusiones.
 */
@Composable
fun FichaComposable(
    ficha: Ficha?,
    conversorLetras: ConversorLetras,
    modifier: Modifier = Modifier
) {
    val esOscuro = isSystemInDarkTheme()
    
    // Animación de color suave (más lenta)
    val colorFondo by animateColorAsState(
        targetValue = if (ficha != null) {
            obtenerColorFicha(ficha.valor, esOscuro)
        } else {
            if (esOscuro) CeldaVaciaOscura else CeldaVacia
        },
        animationSpec = tween(durationMillis = 200),
        label = "colorFondo"
    )
    
    // Estado de escala animada
    var targetScale by remember { mutableFloatStateOf(if (ficha == null) 0.95f else 1f) }
    
    // Animación de aparición para fichas nuevas (más lenta)
    LaunchedEffect(ficha?.id) {
        if (ficha != null && ficha.esNueva) {
            targetScale = 0f // Empieza invisible
            // No delay, animation handles duration
            targetScale = 1f
        } else if (ficha != null && ficha.fusionada) {
            // Efecto pop para fusiones (más pronunciado)
            targetScale = 1.25f
            kotlinx.coroutines.delay(180)  // Más tiempo en estado expandido
            targetScale = 1f
        } else {
            targetScale = if (ficha != null) 1f else 0.95f
        }
    }
    
    val escala by animateFloatAsState(
        targetValue = targetScale,
        animationSpec = tween(durationMillis = 300),
        label = "escala"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .scale(escala)
            .clip(RoundedCornerShape(8.dp))
            .background(colorFondo),
        contentAlignment = Alignment.Center
    ) {
        if (ficha != null) {
            val etiqueta = conversorLetras.obtenerEtiqueta(ficha.valor)
            val colorTexto = obtenerColorTextoFicha(ficha.valor, esOscuro)
            
            // Ajustar tamaño de fuente según longitud del texto
            val tamanoFuente = when {
                etiqueta.length <= 1 -> 32.sp
                etiqueta.length == 2 -> 26.sp
                etiqueta.length == 3 -> 20.sp
                else -> 16.sp
            }
            
            Text(
                text = etiqueta,
                color = colorTexto,
                fontSize = tamanoFuente,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

