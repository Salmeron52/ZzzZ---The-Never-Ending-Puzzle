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
 * Icono personalizado de deshacer: flecha curva hacia atrás con estilo de "rebobinar".
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
        
        // Arco circular (flecha de retroceso)
        val path = Path().apply {
            // Punto inicial arriba a la derecha
            moveTo(anchura * 0.75f, altura * 0.25f)
            // Arco hacia la izquierda y abajo
            cubicTo(
                anchura * 0.35f, altura * 0.1f,
                anchura * 0.2f, altura * 0.45f,
                anchura * 0.35f, altura * 0.7f
            )
        }
        
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = grosor, cap = StrokeCap.Round)
        )
        
        // Punta de flecha en la parte superior
        val flechaPath = Path().apply {
            moveTo(anchura * 0.75f, altura * 0.1f)
            lineTo(anchura * 0.75f, altura * 0.38f)
            lineTo(anchura * 0.52f, altura * 0.24f)
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
