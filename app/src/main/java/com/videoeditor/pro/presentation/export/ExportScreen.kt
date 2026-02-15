package com.videoeditor.pro.presentation.export

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.videoeditor.pro.presentation.theme.VideoEditorColors
import com.videoeditor.pro.presentation.theme.VideoEditorProTheme
import com.videoeditor.pro.presentation.theme.ThemeMode

@Composable
fun ExportScreen(
    onBack: () -> Unit
) {
    var selectedFormat by remember { mutableStateOf("MP4") }
    var selectedQuality by remember { mutableStateOf("1080p") }
    var selectedFrameRate by remember { mutableStateOf("30 fps") }
    var isExporting by remember { mutableStateOf(false) }
    var exportProgress by remember { mutableStateOf(0f) }
    
    VideoEditorProTheme(themeMode = ThemeMode.SYSTEM) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                
                Text(
                    text = "Export Video",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            
            // Export Settings
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Preview Section
                ExportPreviewSection()
                
                // Format Selection
                ExportSection(title = "Format") {
                    FormatSelector(
                        selectedFormat = selectedFormat,
                        onFormatSelected = { selectedFormat = it }
                    )
                }
                
                // Quality Selection
                ExportSection(title = "Quality") {
                    QualitySelector(
                        selectedQuality = selectedQuality,
                        onQualitySelected = { selectedQuality = it }
                    )
                }
                
                // Frame Rate Selection
                ExportSection(title = "Frame Rate") {
                    FrameRateSelector(
                        selectedFrameRate = selectedFrameRate,
                        onFrameRateSelected = { selectedFrameRate = it }
                    )
                }
                
                // Export Button
                if (isExporting) {
                    ExportProgressCard(
                        progress = exportProgress,
                        onCancel = { isExporting = false }
                    )
                } else {
                    Button(
                        onClick = {
                            isExporting = true
                            // Simulate export progress
                            // In real app, this would trigger actual export
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VideoEditorColors.ExportProgress
                        )
                    ) {
                        Icon(
                            Icons.Default.Download,
                            contentDescription = "Export",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = "Export Video",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExportPreviewSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Preview",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Video Preview",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ExportSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            content()
        }
    }
}

@Composable
fun FormatSelector(
    selectedFormat: String,
    onFormatSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        val formats = listOf("MP4", "MOV", "AVI", "MKV", "WebM")
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            formats.forEach { format ->
                FilterChip(
                    onClick = { onFormatSelected(format) },
                    label = { Text(format) },
                    selected = selectedFormat == format
                )
            }
        }
    }
}

@Composable
fun QualitySelector(
    selectedQuality: String,
    onQualitySelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        val qualities = listOf("720p", "1080p", "4K")
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            qualities.forEach { quality ->
                FilterChip(
                    onClick = { onQualitySelected(quality) },
                    label = { Text(quality) },
                    selected = selectedQuality == quality
                )
            }
        }
    }
}

@Composable
fun FrameRateSelector(
    selectedFrameRate: String,
    onFrameRateSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        val frameRates = listOf("24 fps", "30 fps", "60 fps")
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            frameRates.forEach { frameRate ->
                FilterChip(
                    onClick = { onFrameRateSelected(frameRate) },
                    label = { Text(frameRate) },
                    selected = selectedFrameRate == frameRate
                )
            }
        }
    }
}

@Composable
fun ExportProgressCard(
    progress: Float,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = VideoEditorColors.ExportBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = VideoEditorColors.ExportProgress,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "Exporting... ${(progress * 100).toInt()}%",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        }
    }
}
