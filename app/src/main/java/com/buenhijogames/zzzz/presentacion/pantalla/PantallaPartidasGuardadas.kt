package com.buenhijogames.zzzz.presentacion.pantalla

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.buenhijogames.zzzz.dominio.caso_uso.ConversorLetras
import com.buenhijogames.zzzz.dominio.modelo.NivelDificultad
import com.buenhijogames.zzzz.dominio.repositorio.PartidaGuardada
import com.buenhijogames.zzzz.presentacion.pantalla.componentes.DialogoConfirmacion
import com.buenhijogames.zzzz.presentacion.viewmodel.PartidasGuardadasViewModel
import com.buenhijogames.zzzz.ui.theme.obtenerColorFicha
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Pantalla de partidas guardadas con diseño premium.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPartidasGuardadas(
    onVolver: () -> Unit,
    onContinuarPartida: (Long) -> Unit,
    viewModel: PartidasGuardadasViewModel = hiltViewModel()
) {
    val partidas by viewModel.partidas.collectAsState()
    val conversorLetras = remember { ConversorLetras() }
    
    var partidaAEliminar by remember { mutableStateOf<PartidaGuardada?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Partidas Guardadas",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            if (partidas.isEmpty()) {
                // Estado vacío
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "🎮",
                            fontSize = 64.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No hay partidas guardadas",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Guarda una partida desde el juego\npara verla aquí",
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(partidas, key = { it.id }) { partida ->
                        TarjetaPartida(
                            partida = partida,
                            conversorLetras = conversorLetras,
                            onClick = { onContinuarPartida(partida.id) },
                            onEliminar = { partidaAEliminar = partida },
                            modifier = Modifier.animateContentSize()
                        )
                    }
                }
            }
        }
    }

    // Diálogo de confirmación para eliminar
    partidaAEliminar?.let { partida ->
        DialogoConfirmacion(
            titulo = "Eliminar partida",
            mensaje = "¿Estás seguro de que quieres eliminar \"${partida.nombre}\"?",
            textoConfirmar = "Eliminar",
            textoCancelar = "Cancelar",
            onConfirmar = {
                viewModel.eliminarPartida(partida.id)
                partidaAEliminar = null
            },
            onCancelar = { partidaAEliminar = null }
        )
    }
}

/**
 * Obtiene el nombre del nivel.
 */
private fun obtenerNombreNivel(nivelId: Int): String {
    return when (nivelId) {
        1 -> "Normal"
        2 -> "Difícil"
        3 -> "Experto"
        4 -> "Maestro"
        5 -> "Imposible"
        else -> "Normal"
    }
}

/**
 * Tarjeta premium para mostrar una partida guardada.
 * Clickable para continuar la partida.
 */
@Composable
private fun TarjetaPartida(
    partida: PartidaGuardada,
    conversorLetras: ConversorLetras,
    onClick: () -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formato = SimpleDateFormat("dd MMM yyyy - HH:mm", Locale.getDefault())
    val fechaFormateada = formato.format(Date(partida.fechaModificacion))
    val letraMaxima = conversorLetras.obtenerEtiqueta(partida.fichaMaxima)
    val colorFicha = obtenerColorFicha(partida.fichaMaxima, false)
    val nombreNivel = obtenerNombreNivel(partida.nivelId)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            colorFicha.copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Indicador de letra máxima
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(colorFicha),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = letraMaxima,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (letraMaxima.length > 2) 14.sp else 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Información de la partida
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = partida.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row {
                        Text(
                            text = "Puntos: ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${partida.puntuacion}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = fechaFormateada,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    // Badge de nivel debajo de los datos
                    Text(
                        text = nombreNivel,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.secondaryContainer,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Solo botón eliminar
                IconButton(
                    onClick = onEliminar,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.errorContainer)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

