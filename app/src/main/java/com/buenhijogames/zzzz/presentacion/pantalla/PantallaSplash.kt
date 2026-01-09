package com.buenhijogames.zzzz.presentacion.pantalla

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.buenhijogames.zzzz.R
import com.buenhijogames.zzzz.presentacion.viewmodel.SplashViewModel
import kotlinx.coroutines.delay

@Composable
fun PantallaSplash(
    onNavegar: (String) -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        val minSplashTime = 2000L
        val start = System.currentTimeMillis()

        // Obtener destino de forma asíncrona
        val destino = viewModel.determinarDestino()

        // Calcular tiempo restante para cumplir el mínimo de 2 segundos
        val elapsed = System.currentTimeMillis() - start
        val delayTime = maxOf(0L, minSplashTime - elapsed)

        delay(delayTime)
        onNavegar(destino)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), // Fondo negro OLED
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp) // Margen general seguro
        ) {
            Spacer(modifier = Modifier.weight(0.1f)) // Pequeño espacio superior flexible

            // Texto "buenhijoGames presenta"
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000)) +
                        slideInVertically(initialOffsetY = { -40 }, animationSpec = tween(durationMillis = 1000))
            ) {
                // Caja para asegurar centrado y permitir tamaños grandes de texto
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(R.string.splash_presents),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
                        fontSize = 28.sp, // Texto significativamente más grande
                        fontWeight = FontWeight.Normal, // Ligeramente más grueso para legibilidad
                        fontStyle = FontStyle.Italic,
                        letterSpacing = 2.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Logo
             AnimatedVisibility(
                visible = true,
                modifier = Modifier
                    .weight(0.8f) // Ocupa la mayor parte del espacio (80% aprox del disponible restante)
                    .fillMaxWidth(),
                enter = fadeIn(animationSpec = tween(durationMillis = 1200, delayMillis = 300)) +
                        scaleIn(initialScale = 0.8f, animationSpec = tween(durationMillis = 1000, delayMillis = 300))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.splash_logo),
                    contentDescription = "Logo",
                    contentScale = androidx.compose.ui.layout.ContentScale.Fit, // Se adapta sin deformarse
                    modifier = Modifier.fillMaxSize() // Llena el espacio asignado por el weight
                )
            }

            Spacer(modifier = Modifier.weight(0.1f)) // Espacio inferior para balancear
        }
    }
}
