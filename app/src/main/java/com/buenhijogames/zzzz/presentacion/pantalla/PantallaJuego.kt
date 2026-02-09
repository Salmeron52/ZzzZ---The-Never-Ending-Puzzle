package com.buenhijogames.zzzz.presentacion.pantalla

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.buenhijogames.zzzz.R
import com.buenhijogames.zzzz.dominio.caso_uso.ConversorLetras
import com.buenhijogames.zzzz.dominio.caso_uso.Direccion
import com.buenhijogames.zzzz.presentacion.pantalla.componentes.TableroComposable
import com.buenhijogames.zzzz.presentacion.viewmodel.EstadoJuego
import com.buenhijogames.zzzz.presentacion.viewmodel.JuegoViewModel
import kotlin.math.abs

/**
 * Pantalla principal del juego con diseño adaptativo.
 */
@Composable
fun PantallaJuego(
    onIrAPartidasGuardadas: () -> Unit = {},
    onVolverAlMenu: () -> Unit = {},
    viewModel: JuegoViewModel = hiltViewModel(),
    temaViewModel: com.buenhijogames.zzzz.presentacion.viewmodel.TemaViewModel = hiltViewModel()
) {
    val estado by viewModel.estado.collectAsState()
    val esOscuro by temaViewModel.esTemaOscuro.collectAsState()
    val conversorLetras = remember { ConversorLetras() }

    var mostrarDialogoNuevoJuego by remember { mutableStateOf(false) }

    // Variables para detección de gestos a nivel de pantalla
    var acumuladoX by remember { mutableFloatStateOf(0f) }
    var acumuladoY by remember { mutableFloatStateOf(0f) }
    val umbralGesto = 50f

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (estado.cargando) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            // Box con detección de gestos para toda la pantalla
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(estado.finDelJuego) {
                        if (!estado.finDelJuego) {
                            detectDragGestures(
                                onDragStart = {
                                    acumuladoX = 0f
                                    acumuladoY = 0f
                                },
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    acumuladoX += dragAmount.x
                                    acumuladoY += dragAmount.y
                                },
                                onDragEnd = {
                                    val absX = abs(acumuladoX)
                                    val absY = abs(acumuladoY)

                                    if (absX > umbralGesto || absY > umbralGesto) {
                                        val direccion = if (absX > absY) {
                                            if (acumuladoX > 0) Direccion.DERECHA else Direccion.IZQUIERDA
                                        } else {
                                            if (acumuladoY > 0) Direccion.ABAJO else Direccion.ARRIBA
                                        }
                                        viewModel.mover(direccion)
                                    }
                                }
                            )
                        }
                    }
            ) {
                BoxWithConstraints(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                        .padding(16.dp)
                ) {
                    val isLandscape = maxWidth > maxHeight

                    if (isLandscape) {
                        // Diseño Horizontal (Landscape)
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Panel Izquierdo (Info + Controles)
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .verticalScroll(rememberScrollState()), // Scroll por si acaso en landscape pequeños
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                EncabezadoJuego(
                                    estado = estado,
                                    esOscuro = esOscuro,
                                    onCambiarTema = { temaViewModel.cambiarTema() }
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                PieJuego(
                                    estado = estado,
                                    viewModel = viewModel,
                                    onMostrarDialogoNuevo = { mostrarDialogoNuevoJuego = true },
                                    onIrAPartidasGuardadas = onIrAPartidasGuardadas,
                                    onVolverAlMenu = onVolverAlMenu
                                )
                            }

                            Spacer(modifier = Modifier.width(24.dp))

                            // Panel Derecho (Tablero + Tagline)
                            BoxWithConstraints(
                                modifier = Modifier
                                    .weight(1.4f)
                                    .fillMaxHeight(),
                                contentAlignment = Alignment.Center
                            ) {
                                val tamanoTablero = min(maxWidth, maxHeight * 0.9f)

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    TableroComposable(
                                        tablero = estado.tablero,
                                        conversorLetras = conversorLetras,
                                        direccion = estado.ultimaDireccion,
                                        modifier = Modifier.size(tamanoTablero)
                                    )
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = stringResource(R.string.game_tagline),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Light,
                                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                        textAlign = TextAlign.Center,
                                        letterSpacing = 2.sp
                                    )
                                }
                            }
                        }
                    } else {
                        // Diseño Vertical (Portrait)
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            EncabezadoJuego(
                                estado = estado,
                                esOscuro = esOscuro,
                                onCambiarTema = { temaViewModel.cambiarTema() }
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            PieJuego(
                                estado = estado,
                                viewModel = viewModel,
                                onMostrarDialogoNuevo = { mostrarDialogoNuevoJuego = true },
                                onIrAPartidasGuardadas = onIrAPartidasGuardadas,
                                onVolverAlMenu = onVolverAlMenu
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Tablero
                            Box(
                                modifier = Modifier.weight(1f, fill = false),
                                contentAlignment = Alignment.Center
                            ) {
                                TableroComposable(
                                    tablero = estado.tablero,
                                    conversorLetras = conversorLetras,
                                    direccion = estado.ultimaDireccion,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            
                            // Tagline en la parte inferior
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.game_tagline),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Light,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                                textAlign = TextAlign.Center,
                                letterSpacing = 2.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmación para nuevo juego
    if (mostrarDialogoNuevoJuego) {
        com.buenhijogames.zzzz.presentacion.pantalla.componentes.DialogoConfirmacion(
            titulo = stringResource(R.string.new_game_title),
            mensaje = stringResource(R.string.new_game_confirmation),
            textoConfirmar = stringResource(R.string.yes),
            textoCancelar = stringResource(R.string.no),
            onConfirmar = {
                viewModel.iniciarNuevoJuego()
                mostrarDialogoNuevoJuego = false
            },
            onCancelar = { mostrarDialogoNuevoJuego = false }
        )
    }
}

@Composable
private fun EncabezadoJuego(
    estado: EstadoJuego,
    esOscuro: Boolean,
    onCambiarTema: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Título y Botón Tema
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            // Título "ZzzZ" Centrado
             Text(
                text = stringResource(R.string.app_name),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.Center)
            )
            
            // Botón Tema a la derecha
            androidx.compose.material3.IconButton(
                onClick = onCambiarTema,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(48.dp)
            ) {
               if (esOscuro) {
                   com.buenhijogames.zzzz.presentacion.pantalla.componentes.SolIcono(
                       color = MaterialTheme.colorScheme.primary,
                       size = 32.dp
                   )
               } else {
                   com.buenhijogames.zzzz.presentacion.pantalla.componentes.LunaIcono(
                        color = MaterialTheme.colorScheme.primary,
                        size = 32.dp
                   )
               }
            }
        }

        // Nivel Actual
        val nivelStringId = when (estado.nivelActual) {
            com.buenhijogames.zzzz.dominio.modelo.NivelDificultad.NORMAL -> R.string.level_normal
            com.buenhijogames.zzzz.dominio.modelo.NivelDificultad.DIFICIL -> R.string.level_hard
            com.buenhijogames.zzzz.dominio.modelo.NivelDificultad.EXPERTO -> R.string.level_expert
            com.buenhijogames.zzzz.dominio.modelo.NivelDificultad.MAESTRO -> R.string.level_master
            com.buenhijogames.zzzz.dominio.modelo.NivelDificultad.IMPOSIBLE -> R.string.level_impossible
        }

        Text(
            text = "${stringResource(R.string.level_label)} ${stringResource(nivelStringId)}".uppercase(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.secondary,
            letterSpacing = 2.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Puntuación y Récord
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            TarjetaPuntuacion(
                titulo = stringResource(R.string.score_label),
                valor = estado.puntuacion
            )
            TarjetaPuntuacion(
                titulo = stringResource(R.string.high_score_label),
                valor = estado.record
            )
        }
    }
}

@Composable
private fun PieJuego(
    estado: EstadoJuego,
    viewModel: JuegoViewModel,
    onMostrarDialogoNuevo: () -> Unit,
    onIrAPartidasGuardadas: () -> Unit,
    onVolverAlMenu: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Botones de acción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón deshacer
            FilledIconButton(
                onClick = { viewModel.deshacer() },
                enabled = estado.puedeDeshacer,
                modifier = Modifier.size(56.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Undo,
                    contentDescription = stringResource(R.string.undo_desc),
                    modifier = Modifier.size(28.dp)
                )
            }

            // Botón nuevo juego (icono)
            FilledIconButton(
                onClick = onMostrarDialogoNuevo,
                modifier = Modifier.size(56.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.new_game_desc),
                    modifier = Modifier.size(28.dp)
                )
            }

            // Botón partidas guardadas
            FilledIconButton(
                onClick = {
                    viewModel.guardarPartidaComoGuardada()
                    onIrAPartidasGuardadas()
                },
                modifier = Modifier.size(56.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = stringResource(R.string.saved_games_desc),
                    modifier = Modifier.size(28.dp)
                )
            }

            // Botón ir al menú
            FilledIconButton(
                onClick = {
                    viewModel.guardarPartidaComoGuardada()
                    onVolverAlMenu()
                },
                modifier = Modifier.size(56.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = stringResource(R.string.menu_desc),
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        // Mensaje de fin de juego
        if (estado.finDelJuego) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = stringResource(R.string.game_over_title),
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

    }
}

/**
 * Tarjeta para mostrar puntuación o récord.
 */
@Composable
private fun TarjetaPuntuacion(
    titulo: String,
    valor: Long
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .width(100.dp) // Reducido un poco para que quepa mejor en pantallas pequeñas
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titulo,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp
            )
            Text(
                text = valor.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
