package com.buenhijogames.zzzz.dominio.repositorio

import kotlinx.coroutines.flow.Flow

interface TemaRepositorio {
    val esTemaOscuro: Flow<Boolean>
    suspend fun establecerTemaOscuro(esOscuro: Boolean)
}
