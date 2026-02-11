package com.videoeditor.pro.domain.model

/**
 * The root container for a project timeline.
 */
data class Timeline(
    val tracks: List<Track> = emptyList(),
    val duration: Long = 0,
    val currentTime: Long = 0 // Playhead position
)
