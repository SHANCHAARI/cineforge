package com.videoeditor.pro.domain.repository

import com.videoeditor.pro.domain.model.Timeline
import com.videoeditor.pro.domain.model.Clip
import kotlinx.coroutines.flow.Flow

interface TimelineRepository {
    fun getTimeline(): Flow<Timeline>
    suspend fun addClip(trackIndex: Int, clip: Clip)
    suspend fun removeClip(clipId: String)
    suspend fun moveClip(clipId: String, newTrackIndex: Int, newStartTime: Long)
    suspend fun splitClip(clipId: String, splitTime: Long)
    suspend fun trimClip(clipId: String, startTrim: Long, endTrim: Long)
}
