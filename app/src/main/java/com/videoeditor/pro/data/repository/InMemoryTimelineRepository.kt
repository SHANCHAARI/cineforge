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
        _timeline.update { currentTimeline ->
            val newTracks = currentTimeline.tracks.map { track ->
                track.copy(clips = track.clips.filter { it.id != clipId })
            }
            currentTimeline.copy(tracks = newTracks)
        }
    }

    override suspend fun moveClip(clipId: String, newTrackIndex: Int, newStartTime: Long) {
        _timeline.update { currentTimeline ->
            var targetClip: Clip? = null
            val cleanedTracks = currentTimeline.tracks.map { track ->
                val remainingClips = track.clips.filter { 
                    if (it.id == clipId) {
                        targetClip = it
                        false
                    } else true
                }
                track.copy(clips = remainingClips)
            }

            if (targetClip != null && newTrackIndex in cleanedTracks.indices) {
                val updatedTracks = cleanedTracks.toMutableList()
                val targetTrack = updatedTracks[newTrackIndex]
                val newClips = targetTrack.clips.toMutableList().apply { 
                    add(targetClip!!.copy(startTime = newStartTime))
                }
                updatedTracks[newTrackIndex] = targetTrack.copy(clips = newClips)
                currentTimeline.copy(tracks = updatedTracks)
            } else {
                currentTimeline // No change if clip not found or invalid track
            }
        }
    }

    override suspend fun splitClip(clipId: String, splitTime: Long) {
        _timeline.update { currentTimeline ->
            val newTracks = currentTimeline.tracks.map { track ->
                val newClips = mutableListOf<Clip>()
                track.clips.forEach { clip ->
                    if (clip.id == clipId && splitTime > clip.startTime && splitTime < clip.startTime + clip.duration) {
                        val firstPartDuration = splitTime - clip.startTime
                        val secondPartOffset = clip.sourceStartTime + firstPartDuration
                        val secondPartDuration = clip.duration - firstPartDuration
                        
                        // First part
                        newClips.add(clip.copy(duration = firstPartDuration))
                        // Second part
                        newClips.add(clip.copy(
                            id = java.util.UUID.randomUUID().toString(),
                            startTime = splitTime,
                            duration = secondPartDuration,
                            sourceStartTime = secondPartOffset
                        ))
                    } else {
                        newClips.add(clip)
                    }
                }
                track.copy(clips = newClips)
            }
            currentTimeline.copy(tracks = newTracks)
        }
    }

    override suspend fun trimClip(clipId: String, startTrim: Long, endTrim: Long) {
        _timeline.update { currentTimeline ->
            val newTracks = currentTimeline.tracks.map { track ->
                track.copy(clips = track.clips.map { clip ->
                    if (clip.id == clipId) {
                        clip.copy(
                            startTime = clip.startTime + startTrim,
                            duration = clip.duration - startTrim - endTrim,
                            sourceStartTime = clip.sourceStartTime + startTrim
                        )
                    } else clip
                })
            }
            currentTimeline.copy(tracks = newTracks)
        }
    }
}
