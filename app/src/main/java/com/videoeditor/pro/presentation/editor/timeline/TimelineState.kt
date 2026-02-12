package com.videoeditor.pro.presentation.editor.timeline

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class TimelineState(
    val zoomLevel: Float = 1.0f, // Pixels per millisecond
    val scrollOffset: Float = 0f,
    val currentTime: Long = 0L,
    val selectedClipId: String? = null,
    val tracks: List<TrackUiModel> = emptyList(),
    // Project resolution for preview/export. Defaults to 1080x1920 (vertical).
    val resolutionWidth: Int = 1080,
    val resolutionHeight: Int = 1920
)

@Immutable
data class TrackUiModel(
    val id: String,
    val clips: List<ClipUiModel>
)

@Immutable
data class ClipUiModel(
    val id: String,
    val startTimeMs: Long,
    val durationMs: Long,
    val name: String,
    val isSelected: Boolean,
    val color: Long = 0xFF4A4A6A
) {
    fun width(zoomLevel: Float): Dp = (durationMs * zoomLevel).dp
    fun startOffset(zoomLevel: Float): Dp = (startTimeMs * zoomLevel).dp
}
