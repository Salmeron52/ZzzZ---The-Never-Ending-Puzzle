package com.buenhijogames.zzzz.presentacion.pantalla

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.buenhijogames.zzzz.R
import com.buenhijogames.zzzz.dominio.modelo.NivelDificultad
import kotlinx.coroutines.delay

/**
 * Pantalla de menú principal con diseño premium.
 */
@Composable
fun PantallaMenu(
    onSeleccionarNivel: (NivelDificultad) -> Unit,
    onIrAPartidasGuardadas: () -> Unit,
    onIrAyuda: () -> Unit
) {
    var animacionIniciada by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        animacionIniciada = true
    }

    val escala by animateFloatAsState(
        targetValue = if (animacionIniciada) 1f else 0.5f,
        animationSpec = tween(durationMillis = 500),
        label = "escala"
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f)
                        )
                    )
                )


        ) {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding() // Respetar insets
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo animado
                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.scale(escala)
                )

                // Subtítulo eliminado según requerimiento
                // Spacer era height 32.dp, lo mantengo para separar título de botones
                Spacer(modifier = Modifier.height(32.dp))

                // Botón partidas guardadas (Movido arriba por petición)
                Button(
                    onClick = onIrAPartidasGuardadas,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "  ${stringResource(R.string.saved_games_title)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botones de nivel
                BotonNivel(
                    nivel = NivelDificultad.NORMAL,
                    titulo = stringResource(R.string.level_normal),
                    descripcion = stringResource(R.string.level_normal_desc),
                    colorFondo = MaterialTheme.colorScheme.primaryContainer,
                    colorTexto = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = { onSeleccionarNivel(NivelDificultad.NORMAL) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                BotonNivel(
                    nivel = NivelDificultad.DIFICIL,
                    titulo = stringResource(R.string.level_hard),
                    descripcion = stringResource(R.string.level_hard_desc),
                    colorFondo = MaterialTheme.colorScheme.secondaryContainer,
                    colorTexto = MaterialTheme.colorScheme.onSecondaryContainer,
                    onClick = { onSeleccionarNivel(NivelDificultad.DIFICIL) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                BotonNivel(
                    nivel = NivelDificultad.EXPERTO,
                    titulo = stringResource(R.string.level_expert),
                    descripcion = stringResource(R.string.level_expert_desc),
                    colorFondo = MaterialTheme.colorScheme.tertiaryContainer,
                    colorTexto = MaterialTheme.colorScheme.onTertiaryContainer,
                    onClick = { onSeleccionarNivel(NivelDificultad.EXPERTO) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                BotonNivel(
                    nivel = NivelDificultad.MAESTRO,
                    titulo = stringResource(R.string.level_master),
                    descripcion = stringResource(R.string.level_master_desc),
                    colorFondo = Color(0xFFFFB74D),
                    colorTexto = Color(0xFF5D4037),
                    onClick = { onSeleccionarNivel(NivelDificultad.MAESTRO) }
                )

                Spacer(modifier = Modifier.height(10.dp))

                BotonNivel(
                    nivel = NivelDificultad.IMPOSIBLE,
                    titulo = stringResource(R.string.level_impossible),
                    descripcion = stringResource(R.string.level_impossible_desc),
                    colorFondo = Color(0xFFEF5350),
                    colorTexto = Color.White,
                    onClick = { onSeleccionarNivel(NivelDificultad.IMPOSIBLE) }
                )



                Spacer(modifier = Modifier.weight(1f))

                // Instrucciones
                Text(
                    text = stringResource(R.string.menu_hint),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
            // Icono de Información / Ayuda (Overlay - Z-Index superior)
            androidx.compose.material3.IconButton(
                onClick = onIrAyuda,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .safeDrawingPadding()
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = stringResource(R.string.info_title),
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


/**
 * Botón de selección de nivel con diseño premium.
 */
@Composable
private fun BotonNivel(
    nivel: NivelDificultad,
    titulo: String,
    descripcion: String,
    colorFondo: Color,
    colorTexto: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorFondo,
            contentColor = colorTexto
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titulo,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = descripcion,
                fontSize = 12.sp,
                color = colorTexto.copy(alpha = 0.8f)
            )
        }
    }
}
