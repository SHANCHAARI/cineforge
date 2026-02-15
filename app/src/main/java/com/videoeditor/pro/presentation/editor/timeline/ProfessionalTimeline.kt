package com.videoeditor.pro.presentation.editor.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.videoeditor.pro.presentation.theme.VideoEditorColors
import com.videoeditor.pro.presentation.theme.getTimelineColors

@Composable
fun ProfessionalTimeline(
    state: TimelineState,
    onClipSelected: (String) -> Unit,
    onClipMove: (String, Int, Long) -> Unit,
    onZoomChange: (Float) -> Unit,
    onTrackAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timelineColors = getTimelineColors()
    val scrollState = rememberScrollableState { delta -> delta }
    
    Box(modifier = modifier.fillMaxSize()) {
        // Header with time ruler and controls
        TimelineHeader(
            zoomLevel = state.zoomLevel,
            currentTime = state.currentTime,
            onZoomChange = onZoomChange,
            onTrackAdd = onTrackAdd,
            colors = timelineColors
        )
        
        // Tracks container
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 80.dp)
        ) {
            // Track headers and content
            Row(modifier = Modifier.fillMaxSize()) {
                // Track labels column
                TrackLabelsColumn(
                    tracks = state.tracks,
                    colors = timelineColors,
                    modifier = Modifier.width(120.dp)
                )
                
                // Tracks content
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(timelineColors.background)
                        .scrollable(
                            state = scrollState,
                            orientation = Orientation.Horizontal
                        )
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        itemsIndexed(state.tracks) { index, track ->
                            ProfessionalTrackRow(
                                track = track,
                                trackIndex = index,
                                zoomLevel = state.zoomLevel,
                                currentTime = state.currentTime,
                                colors = timelineColors,
                                onClipClick = onClipSelected,
                                onClipMove = onClipMove
                            )
                        }
                    }
                    
                    // Playhead
                    Playhead(
                        currentTime = state.currentTime,
                        zoomLevel = state.zoomLevel,
                        colors = timelineColors,
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            }
        }
    }
}

