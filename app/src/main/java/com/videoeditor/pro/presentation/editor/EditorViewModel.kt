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
                                        name = "Clip", // TODO: meaningful names
                                        isSelected = currentState.selectedClipId == clip.id
                                    )
                                }
                            )
                        }
                    )
                }
            }
        }
    }

    fun selectClip(clipId: String) {
        _uiState.update { it.copy(selectedClipId = clipId) }
    }

    fun updateZoom(zoomMultiplier: Float) {
        _uiState.update { currentState ->
            // Clamp zoom level between 0.1f (zoomed out) and 10f (zoomed in)
            val newZoom = (currentState.zoomLevel * zoomMultiplier).coerceIn(0.1f, 10f)
            currentState.copy(zoomLevel = newZoom)
        }
    }

    fun addMediaUri(uri: android.net.Uri, context: android.content.Context) {
        viewModelScope.launch {
            val retriever = android.media.MediaMetadataRetriever()
            try {
                retriever.setDataSource(context, uri)
                val durationStr = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION)
                val duration = durationStr?.toLong() ?: 5000L // Fallback to 5s if duration unknown
                
                val clip = Clip(
                    filePath = uri.toString(),
                    type = ClipType.VIDEO,
                    startTime = _uiState.value.currentTime,
                    duration = duration
                )
                timelineRepository.addClip(0, clip)
            } catch (e: Exception) {
                Timber.e(e, "Error adding media clip from URI: $uri")
                // Fallback to a default clip if there's an error
                val clip = Clip(
                    filePath = uri.toString(),
                    type = ClipType.VIDEO,
                    startTime = _uiState.value.currentTime,
                    duration = 5000
                )
                timelineRepository.addClip(0, clip)
            } finally {
                retriever.release()
            }
        }
    }
}
