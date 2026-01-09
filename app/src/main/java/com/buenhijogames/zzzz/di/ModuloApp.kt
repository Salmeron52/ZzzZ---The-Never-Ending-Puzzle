package com.buenhijogames.zzzz.di

import android.content.Context
import com.buenhijogames.zzzz.datos.local.BaseDatosJuego
import com.buenhijogames.zzzz.datos.local.PartidaDao
import com.buenhijogames.zzzz.datos.repositorio.RepositorioJuegoImpl
import com.buenhijogames.zzzz.dominio.repositorio.RepositorioJuego
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Hilt que provee las dependencias de la aplicación.
 */
@Module
@InstallIn(SingletonComponent::class)
object ModuloApp {

    /**
     * Provee la instancia de la base de datos.
     */
    @Provides
    @Singleton
    fun proveerBaseDatos(@ApplicationContext contexto: Context): BaseDatosJuego {
        return BaseDatosJuego.obtenerInstancia(contexto)
    }

    /**
     * Provee el DAO de partidas.
     */
    @Provides
    @Singleton
    fun proveerPartidaDao(baseDatos: BaseDatosJuego): PartidaDao {
        return baseDatos.partidaDao()
    }

    /**
     * Provee la implementación del repositorio.
     */
    @Provides
    @Singleton
    fun proveerRepositorioJuego(partidaDao: PartidaDao): RepositorioJuego {
        return RepositorioJuegoImpl(partidaDao)
    }
}
