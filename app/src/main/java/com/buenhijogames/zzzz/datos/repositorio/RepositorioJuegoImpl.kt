package com.buenhijogames.zzzz.datos.repositorio

import com.buenhijogames.zzzz.datos.local.Convertidores
import com.buenhijogames.zzzz.datos.local.PartidaDao
import com.buenhijogames.zzzz.datos.local.PartidaEntidad
import com.buenhijogames.zzzz.datos.local.PartidaGuardadaEntidad
import com.buenhijogames.zzzz.dominio.modelo.Ficha
import com.buenhijogames.zzzz.dominio.repositorio.PartidaGuardada
import com.buenhijogames.zzzz.dominio.repositorio.RepositorioJuego
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del repositorio usando Room para persistencia.
 */
@Singleton
class RepositorioJuegoImpl @Inject constructor(
    private val partidaDao: PartidaDao
) : RepositorioJuego {

    // ========== Partida Actual ==========

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

    // ========== Partidas Guardadas Múltiples ==========

    override suspend fun guardarPartidaComoGuardada(
        tablero: List<List<Ficha?>>,
        puntuacion: Long,
        record: Long,
        partidaId: Long?
    ): Long {
        return runCatching {
            val tableroJson = Convertidores.tableroAJson(tablero)
            val contadorFichas = Convertidores.obtenerMaximoId(tablero)
            val fichaMaxima = tablero.flatten().filterNotNull().maxOfOrNull { it.valor } ?: 1
            val ahora = System.currentTimeMillis()

            if (partidaId != null) {
                // Actualizar partida existente
                partidaDao.actualizarPartidaGuardada(
                    id = partidaId,
                    tableroJson = tableroJson,
                    puntuacion = puntuacion,
                    record = record,
                    fichaMaxima = fichaMaxima,
                    fechaModificacion = ahora,
                    contadorFichas = contadorFichas
                )
                partidaId
            } else {
                // Crear nueva partida
                val nombre = generarNombrePartida()
                val entidad = PartidaGuardadaEntidad(
                    nombre = nombre,
                    tableroJson = tableroJson,
                    puntuacion = puntuacion,
                    record = record,
                    fichaMaxima = fichaMaxima,
                    fechaCreacion = ahora,
                    fechaModificacion = ahora,
                    contadorFichas = contadorFichas
                )
                partidaDao.insertarPartidaGuardada(entidad)
            }
        }.getOrElse { 0L }
    }

    override fun obtenerPartidasGuardadas(): Flow<List<PartidaGuardada>> {
        return partidaDao.obtenerTodasLasPartidas().map { lista ->
            lista.map { entidad -> entidadAPartidaGuardada(entidad) }
        }
    }

    override suspend fun cargarPartidaGuardada(id: Long): PartidaGuardada? {
        return runCatching {
            val entidad = partidaDao.obtenerPartidaGuardadaPorId(id) ?: return null
            entidadAPartidaGuardada(entidad)
        }.getOrNull()
    }

    override suspend fun eliminarPartidaGuardada(id: Long) {
        runCatching {
            partidaDao.eliminarPartidaGuardada(id)
        }
    }

    // ========== Helpers ==========

    private fun entidadAPartidaGuardada(entidad: PartidaGuardadaEntidad): PartidaGuardada {
        return PartidaGuardada(
            id = entidad.id,
            nombre = entidad.nombre,
            tablero = Convertidores.jsonATablero(entidad.tableroJson),
            puntuacion = entidad.puntuacion,
            record = entidad.record,
            fichaMaxima = entidad.fichaMaxima,
            fechaCreacion = entidad.fechaCreacion,
            fechaModificacion = entidad.fechaModificacion
        )
    }

    private fun generarNombrePartida(): String {
        val formato = SimpleDateFormat("dd MMM - HH:mm", Locale.getDefault())
        return "Partida ${formato.format(Date())}"
    }
}