@Composable
fun TimelineHeader(
    zoomLevel: Float,
    currentTime: Long,
    onZoomChange: (Float) -> Unit,
    onTrackAdd: () -> Unit,
    colors: com.videoeditor.pro.presentation.theme.TimelineColors
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(colors.ruler)
            .padding(8.dp)
    ) {
        // Time ruler (simplified)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(colors.ruler)
        ) {
            Text(
                text = "00:00",
                color = colors.text,
                modifier = Modifier.padding(4.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Zoom controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Zoom controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { onZoomChange(zoomLevel * 0.8f) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.ZoomOut,
                        contentDescription = "Zoom Out",
                        tint = colors.text,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Text(
                    text = "${(zoomLevel * 100).toInt()}%",
                    color = colors.text,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.width(50.dp)
                )
                
                IconButton(
                    onClick = { onZoomChange(zoomLevel * 1.2f) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.ZoomIn,
                        contentDescription = "Zoom In",
                        tint = colors.text,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            
            // Add track button
            IconButton(
                onClick = onTrackAdd,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Track",
                    tint = colors.text,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun TrackLabelsColumn(
    tracks: List<TrackUiModel>,
    colors: com.videoeditor.pro.presentation.theme.TimelineColors,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .background(colors.track)
            .padding(8.dp)
    ) {
        tracks.forEachIndexed { index, track ->
            TrackLabel(
                track = track,
                index = index,
                colors = colors,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
fun TrackLabel(
    track: TrackUiModel,
    index: Int,
    colors: com.videoeditor.pro.presentation.theme.TimelineColors,
    modifier: Modifier = Modifier
) {
    val trackType = when {
        track.clips.any { it.name.contains("video", ignoreCase = true) } -> "video"
        track.clips.any { it.name.contains("audio", ignoreCase = true) } -> "audio"
        track.clips.any { it.name.contains("text", ignoreCase = true) } -> "text"
        track.clips.any { it.name.contains("image", ignoreCase = true) } -> "image"
        else -> "general"
    }
    
    Card(
        modifier = modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(
            containerColor = when (trackType) {
                "video" -> VideoEditorColors.VideoClip
                "audio" -> VideoEditorColors.AudioClip
                "text" -> VideoEditorColors.TextClip
                "image" -> VideoEditorColors.ImageClip
                else -> colors.track
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = getTrackName(trackType, index),
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${track.clips.size} clips",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 10.sp
                )
            }
            
            Icon(
                getTrackIcon(trackType),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun ProfessionalTrackRow(
    track: TrackUiModel,
    trackIndex: Int,
    zoomLevel: Float,
    currentTime: Long,
    colors: com.videoeditor.pro.presentation.theme.TimelineColors,
    onClipClick: (String) -> Unit,
    onClipMove: (String, Int, Long) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(
                if (trackIndex % 2 == 0) colors.track 
                else colors.track.copy(alpha = 0.8f)
            )
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        track.clips.forEach { clip ->
            ProfessionalClipItem(
                clip = clip,
                zoomLevel = zoomLevel,
                colors = colors,
                onClick = { onClipClick(clip.id) },
                onDrag = { deltaX ->
                    val newStartTime = (clip.startTimeMs + deltaX / zoomLevel).toLong().coerceAtLeast(0)
                    onClipMove(clip.id, trackIndex, newStartTime)
                }
            )
        }
    }
}

@Composable
fun ProfessionalClipItem(
    clip: ClipUiModel,
    zoomLevel: Float,
    colors: com.videoeditor.pro.presentation.theme.TimelineColors,
    onClick: () -> Unit,
    onDrag: (Float) -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    
    val clipColor = when {
        clip.name.contains("video", ignoreCase = true) -> VideoEditorColors.VideoClip
        clip.name.contains("audio", ignoreCase = true) -> VideoEditorColors.AudioClip
        clip.name.contains("text", ignoreCase = true) -> VideoEditorColors.TextClip
        clip.name.contains("image", ignoreCase = true) -> VideoEditorColors.ImageClip
        else -> Color(clip.color)
    }
    
    Box(
        modifier = Modifier
            .offset(x = clip.startOffset(zoomLevel) + offsetX.dp)
            .width(clip.width(zoomLevel))
            .fillMaxHeight()
            .clip(RoundedCornerShape(6.dp))
            .background(
                if (clip.isSelected) 
                    clipColor
                else 
                    clipColor
            )
            .border(
                width = if (clip.isSelected) 2.dp else 1.dp,
                color = if (clip.isSelected) colors.playhead else Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(6.dp)
            )
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        offsetX += dragAmount.x
                    },
                    onDragEnd = {
                        onDrag(offsetX)
                        offsetX = 0f
                    }
                )
            }
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = clip.name,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}

@Composable
fun Playhead(
    currentTime: Long,
    zoomLevel: Float,
    colors: com.videoeditor.pro.presentation.theme.TimelineColors,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight()
                .background(colors.playhead)
                .offset(x = (currentTime * zoomLevel / 1000f).dp)
        ) {
            // Playhead handle
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(colors.playhead, RoundedCornerShape(50))
                    .align(Alignment.TopCenter)
                    .offset(y = (-8).dp)
            )
        }
    }
}

private fun getTrackName(type: String, index: Int): String {
    return when (type) {
        "video" -> "Video ${index + 1}"
        "audio" -> "Audio ${index + 1}"
        "text" -> "Text ${index + 1}"
        "image" -> "Image ${index + 1}"
        else -> "Track ${index + 1}"
    }
}

private fun getTrackIcon(type: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when (type) {
        "video" -> Icons.Default.VideoFile
        "audio" -> Icons.Default.AudioFile
        "text" -> Icons.Default.TextFields
        "image" -> Icons.Default.Image
        else -> Icons.Default.Folder
    }
}
