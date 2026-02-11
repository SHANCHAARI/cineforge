package com.videoeditor.pro.presentation.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.videoeditor.pro.presentation.theme.TimelineTrackColor

@Composable
fun TimelineView(
    tracks: List<com.videoeditor.pro.domain.model.Track>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF1E1E1E)) // Dark timeline background
    ) {
        // Time Ruler
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(Color(0xFF252525))
        ) {
            Text(
                text = "00:00:00",
                color = Color.Gray,
                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
            )
        }

        // Tracks
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(tracks.size) { index ->
                TimelineTrack(tracks[index], index)
            }
        }
    }
}

@Composable
fun TimelineTrack(track: com.videoeditor.pro.domain.model.Track, index: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(vertical = 2.dp)
            .background(TimelineTrackColor)
    ) {
        Text(
            text = "Track ${index + 1} (${track.type}) - ${track.clips.size} clips",
            color = Color.White,
            modifier = Modifier.padding(8.dp)
        )
        
        // Render Clips nicely here later
    }
}
