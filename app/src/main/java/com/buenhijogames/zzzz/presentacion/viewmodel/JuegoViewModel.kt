package com.buenhijogames.zzzz.presentacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buenhijogames.zzzz.dominio.caso_uso.Direccion
import com.buenhijogames.zzzz.dominio.caso_uso.ReglasJuego
import com.buenhijogames.zzzz.dominio.repositorio.RepositorioJuego
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel principal del juego que gestiona el estado y las acciones del usuario.
 */
@HiltViewModel
class JuegoViewModel @Inject constructor(
    private val reglasJuego: ReglasJuego,
    private val repositorio: RepositorioJuego
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoJuego())
    val estado: StateFlow<EstadoJuego> = _estado.asStateFlow()

    init {
        cargarPartida()
    }

    /**
     * Carga la partida actual o inicia una nueva.
     */
    private fun cargarPartida() {
        viewModelScope.launch {
            runCatching {
                val partidaGuardada = repositorio.cargarPartida()
                if (partidaGuardada != null) {
                    val (tablero, puntuacion, record) = partidaGuardada
                    val maxId = tablero.flatten().filterNotNull().maxOfOrNull { it.id } ?: 0L
                    reglasJuego.establecerContadorId(maxId)
                    
                    _estado.update {
                        it.copy(
                            tablero = tablero,
                            puntuacion = puntuacion,
                            record = record,
                            finDelJuego = reglasJuego.esFinDeJuego(tablero),
                            cargando = false,
                            puedeDeshacer = false,
                            estadoAnterior = null
                        )
                    }
                } else {
                    iniciarNuevoJuegoInterno()
                }
            }.onFailure {
                iniciarNuevoJuegoInterno()
            }
        }
    }

    /**
     * Inicia un nuevo juego (uso interno).
     */
    private fun iniciarNuevoJuegoInterno() {
        val tableroNuevo = reglasJuego.inicializarJuego()
        _estado.update {
            it.copy(
                tablero = tableroNuevo,
                puntuacion = 0L,
                finDelJuego = false,
                cargando = false,
                puedeDeshacer = false,
                estadoAnterior = null,
                partidaId = null
            )
        }
        guardarPartidaActual()
    }

    /**
     * Inicia un nuevo juego (llamado desde UI).
     */
    fun iniciarNuevoJuego() {
        iniciarNuevoJuegoInterno()
    }

    /**
     * Procesa un movimiento del usuario.
     */
    fun mover(direccion: Direccion) {
        val estadoActual = _estado.value
        if (estadoActual.finDelJuego || estadoActual.cargando) return

        val resultado = reglasJuego.mover(estadoActual.tablero, direccion)
        
        if (resultado.huboMovimiento) {
            // Guardar estado anterior para poder deshacer
            val snapshotAnterior = SnapshotEstado(
                tablero = estadoActual.tablero,
                puntuacion = estadoActual.puntuacion
            )
            
            val tableroConNuevaFicha = reglasJuego.agregarFichaAleatoria(resultado.tablero)
            val nuevaPuntuacion = estadoActual.puntuacion + resultado.puntuacionGanada
            val nuevoRecord = maxOf(estadoActual.record, nuevaPuntuacion)
            val finDelJuego = reglasJuego.esFinDeJuego(tableroConNuevaFicha)

            _estado.update {
                it.copy(
                    tablero = tableroConNuevaFicha,
                    puntuacion = nuevaPuntuacion,
                    record = nuevoRecord,
                    finDelJuego = finDelJuego,
                    puedeDeshacer = true,
                    estadoAnterior = snapshotAnterior
                )
            }
            guardarPartidaActual()
        }
    }

    /**
     * Deshace el último movimiento (solo una vez).
     */
    fun deshacer() {
        val estadoActual = _estado.value
        val anterior = estadoActual.estadoAnterior ?: return
        
        if (!estadoActual.puedeDeshacer) return
        
        // Restaurar el contador de IDs al estado anterior
        val maxId = anterior.tablero.flatten().filterNotNull().maxOfOrNull { it.id } ?: 0L
        reglasJuego.establecerContadorId(maxId)
        
        _estado.update {
            it.copy(
                tablero = anterior.tablero,
                puntuacion = anterior.puntuacion,
                finDelJuego = false,
                puedeDeshacer = false,
                estadoAnterior = null
            )
        }
        guardarPartidaActual()
    }

    /**
     * Guarda la partida actual como una partida guardada.
     */
    fun guardarPartidaComoGuardada() {
        viewModelScope.launch {
            runCatching {
                val estadoActual = _estado.value
                val nuevoId = repositorio.guardarPartidaComoGuardada(
                    tablero = estadoActual.tablero,
                    puntuacion = estadoActual.puntuacion,
                    record = estadoActual.record,
                    partidaId = estadoActual.partidaId
                )
                _estado.update { it.copy(partidaId = nuevoId) }
            }
        }
    }

    /**
     * Carga una partida guardada por ID.
     */
    fun cargarPartidaGuardada(id: Long) {
        viewModelScope.launch {
            runCatching {
                val partida = repositorio.cargarPartidaGuardada(id) ?: return@launch
                
                val maxId = partida.tablero.flatten().filterNotNull().maxOfOrNull { it.id } ?: 0L
                reglasJuego.establecerContadorId(maxId)
                
                _estado.update {
                    it.copy(
                        tablero = partida.tablero,
                        puntuacion = partida.puntuacion,
                        record = partida.record,
                        finDelJuego = reglasJuego.esFinDeJuego(partida.tablero),
                        cargando = false,
                        puedeDeshacer = false,
                        estadoAnterior = null,
                        partidaId = id
                    )
                }
                guardarPartidaActual()
            }
        }
    }

    /**
     * Guarda la partida actual en la base de datos.
     */
    private fun guardarPartidaActual() {
        viewModelScope.launch {
            runCatching {
                val estadoActual = _estado.value
                repositorio.guardarPartida(
                    tablero = estadoActual.tablero,
                    puntuacion = estadoActual.puntuacion,
                    record = estadoActual.record
                )
            }
        }
    }
}

