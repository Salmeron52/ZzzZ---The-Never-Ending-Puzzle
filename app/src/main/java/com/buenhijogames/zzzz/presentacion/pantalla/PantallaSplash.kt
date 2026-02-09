package com.buenhijogames.zzzz.presentacion.pantalla

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.buenhijogames.zzzz.R
import com.buenhijogames.zzzz.presentacion.viewmodel.SplashViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

// Colores premium para el splash
private val GoldLight = Color(0xFFFFD700)
private val CyanBright = Color(0xFF00FFFF)
private val MagentaBright = Color(0xFFFF00FF)
private val PurpleDeep = Color(0xFF9B59B6)
private val BlueElectric = Color(0xFF3498DB)

// Clase para representar una partícula/estrella
private data class Particle(
    val x: Float,           // Posición X (0-1)
    val startY: Float,      // Posición Y inicial (0-1)
    val size: Float,        // Tamaño del punto
    val speed: Float,       // Velocidad de movimiento
    val alpha: Float,       // Transparencia
    val twinkleSpeed: Float // Velocidad de parpadeo
)

// Genera partículas aleatorias
private fun generateParticles(count: Int): List<Particle> {
    return List(count) {
        Particle(
            x = Random.nextFloat(),
            startY = Random.nextFloat(),
            size = Random.nextFloat() * 3f + 1f,  // 1-4 dp
            speed = Random.nextFloat() * 0.15f + 0.05f,  // Velocidad variable
            alpha = Random.nextFloat() * 0.6f + 0.2f,  // 0.2-0.8 alpha
            twinkleSpeed = Random.nextFloat() * 2f + 1f  // Para el parpadeo
        )
    }
}

