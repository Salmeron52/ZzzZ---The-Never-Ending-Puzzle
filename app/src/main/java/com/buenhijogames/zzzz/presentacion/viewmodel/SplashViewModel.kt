package com.buenhijogames.zzzz.presentacion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buenhijogames.zzzz.dominio.repositorio.RepositorioJuego
import com.buenhijogames.zzzz.presentacion.navegacion.Rutas
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repositorio: RepositorioJuego
) : ViewModel() {

    suspend fun determinarDestino(): String {
        return withContext(Dispatchers.IO) {
            val partidaGuardada = repositorio.cargarPartida()
            if (partidaGuardada != null) Rutas.JUEGO else Rutas.MENU
        }
    }
}
