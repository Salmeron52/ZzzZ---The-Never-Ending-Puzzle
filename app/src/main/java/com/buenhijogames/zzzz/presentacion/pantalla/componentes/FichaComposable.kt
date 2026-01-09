package com.buenhijogames.zzzz.presentacion.pantalla.componentes

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.runtime.getValue
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
 */
@Composable
fun FichaComposable(
    ficha: Ficha?,
    conversorLetras: ConversorLetras,
    modifier: Modifier = Modifier
) {
    val esOscuro = isSystemInDarkTheme()
    
    val colorFondo by animateColorAsState(
        targetValue = if (ficha != null) {
            obtenerColorFicha(ficha.valor, esOscuro)
        } else {
            if (esOscuro) CeldaVaciaOscura else CeldaVacia
        },
        animationSpec = tween(durationMillis = 150),
        label = "colorFondo"
    )
    
    val escala by animateFloatAsState(
        targetValue = if (ficha != null) 1f else 0.95f,
        animationSpec = tween(durationMillis = 100),
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
