package com.buenhijogames.zzzz.presentacion.pantalla

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.buenhijogames.zzzz.dominio.caso_uso.ConversorLetras
import com.buenhijogames.zzzz.presentacion.pantalla.componentes.DialogoConfirmacion
import com.buenhijogames.zzzz.presentacion.pantalla.componentes.TableroComposable
import com.buenhijogames.zzzz.presentacion.viewmodel.JuegoViewModel

/**
 * Pantalla principal del juego.
 */
@Composable
fun PantallaJuego(
    onIrAPartidasGuardadas: () -> Unit = {},
    viewModel: JuegoViewModel = hiltViewModel()
) {
    val estado by viewModel.estado.collectAsState()
    val conversorLetras = remember { ConversorLetras() }
    
    var mostrarDialogoNuevoJuego by remember { mutableStateOf(false) }

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Título
                Text(
                    text = "ZzzZ",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Puntuación y Récord
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TarjetaPuntuacion(
                        titulo = "Puntuación",
                        valor = estado.puntuacion
                    )
                    TarjetaPuntuacion(
                        titulo = "Récord",
                        valor = estado.record
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Tablero
                TableroComposable(
                    tablero = estado.tablero,
                    conversorLetras = conversorLetras,
                    onMover = { direccion -> viewModel.mover(direccion) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

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
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Deshacer",
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    // Botón nuevo juego (icono)
                    FilledIconButton(
                        onClick = { mostrarDialogoNuevoJuego = true },
                        modifier = Modifier.size(56.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Nuevo juego",
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
                            contentDescription = "Partidas guardadas",
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
                            text = "¡Fin del juego!",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Instrucciones
                Text(
                    text = "Desliza para mover las fichas.\nUne fichas iguales para subir de letra.",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        }
    }

    // Diálogo de confirmación para nuevo juego
    if (mostrarDialogoNuevoJuego) {
        DialogoConfirmacion(
            titulo = "Nuevo juego",
            mensaje = "¿Estás seguro de que quieres empezar un nuevo juego? Se perderá el progreso actual.",
            textoConfirmar = "Sí",
            textoCancelar = "No",
            onConfirmar = {
                viewModel.iniciarNuevoJuego()
                mostrarDialogoNuevoJuego = false
            },
            onCancelar = { mostrarDialogoNuevoJuego = false }
        )
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
                .width(120.dp)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titulo,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = valor.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

