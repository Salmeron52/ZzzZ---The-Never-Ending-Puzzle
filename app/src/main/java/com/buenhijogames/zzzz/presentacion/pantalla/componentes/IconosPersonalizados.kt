package com.buenhijogames.zzzz.presentacion.pantalla.componentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Los iconos de acción (Deshacer, Nuevo Juego, etc.) han sido sustituidos 
 * por iconos estándar de Material Design para mejorar la claridad.
 * Se mantienen SolIcono y LunaIcono para el selector de tema.
 */

@Composable
fun SolIcono(
    color: Color,
    size: Dp = 28.dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val radio = this.size.width * 0.25f
        val rayLength = this.size.width * 0.45f

        // Sol central
        drawCircle(color = color, radius = radio, center = center)

        // Rayos
        for (i in 0 until 8) {
            val angle = i * 45.0 * (Math.PI / 180)
            val start = Offset(
                x = center.x + (radio * 1.4f) * kotlin.math.cos(angle).toFloat(),
                y = center.y + (radio * 1.4f) * kotlin.math.sin(angle).toFloat()
            )
            val end = Offset(
                x = center.x + rayLength * kotlin.math.cos(angle).toFloat(),
                y = center.y + rayLength * kotlin.math.sin(angle).toFloat()
            )
            drawLine(
                color = color,
                start = start,
                end = end,
                strokeWidth = this.size.width * 0.08f,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun LunaIcono(
    color: Color,
    size: Dp = 28.dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val width = this.size.width
        val height = this.size.height

        val path = Path().apply {
            moveTo(width * 0.5f, height * 0.1f)
            cubicTo(
                width * 1.2f, height * 0.1f, // Control point 1 external
                width * 1.2f, height * 0.9f, // Control point 2 external
                width * 0.5f, height * 0.9f  // End point
            )
            cubicTo(
                width * 0.8f, height * 0.7f, // Control point 2 internal (concave)
                width * 0.8f, height * 0.3f, // Control point 1 internal (concave)
                width * 0.5f, height * 0.1f  // Back to start
            )
            close()
        }

        drawPath(path = path, color = color)
    }
}
