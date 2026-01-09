package com.buenhijogames.zzzz.presentacion.pantalla.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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



    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(colorFondo)
            .padding(8.dp)
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

