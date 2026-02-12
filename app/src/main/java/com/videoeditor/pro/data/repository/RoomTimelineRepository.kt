package com.videoeditor.pro.data.repository

import com.google.gson.Gson
import com.videoeditor.pro.data.local.ClipDao
import com.videoeditor.pro.data.local.DbClip
import com.videoeditor.pro.domain.model.Clip
import com.videoeditor.pro.domain.model.ClipProperties
import com.videoeditor.pro.domain.model.ClipType
import com.videoeditor.pro.domain.model.Timeline
import com.videoeditor.pro.domain.model.Track
import com.videoeditor.pro.domain.model.TrackType
import com.videoeditor.pro.domain.repository.TimelineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomTimelineRepository @Inject constructor(
    private val clipDao: ClipDao,
    private val gson: Gson
) : TimelineRepository {

    override fun getTimeline(): Flow<Timeline> {
        return clipDao.getAllClipsFlow().map { dbClips ->
            val byTrack = dbClips.groupBy { it.trackIndex }

            val tracks = listOf(0, 1, 2).map { index ->
                val type = when (index) {
                    0 -> TrackType.VIDEO
                    1 -> TrackType.AUDIO
                    2 -> TrackType.TEXT
                    else -> TrackType.EFFECT
                }

                val clips = byTrack[index]
                    ?.map { it.toDomainClip(gson) }
                    ?.sortedBy { it.startTime }
                    ?: emptyList()

                Track(
                    type = type,
                    clips = clips
                )
            }

            val maxEnd = tracks
                .flatMap { it.clips }
                .maxOfOrNull { it.startTime + it.duration } ?: 0L

            Timeline(
                tracks = tracks,
                duration = maxEnd
            )
        }
    }

    override suspend fun addClip(trackIndex: Int, clip: Clip) {
        val db = clip.toDbClip(trackIndex, gson)
        clipDao.insertClip(db)
    }

    override suspend fun removeClip(clipId: String) {
        clipDao.deleteClipById(clipId)
    }

    override suspend fun moveClip(clipId: String, newTrackIndex: Int, newStartTime: Long) {
        val all = clipDao.getAllClips()
        val target = all.firstOrNull { it.id == clipId } ?: return
        val duration = target.duration
        val updated = target.copy(
            trackIndex = newTrackIndex,
            startTime = newStartTime,
            // keep same duration; end is derived in domain layer
            duration = duration
        )
        clipDao.updateClip(updated)
    }

    override suspend fun splitClip(clipId: String, splitTime: Long) {
        val all = clipDao.getAllClips()
        val target = all.firstOrNull { it.id == clipId } ?: return

        val clipStart = target.startTime
        val clipEnd = target.startTime + target.duration

        if (splitTime <= clipStart || splitTime >= clipEnd) return

        val firstPartDuration = splitTime - clipStart
        val secondPartDuration = clipEnd - splitTime
        val secondSourceOffset = target.sourceStartTime + firstPartDuration

        val first = target.copy(
            duration = firstPartDuration
        )

        val second = target.copy(
            id = UUID.randomUUID().toString(),
            startTime = splitTime,
            duration = secondPartDuration,
            sourceStartTime = secondSourceOffset
        )

        clipDao.updateClip(first)
        clipDao.insertClip(second)
    }

    override suspend fun trimClip(clipId: String, startTrim: Long, endTrim: Long) {
        val all = clipDao.getAllClips()
        val target = all.firstOrNull { it.id == clipId } ?: return

        val newStart = target.startTime + startTrim
        val newDuration = (target.duration - startTrim - endTrim).coerceAtLeast(0L)
        val newSourceStart = target.sourceStartTime + startTrim

        val updated = target.copy(
            startTime = newStart,
            duration = newDuration,
            sourceStartTime = newSourceStart
        )
        clipDao.updateClip(updated)
    }

    private fun Clip.toDbClip(trackIndex: Int, gson: Gson): DbClip {
        val propsJson = gson.toJson(properties)
        return DbClip(
            id = id,
            trackIndex = trackIndex,
            filePath = filePath,
            type = type.name,
            startTime = startTime,
            duration = duration,
            sourceStartTime = sourceStartTime,
            propertiesJson = propsJson
        )
    }

    private fun DbClip.toDomainClip(gson: Gson): Clip {
        val clipType = runCatching { ClipType.valueOf(type) }.getOrElse { ClipType.VIDEO }
        val props = runCatching {
            gson.fromJson(propertiesJson, ClipProperties::class.java)
        }.getOrDefault(ClipProperties())

        return Clip(
            id = id,
            filePath = filePath,
            type = clipType,
            startTime = startTime,
            duration = duration,
            sourceStartTime = sourceStartTime,
            properties = props
        )
    }
}

