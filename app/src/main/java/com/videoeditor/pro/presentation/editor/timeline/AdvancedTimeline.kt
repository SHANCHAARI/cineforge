package com.videoeditor.pro.presentation.editor.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.videoeditor.pro.presentation.theme.TimelineTrackColor
import com.videoeditor.pro.presentation.theme.SecondaryDark

@Composable
fun TimeRuler(
    zoomLevel: Float,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        // TODO: Draw ticks using Canvas
        Text("00:00", color = Color.Gray, modifier = Modifier.padding(2.dp))
    }
}

@Composable
fun AdvancedTimeline(
    state: TimelineState,
    onClipSelected: (String) -> Unit,
    onZoomChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollableState { delta ->
        delta
    }

    // Pinch to Zoom
    val transformableState = rememberTransformableState { zoomChange, _, _ ->
        onZoomChange(zoomChange)
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black) 
            .transformable(state = transformableState)
            .scrollable(
                state = scrollState,
                orientation = Orientation.Horizontal
            )
    ) {
        // Time Ruler
        TimeRuler(
            zoomLevel = state.zoomLevel,
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .align(Alignment.TopCenter)
                .background(Color(0xFF050505))
        )

        // Tracks Container
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 30.dp), 
            contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            items(state.tracks.size) { index ->
                TrackRow(
                    track = state.tracks[index],
                    zoomLevel = state.zoomLevel,
                    onClipClick = onClipSelected,
                    index = index
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        
        // Playhead
        Box(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight()
                .background(Color(0xFFFF3D00))
                .align(Alignment.CenterStart) 
                .offset(x = (state.currentTime * state.zoomLevel).dp)
        )
    }
}

@Composable
fun TrackRow(
    track: TrackUiModel,
    zoomLevel: Float,
    onClipClick: (String) -> Unit,
    index: Int
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(Color(0xFF1A1A1A))
    ) {
        Text(
            text = "T${index + 1}",
            color = Color.DarkGray,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 4.dp)
        )

        track.clips.forEach { clip ->
            ClipItem(
                clip = clip,
                zoomLevel = zoomLevel,
                onClick = { onClipClick(clip.id) }
            )
        }
    }
}

@Composable
fun ClipItem(
    clip: ClipUiModel,
    zoomLevel: Float,
    onClick: () -> Unit
) {
    val startColor = if (clip.isSelected) Color(0xFF6C63FF) else Color(0xFF4A4A6A)
    val endColor = if (clip.isSelected) Color(0xFF3F37C9) else Color(0xFF3A3A5A)
    
    Box(
        modifier = Modifier
            .offset(x = clip.startOffset(zoomLevel))
            .width(clip.width(zoomLevel))
            .fillMaxHeight()
            .padding(vertical = 4.dp, horizontal = 1.dp)
            .background(
                brush = Brush.verticalGradient(listOf(startColor, endColor)),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = if (clip.isSelected) 2.dp else 1.dp,
                color = if (clip.isSelected) Color.White.copy(alpha = 0.8f) else Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Text(
                text = clip.name,
                color = Color.White,
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1
            )
        }
    }
}

