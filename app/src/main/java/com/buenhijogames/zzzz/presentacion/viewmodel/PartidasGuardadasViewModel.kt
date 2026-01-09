package com.buenhijogames.zzzz.presentacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buenhijogames.zzzz.dominio.repositorio.PartidaGuardada
import com.buenhijogames.zzzz.dominio.repositorio.RepositorioJuego
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de partidas guardadas.
 */
@HiltViewModel
class PartidasGuardadasViewModel @Inject constructor(
    private val repositorio: RepositorioJuego
) : ViewModel() {

    /**
     * Lista de partidas guardadas como StateFlow.
     */
    val partidas: StateFlow<List<PartidaGuardada>> = repositorio.obtenerPartidasGuardadas()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Elimina una partida guardada.
     */
    fun eliminarPartida(id: Long) {
        viewModelScope.launch {
            runCatching {
                repositorio.eliminarPartidaGuardada(id)
            }
        }
    }
}
