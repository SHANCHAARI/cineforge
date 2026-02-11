package com.videoeditor.pro.data.repository

import com.videoeditor.pro.domain.model.Clip
import com.videoeditor.pro.domain.model.Timeline
import com.videoeditor.pro.domain.model.Track
import com.videoeditor.pro.domain.model.TrackType
import com.videoeditor.pro.domain.repository.TimelineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryTimelineRepository @Inject constructor() : TimelineRepository {

    private val _timeline = MutableStateFlow(
        Timeline(
            tracks = listOf(
                Track(type = TrackType.VIDEO),
                Track(type = TrackType.AUDIO),
                Track(type = TrackType.TEXT)
            )
        )
    )

    override fun getTimeline(): Flow<Timeline> = _timeline.asStateFlow()

    override suspend fun addClip(trackIndex: Int, clip: Clip) {
        _timeline.update { currentTimeline ->
            val newTracks = currentTimeline.tracks.toMutableList()
            if (trackIndex in newTracks.indices) {
                val track = newTracks[trackIndex]
                val newClips = track.clips.toMutableList().apply { add(clip) }
                newTracks[trackIndex] = track.copy(clips = newClips)
            }
            currentTimeline.copy(tracks = newTracks)
        }
    }

    override suspend fun removeClip(clipId: String) {
        // TODO: Implement removal logic
    }

    override suspend fun moveClip(clipId: String, newTrackIndex: Int, newStartTime: Long) {
        // TODO: Implement move logic
    }
}
