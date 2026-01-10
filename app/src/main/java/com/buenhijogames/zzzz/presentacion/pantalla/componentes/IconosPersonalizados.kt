package com.buenhijogames.zzzz.presentacion.pantalla.componentes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Icono personalizado de deshacer: flecha curva hacia la izquierda con punta hacia la derecha.
 * Similar al icono clásico de Undo.
 */
@Composable
fun IconoDeshacer(
    color: Color,
    size: Dp = 28.dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val anchura = this.size.width
        val altura = this.size.height
        val grosor = anchura * 0.12f

        // Arco circular que va desde la derecha, hacia arriba, hacia la izquierda
        val path = Path().apply {
            // Punto inicial a la derecha abajo
            moveTo(anchura * 0.8f, altura * 0.65f)
            // Curva hacia arriba y luego hacia la izquierda
            cubicTo(
                anchura * 0.8f, altura * 0.35f,
                anchura * 0.55f, altura * 0.25f,
                anchura * 0.25f, altura * 0.35f
            )
        }

        drawPath(
            path = path,
            color = color,
            style = Stroke(width = grosor, cap = StrokeCap.Round)
        )

        // Punta de flecha apuntando hacia la derecha (en el extremo izquierdo del arco)
        val flechaPath = Path().apply {
            moveTo(anchura * 0.35f, altura * 0.2f)
            lineTo(anchura * 0.2f, altura * 0.38f)
            lineTo(anchura * 0.4f, altura * 0.45f)
            close()
        }

        drawPath(
            path = flechaPath,
            color = color
        )
    }
}

/**
 * Icono personalizado de nueva partida: tablero 2x2 con símbolo "+".
 */
@Composable
fun IconoNuevaPartida(
    color: Color,
    size: Dp = 28.dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val anchura = this.size.width
        val altura = this.size.height
        val grosor = anchura * 0.08f
        val radio = anchura * 0.06f
        val margen = anchura * 0.08f
        val celda = (anchura - margen * 2 - grosor) / 2

        // Dibujar 4 celdas como un mini tablero
        val colores = listOf(
            Offset(margen, margen),
            Offset(margen + celda + grosor, margen),
            Offset(margen, margen + celda + grosor),
            Offset(margen + celda + grosor, margen + celda + grosor)
        )

        colores.forEach { offset ->
            drawRoundRect(
                color = color.copy(alpha = 0.4f),
                topLeft = offset,
                size = Size(celda, celda),
                cornerRadius = CornerRadius(radio, radio)
            )
        }

        // Símbolo "+" en el centro
        val centroX = anchura / 2
        val centroY = altura / 2
        val tamanoPlus = anchura * 0.25f

        // Línea vertical del "+"
        drawLine(
            color = color,
            start = Offset(centroX, centroY - tamanoPlus),
            end = Offset(centroX, centroY + tamanoPlus),
            strokeWidth = grosor * 1.5f,
            cap = StrokeCap.Round
        )

        // Línea horizontal del "+"
        drawLine(
            color = color,
            start = Offset(centroX - tamanoPlus, centroY),
            end = Offset(centroX + tamanoPlus, centroY),
            strokeWidth = grosor * 1.5f,
            cap = StrokeCap.Round
        )
    }
}

/**
 * Icono de guardar partida: diskette estilizado.
 */
@Composable
fun IconoGuardar(
    color: Color,
    size: Dp = 28.dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val anchura = this.size.width
        val altura = this.size.height
        val margen = anchura * 0.15f
        val radio = anchura * 0.08f

        // Cuerpo del diskette
        drawRoundRect(
            color = color,
            topLeft = Offset(margen, margen),
            size = Size(anchura - margen * 2, altura - margen * 2),
            cornerRadius = CornerRadius(radio, radio)
        )

        // Ranura superior (etiqueta)
        drawRoundRect(
            color = Color.White.copy(alpha = 0.9f),
            topLeft = Offset(anchura * 0.3f, margen),
            size = Size(anchura * 0.4f, altura * 0.25f),
            cornerRadius = CornerRadius(radio / 2, radio / 2)
        )

        // Área de datos inferior
        drawRoundRect(
            color = Color.White.copy(alpha = 0.7f),
            topLeft = Offset(anchura * 0.25f, altura * 0.5f),
            size = Size(anchura * 0.5f, altura * 0.3f),
            cornerRadius = CornerRadius(radio / 2, radio / 2)
        )
    }
}

/**
 * Icono de menú/home: casa estilizada.
 */
@Composable
fun IconoMenu(
    color: Color,
    size: Dp = 28.dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val anchura = this.size.width
        val altura = this.size.height
        val grosor = anchura * 0.1f

        // Techo triangular
        val techoPath = Path().apply {
            moveTo(anchura * 0.5f, altura * 0.15f)
            lineTo(anchura * 0.1f, altura * 0.45f)
            lineTo(anchura * 0.9f, altura * 0.45f)
            close()
        }

        drawPath(
            path = techoPath,
            color = color
        )

        // Cuerpo de la casa
        drawRoundRect(
            color = color,
            topLeft = Offset(anchura * 0.2f, altura * 0.45f),
            size = Size(anchura * 0.6f, altura * 0.4f),
            cornerRadius = CornerRadius(anchura * 0.05f, anchura * 0.05f)
        )

        // Puerta
        drawRoundRect(
            color = Color.White.copy(alpha = 0.8f),
            topLeft = Offset(anchura * 0.38f, altura * 0.55f),
            size = Size(anchura * 0.24f, altura * 0.3f),
            cornerRadius = CornerRadius(anchura * 0.03f, anchura * 0.03f)
        )
    }
}

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
