package com.videoeditor.pro.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room‑backed representation of a clip on a specific track.
 *
 * We normalise the timeline model down to a flat list of clips with an
 * associated track index. Domain models (Timeline/Track/Clip) are
 * reconstructed in the repository layer.
 */
@Entity(tableName = "clips")
data class DbClip(
    @PrimaryKey val id: String,
    val trackIndex: Int,
    val filePath: String,
    val type: String,
    val startTime: Long,
    val duration: Long,
    val sourceStartTime: Long,
    val propertiesJson: String
)

