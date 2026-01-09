package com.buenhijogames.zzzz.datos.repositorio

import com.buenhijogames.zzzz.datos.local.Convertidores
import com.buenhijogames.zzzz.datos.local.PartidaDao
import com.buenhijogames.zzzz.datos.local.PartidaEntidad
import com.buenhijogames.zzzz.dominio.modelo.Ficha
import com.buenhijogames.zzzz.dominio.repositorio.RepositorioJuego
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del repositorio usando Room para persistencia.
 */
@Singleton
class RepositorioJuegoImpl @Inject constructor(
    private val partidaDao: PartidaDao
) : RepositorioJuego {

    override suspend fun guardarPartida(
        tablero: List<List<Ficha?>>,
        puntuacion: Long,
        record: Long
    ) {
        runCatching {
            val tableroJson = Convertidores.tableroAJson(tablero)
            val contadorFichas = Convertidores.obtenerMaximoId(tablero)
            val entidad = PartidaEntidad(
                id = 1,
                tableroJson = tableroJson,
                puntuacion = puntuacion,
                record = record,
                contadorFichas = contadorFichas
            )
            partidaDao.guardarPartida(entidad)
        }
    }

    override suspend fun cargarPartida(): Triple<List<List<Ficha?>>, Long, Long>? {
        return runCatching {
            val entidad = partidaDao.obtenerPartida() ?: return null
            val tablero = Convertidores.jsonATablero(entidad.tableroJson)
            Triple(tablero, entidad.puntuacion, entidad.record)
        }.getOrNull()
    }

    override suspend fun eliminarPartida() {
        runCatching {
            partidaDao.eliminarPartida()
        }
    }
}