@Composable
fun PantallaSplash(
    onNavegar: (String) -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    // Estados para animaciones escalonadas
    var showStudio by remember { mutableStateOf(false) }
    var showPresents by remember { mutableStateOf(false) }
    var showTitle by remember { mutableStateOf(false) }
    var showLogo by remember { mutableStateOf(false) }
    var showButton by remember { mutableStateOf(false) }
    
    // Partículas del fondo
    val particles = remember { generateParticles(50) }
    
    // Destino precalculado
    var destino by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()
    
    // Animación infinita para el gradiente del título
    val infiniteTransition = rememberInfiniteTransition(label = "gradient")
    val gradientOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradientShift"
    )
    
    // Animación de brillo pulsante
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    // Animación de escala pulsante para el botón
    val buttonScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "buttonPulse"
    )
    
    // Animación para el movimiento de partículas (0 a 1 en loop)
    val particleProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particles"
    )
    
    // Animación para el parpadeo de las estrellas
    val twinkle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "twinkle"
    )
    
    LaunchedEffect(Unit) {
        // Animaciones escalonadas
        delay(100)
        showStudio = true
        delay(400)
        showPresents = true
        delay(500)
        showTitle = true
        delay(400)
        showLogo = true
        
        // Obtener destino mientras se muestran las animaciones
        destino = viewModel.determinarDestino()
        
        delay(600)
        showButton = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1a1a2e),
                        Color(0xFF16213e),
                        Color(0xFF0f0f23),
                        Color.Black
                    ),
                    radius = 1200f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // ═══════════════════════════════════════════════════════════
        // CAPA DE PARTÍCULAS ESTELARES - Fondo animado
        // ═══════════════════════════════════════════════════════════
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            
            particles.forEach { particle ->
                // Calcular posición Y con movimiento hacia arriba
                val rawY = particle.startY - (particleProgress * particle.speed * 5f)
                val y = ((rawY % 1f) + 1f) % 1f  // Wrap around
                
                // Calcular parpadeo individual
                val individualTwinkle = kotlin.math.sin(
                    (twinkle * particle.twinkleSpeed * Math.PI * 2).toFloat()
                ) * 0.3f + 0.7f
                
                val finalAlpha = particle.alpha * individualTwinkle
                
                drawCircle(
                    color = Color.White.copy(alpha = finalAlpha),
                    radius = particle.size,
                    center = Offset(
                        x = particle.x * width,
                        y = y * height
                    )
                )
            }
        }
        
        // ═══════════════════════════════════════════════════════════
        // CONTENIDO PRINCIPAL
        // ═══════════════════════════════════════════════════════════
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Spacer(modifier = Modifier.weight(0.12f))

            // ═══════════════════════════════════════════════════════════
            // LÍNEA 1: "buenhijoGames" - Elegante y sofisticado
            // ═══════════════════════════════════════════════════════════
            AnimatedVisibility(
                visible = showStudio,
                enter = fadeIn(animationSpec = tween(800)) +
                        slideInVertically(
                            initialOffsetY = { -60 },
                            animationSpec = tween(800, easing = FastOutSlowInEasing)
                        )
            ) {
                Text(
                    text = "buenhijoGames",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Light,
                        letterSpacing = 6.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        shadow = Shadow(
                            color = CyanBright.copy(alpha = 0.3f),
                            offset = Offset(0f, 0f),
                            blurRadius = 10f
                        )
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ═══════════════════════════════════════════════════════════
            // LÍNEA 2: "presenta" - Sutil y minimalista
            // ═══════════════════════════════════════════════════════════
            AnimatedVisibility(
                visible = showPresents,
                enter = fadeIn(animationSpec = tween(600))
            ) {
                Text(
                    text = stringResource(R.string.splash_presents_action),
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 4.sp,
                        color = GoldLight.copy(alpha = 0.6f)
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ═══════════════════════════════════════════════════════════
            // LÍNEA 3: "El Puzzle Interminable" - ESPECTACULAR
            // ═══════════════════════════════════════════════════════════
            AnimatedVisibility(
                visible = showTitle,
                enter = fadeIn(animationSpec = tween(1000)) +
                        scaleIn(
                            initialScale = 0.7f,
                            animationSpec = tween(1000, easing = FastOutSlowInEasing)
                        )
            ) {
                // Gradiente animado para el título principal
                val animatedColors = listOf(
                    CyanBright,
                    BlueElectric,
                    PurpleDeep,
                    MagentaBright,
                    CyanBright
                )
                
                Text(
                    text = stringResource(R.string.splash_title_main),
                    style = TextStyle(
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp,
                        brush = Brush.linearGradient(
                            colors = animatedColors,
                            start = Offset(gradientOffset * 500f, 0f),
                            end = Offset(gradientOffset * 500f + 400f, 100f)
                        ),
                        shadow = Shadow(
                            color = CyanBright.copy(alpha = glowAlpha * 0.6f),
                            offset = Offset(0f, 0f),
                            blurRadius = 20f
                        )
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ═══════════════════════════════════════════════════════════
            // LOGO - Con efecto de aparición elegante
            // ═══════════════════════════════════════════════════════════
            AnimatedVisibility(
                visible = showLogo,
                modifier = Modifier
                    .weight(0.45f)
                    .fillMaxWidth(),
                enter = fadeIn(animationSpec = tween(1200)) +
                        scaleIn(
                            initialScale = 0.85f,
                            animationSpec = tween(1000, easing = FastOutSlowInEasing)
                        )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Glow effect detrás del logo
                    Box(
                        modifier = Modifier
                            .fillMaxSize(0.7f)
                            .alpha(glowAlpha * 0.15f)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        CyanBright,
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                    
                    Image(
                        painter = painterResource(id = R.drawable.splash_logo),
                        contentDescription = "Logo",
                        contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                        modifier = Modifier.fillMaxSize(0.85f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ═══════════════════════════════════════════════════════════
            // BOTÓN ELEGANTE - Flecha circular con efecto pulsante
            // ═══════════════════════════════════════════════════════════
            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn(animationSpec = tween(800)) +
                        scaleIn(
                            initialScale = 0.5f,
                            animationSpec = tween(600, easing = FastOutSlowInEasing)
                        )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .scale(buttonScale)
                        .size(72.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    CyanBright.copy(alpha = 0.3f),
                                    PurpleDeep.copy(alpha = 0.3f)
                                )
                            ),
                            shape = CircleShape
                        )
                        .border(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    CyanBright.copy(alpha = glowAlpha),
                                    MagentaBright.copy(alpha = glowAlpha)
                                )
                            ),
                            shape = CircleShape
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            destino?.let { dest ->
                                scope.launch {
                                    onNavegar(dest)
                                }
                            }
                        }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Entrar",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.08f))
        }
    }
}


