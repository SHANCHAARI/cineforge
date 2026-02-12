package com.videoeditor.pro.presentation.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.videoeditor.pro.domain.model.Clip
import com.videoeditor.pro.domain.model.ClipType
import com.videoeditor.pro.domain.repository.TimelineRepository
import com.videoeditor.pro.presentation.editor.timeline.ClipUiModel
import com.videoeditor.pro.presentation.editor.timeline.TimelineState
import com.videoeditor.pro.presentation.editor.timeline.TrackUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditorViewModel @Inject constructor(
    private val timelineRepository: TimelineRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimelineState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            timelineRepository.getTimeline().collect { domainTimeline ->
                _uiState.update { currentState ->
                    currentState.copy(
                        tracks = domainTimeline.tracks.map { track ->
                            TrackUiModel(
                                id = track.id,
                                clips = track.clips.map { clip ->
                                    ClipUiModel(
                                        id = clip.id,
                                        startTimeMs = clip.startTime,
                                        durationMs = clip.duration,
                                        name = if (clip.type == com.videoeditor.pro.domain.model.ClipType.TEXT) 
                                            clip.properties.textContent ?: "Text" else "Clip",
                                        isSelected = currentState.selectedClipId == clip.id,
                                        color = when(clip.type) {
                                            com.videoeditor.pro.domain.model.ClipType.VIDEO -> 0xFF4A4A6A
                                            com.videoeditor.pro.domain.model.ClipType.AUDIO -> 0xFF2D6A4F
                                            com.videoeditor.pro.domain.model.ClipType.TEXT -> 0xFF7209B7
                                            else -> 0xFF4A4A6A
                                        }
                                    )
                                }
                            )
                        }
                    )
                }
            }
        }
    }

    fun addTextClip(text: String) {
        viewModelScope.launch {
            val clip = Clip(
                filePath = "text://",
                type = ClipType.TEXT,
                startTime = _uiState.value.currentTime,
                duration = 3000,
                properties = com.videoeditor.pro.domain.model.ClipProperties(textContent = text)
            )
            timelineRepository.addClip(2, clip) // Text track is index 2
        }
    }

    fun selectClip(clipId: String) {
        _uiState.update { it.copy(selectedClipId = clipId) }
    }

    fun splitSelectedClip() {
        val selectedId = _uiState.value.selectedClipId ?: return
        val currentTime = _uiState.value.currentTime
        viewModelScope.launch {
            timelineRepository.splitClip(selectedId, currentTime)
            NativeBridge.nativeSplitClip(selectedId, currentTime)
        }
    }

    fun moveClip(clipId: String, trackIndex: Int, newStartTime: Long) {
        viewModelScope.launch {
            timelineRepository.moveClip(clipId, trackIndex, newStartTime)
            NativeBridge.nativeMoveClip(clipId, newStartTime)
        }
    }

    fun updateClipProperties(clipId: String, propertiesUpdate: (com.videoeditor.pro.domain.model.ClipProperties) -> com.videoeditor.pro.domain.model.ClipProperties) {
        viewModelScope.launch {
            // In a real app, we'd fetch the clip from repository, update it, and save back.
            // For real-time grading, we also push directly to the native bridge.
            _uiState.update { currentState ->
                // This is a simplified update for the UI/Preview
                // We'll find the clip in the tracks and apply the update
                val updatedTracks = currentState.tracks.map { track ->
                    track.copy(clips = track.clips.map { clip ->
                        if (clip.id == clipId) {
                            // Find corresponding domain clip (simplified)
                            // In a real implementation, we'd have a more robust way to sync this.
                            clip
                        } else clip
                    })
                }
                currentState.copy(tracks = updatedTracks)
            }
            
            // Apply mock values for grading
            // In a real app, these values would come from the updated properties.
            // NativeBridge.nativeSetColorGrading(brightness, contrast, saturation)
        }
    }

    fun setLiveColorGrading(brightness: Float, contrast: Float, saturation: Float) {
        NativeBridge.nativeSetColorGrading(brightness, contrast, saturation)
    }

    fun updateZoom(zoomMultiplier: Float) {
        _uiState.update { currentState ->
            // Clamp zoom level between 0.1f (zoomed out) and 10f (zoomed in)
            val newZoom = (currentState.zoomLevel * zoomMultiplier).coerceIn(0.1f, 10f)
            currentState.copy(zoomLevel = newZoom)
        }
    }

    fun seekTo(timeMs: Long) {
        _uiState.update { current ->
            current.copy(currentTime = timeMs.coerceAtLeast(0L))
        }
        NativeBridge.nativeSetPlayheadMs(timeMs.coerceAtLeast(0L))
    }

    fun togglePlayPause() {
        val currentClips = _uiState.value.tracks.flatMap { it.clips }
        if (currentClips.isEmpty()) return

        val timelineEnd = currentClips.maxOfOrNull { it.startTimeMs + it.durationMs } ?: 0L

        if (isPlaying) {
            isPlaying = false
            return
        }

        isPlaying = true

        viewModelScope.launch {
            var lastTime = System.currentTimeMillis()
            while (isPlaying) {
                val now = System.currentTimeMillis()
                val delta = now - lastTime
                lastTime = now

                val next = (_uiState.value.currentTime + delta).coerceAtMost(timelineEnd)
                seekTo(next)

                if (next >= timelineEnd) {
                    isPlaying = false
                }

                kotlinx.coroutines.delay(16L)
            }
        }
    }

    fun setAspectRatio(width: Int, height: Int) {
        if (width <= 0 || height <= 0) return
        _uiState.update { currentState ->
            currentState.copy(
                resolutionWidth = width,
                resolutionHeight = height
            )
        }
    }

    private var isPlaying: Boolean = false

    private fun detectClipType(
        uri: android.net.Uri,
        context: android.content.Context,
        fallback: ClipType
    ): ClipType {
        val mimeType = context.contentResolver.getType(uri) ?: return fallback
        return when {
            mimeType.startsWith("video/") -> ClipType.VIDEO
            mimeType.startsWith("image/") -> ClipType.IMAGE
            mimeType.startsWith("audio/") -> ClipType.AUDIO
            else -> fallback
        }
    }

    private fun resolveTrackIndex(type: ClipType): Int {
        return when (type) {
            ClipType.VIDEO, ClipType.IMAGE -> 0 // Video/visual track
            ClipType.AUDIO -> 1               // Audio track
            ClipType.TEXT -> 2                // Text track
            else -> 0
        }
    }

    private suspend fun addMediaClipInternal(
        uri: android.net.Uri,
        context: android.content.Context,
        typeHint: ClipType? = null
    ) {
        val retriever = android.media.MediaMetadataRetriever()
        val clipId = java.util.UUID.randomUUID().toString()
        try {
            retriever.setDataSource(context, uri)
            val durationStr = retriever.extractMetadata(
                android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
            )
            val extractedDuration = durationStr?.toLongOrNull()

            val inferredType = typeHint ?: detectClipType(uri, context, ClipType.VIDEO)
            val defaultDuration = when (inferredType) {
                ClipType.IMAGE -> 3000L
                else -> 5000L
            }
            val duration = extractedDuration?.takeIf { it > 0 } ?: defaultDuration

            val clip = Clip(
                id = clipId,
                filePath = uri.toString(),
                type = inferredType,
                startTime = _uiState.value.currentTime,
                duration = duration
            )
            val trackIndex = resolveTrackIndex(inferredType)
            timelineRepository.addClip(trackIndex, clip)

            // Notify Native Engine (generic media clip – decoding handled natively later)
            NativeBridge.nativeAddMediaClip(
                clipId,
                uri.toString(),
                _uiState.value.currentTime,
                duration
            )
        } catch (e: Exception) {
            val inferredType = typeHint ?: detectClipType(uri, context, ClipType.VIDEO)
            val defaultDuration = when (inferredType) {
                ClipType.IMAGE -> 3000L
                else -> 5000L
            }

            val clip = Clip(
                id = clipId,
                filePath = uri.toString(),
                type = inferredType,
                startTime = _uiState.value.currentTime,
                duration = defaultDuration
            )
            val trackIndex = resolveTrackIndex(inferredType)
            timelineRepository.addClip(trackIndex, clip)

            NativeBridge.nativeAddMediaClip(
                clipId,
                uri.toString(),
                _uiState.value.currentTime,
                defaultDuration
            )
        } finally {
            retriever.release()
        }
    }

    fun addMediaUri(uri: android.net.Uri, context: android.content.Context) {
        viewModelScope.launch {
            addMediaClipInternal(uri, context, null)
        }
    }

    fun addAudioUri(uri: android.net.Uri, context: android.content.Context) {
        viewModelScope.launch {
            addMediaClipInternal(uri, context, ClipType.AUDIO)
        }
    }
}
