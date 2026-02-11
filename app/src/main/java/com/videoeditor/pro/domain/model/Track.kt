package com.videoeditor.pro.domain.model

import java.util.UUID

/**
 * A track contains a list of non-overlapping clips.
 */
data class Track(
    val id: String = UUID.randomUUID().toString(),
    val type: TrackType,
    val clips: List<Clip> = emptyList(),
    val isMuted: Boolean = false,
    val isLocked: Boolean = false,
    val isVisible: Boolean = true
)

enum class TrackType {
    VIDEO, AUDIO, TEXT, EFFECT
}
