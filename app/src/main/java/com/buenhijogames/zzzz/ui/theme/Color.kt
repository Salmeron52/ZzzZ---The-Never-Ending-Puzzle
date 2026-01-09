package com.buenhijogames.zzzz.ui.theme

import androidx.compose.ui.graphics.Color

// Colores del tema
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Colores del juego
val FondoTablero = Color(0xFFBBADA0)
val FondoTableroOscuro = Color(0xFF4A4458)
val CeldaVacia = Color(0xFFCDC1B4)
val CeldaVaciaOscura = Color(0xFF635C70)

/**
 * Genera un color para una ficha basado en su valor.
 * Usa HSL para crear colores armoniosos que progresan visualmente.
 */
fun obtenerColorFicha(valor: Int, esOscuro: Boolean): Color {
    // Cicla a través del espectro de colores
    val hue = ((valor - 1) * 25f) % 360f
    val saturation = if (esOscuro) 0.6f else 0.7f
    val lightness = if (esOscuro) 0.35f else 0.75f
    
    return Color.hsl(hue, saturation, lightness)
}

/**
 * Obtiene el color del texto de una ficha para contraste óptimo.
 */
fun obtenerColorTextoFicha(valor: Int, esOscuro: Boolean): Color {
    return if (esOscuro) {
        Color.White
    } else {
        if (valor <= 2) Color(0xFF776E65) else Color.White
    }
}