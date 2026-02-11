package com.videoeditor.pro.domain.model

import java.util.UUID

/**
 * Represents a single media clip in the timeline.
 */
data class Clip(
    val id: String = UUID.randomUUID().toString(),
    val filePath: String,
    val type: ClipType,
    val startTime: Long, // In milliseconds, relative to timeline start
    val duration: Long,  // trimmed duration
    val sourceStartTime: Long = 0, // offset into the source file
    val zIndex: Int = 0,
    val properties: ClipProperties = ClipProperties()
)

enum class ClipType {
    VIDEO, AUDIO, IMAGE, TEXT, OVERLAY
}

/**
 * transform and visual properties for a clip
 */
data class ClipProperties(
    val scale: Float = 1.0f,
    val rotation: Float = 0f, // degrees
    val positionX: Float = 0f, // normalized -1 to 1
    val positionY: Float = 0f,
    val opacity: Float = 1.0f,
    val volume: Float = 1.0f
)
