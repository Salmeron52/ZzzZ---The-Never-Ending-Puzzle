package com.buenhijogames.zzzz.datos.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para persistir el estado de la partida.
 *
 * @property id Identificador único (siempre 1 para una sola partida activa)
 * @property tableroJson Representación JSON del tablero
 * @property puntuacion Puntuación actual
 * @property record Récord máximo alcanzado
 * @property contadorFichas Contador de IDs de fichas para restaurar estado
 */
@Entity(tableName = "partida")
data class PartidaEntidad(
    @PrimaryKey
    val id: Int = 1,
    val tableroJson: String,
    val puntuacion: Long,
    val record: Long,
    val contadorFichas: Long = 0L,
    val nivelId: Int = 1,
    val partidaId: Long? = null
)
