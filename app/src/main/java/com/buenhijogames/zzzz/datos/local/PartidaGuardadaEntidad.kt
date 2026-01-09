package com.buenhijogames.zzzz.datos.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room para persistir partidas guardadas múltiples.
 *
 * @property id Identificador único autogenerado
 * @property nombre Nombre descriptivo de la partida
 * @property tableroJson Representación JSON del tablero
 * @property puntuacion Puntuación actual
 * @property record Récord máximo alcanzado
 * @property fichaMaxima Valor de la ficha más alta (para mostrar letra)
 * @property fechaCreacion Timestamp de creación
 * @property fechaModificacion Timestamp de última modificación
 * @property contadorFichas Contador de IDs de fichas para restaurar estado
 */
@Entity(tableName = "partidas_guardadas")
data class PartidaGuardadaEntidad(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val tableroJson: String,
    val puntuacion: Long,
    val record: Long,
    val fichaMaxima: Int,
    val fechaCreacion: Long,
    val fechaModificacion: Long,
    val contadorFichas: Long = 0L
)
