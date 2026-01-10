package com.buenhijogames.zzzz.presentacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buenhijogames.zzzz.dominio.repositorio.TemaRepositorio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemaViewModel @Inject constructor(
    private val temaRepositorio: TemaRepositorio
) : ViewModel() {

    val esTemaOscuro: StateFlow<Boolean> = temaRepositorio.esTemaOscuro
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true // Defecto oscuro mientras carga
        )

    fun cambiarTema() {
        viewModelScope.launch {
            val actual = esTemaOscuro.value
            temaRepositorio.establecerTemaOscuro(!actual)
        }
    }
}
