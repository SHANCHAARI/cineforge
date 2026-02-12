package com.videoeditor.pro.presentation.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.videoeditor.pro.presentation.editor.timeline.AdvancedTimeline
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import android.Manifest
import android.os.Build

@Composable
fun EditorScreen(
    onBack: () -> Unit,
    viewModel: EditorViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    
    // Visual media picker (video / image)
    val pickVisualMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.addMediaUri(uri, context)
        }
    }

    // Audio picker
    val pickAudio = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            viewModel.addAudioUri(uri, context)
        }
    }

    // Permission Launcher
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.entries.all { it.value }
        if (isGranted) {
            pickVisualMedia.launch(
                PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageAndVideo
                )
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("New Project", color = Color.White)
                Text(
                    text = "${uiState.resolutionWidth} x ${uiState.resolutionHeight}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.LightGray
                )
            }
            var showAspectMenu by remember { mutableStateOf(false) }
            Box {
                IconButton(onClick = { showAspectMenu = !showAspectMenu }) {
                    Icon(
                        Icons.Default.AspectRatio,
                        contentDescription = "Aspect Ratio",
                        tint = Color.White
                    )
                }
                DropdownMenu(
                    expanded = showAspectMenu,
                    onDismissRequest = { showAspectMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Vertical 9:16 (1080x1920)") },
                        onClick = {
                            viewModel.setAspectRatio(1080, 1920)
                            showAspectMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Horizontal 16:9 (1920x1080)") },
                        onClick = {
                            viewModel.setAspectRatio(1920, 1080)
                            showAspectMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Square 1:1 (1080x1080)") },
                        onClick = {
                            viewModel.setAspectRatio(1080, 1080)
                            showAspectMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("4:5 Portrait (1080x1350)") },
                        onClick = {
                            viewModel.setAspectRatio(1080, 1350)
                            showAspectMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("2:1 Cinema (1920x960)") },
                        onClick = {
                            viewModel.setAspectRatio(1920, 960)
                            showAspectMenu = false
                        }
                    )
                }
            }
        }

        // Preview Area (Top 40%)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .background(Color.Black)
        ) {
            val aspect = if (uiState.resolutionHeight != 0) {
                uiState.resolutionWidth.toFloat() / uiState.resolutionHeight.toFloat()
            } else {
                9f / 16f
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspect)
                    .align(Alignment.Center)
            ) {
                PreviewSurface()
            }
        }

        // Timeline Area (Bottom 60%)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
        ) {
            AdvancedTimeline(
                state = uiState,
                onClipSelected = viewModel::selectClip,
                onClipMove = viewModel::moveClip,
                onZoomChange = viewModel::updateZoom
            )
        }
        } // Closing the Screen Column here

        // Tool Panel (Floating)
        if (uiState.selectedClipId != null) {
            var showGrading by remember { mutableStateOf(false) }

            Box(modifier = Modifier.fillMaxSize()) {
                // Actions Column
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SmallFloatingActionButton(
                        onClick = { viewModel.splitSelectedClip() },
                        containerColor = MaterialTheme.colorScheme.tertiary
                    ) {
                        Icon(Icons.Default.ContentCut, contentDescription = "Split Clip")
                    }
                    
                    SmallFloatingActionButton(
                        onClick = { viewModel.addTextClip("Sample Text") },
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Icon(Icons.Default.TextFields, contentDescription = "Add Text")
                    }

                    SmallFloatingActionButton(
                        onClick = { 
                            pickAudio.launch("audio/*")
                        },
                        containerColor = Color(0xFF2D6A4F)
                    ) {
                        Icon(Icons.Default.MusicNote, contentDescription = "Add Audio")
                    }

                    SmallFloatingActionButton(
                        onClick = { showGrading = !showGrading },
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ) {
                        Icon(Icons.Default.Palette, contentDescription = "Color Grading")
                    }
                }

                // Grading Panel
                if (showGrading) {
                    var b by remember { mutableStateOf(1.0f) }
                    var c by remember { mutableStateOf(1.0f) }
                    var s by remember { mutableStateOf(1.0f) }

                    Card(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 80.dp, end = 16.dp)
                            .width(200.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Color Grading", style = MaterialTheme.typography.labelLarge)
                            Spacer(Modifier.height(8.dp))
                            
                            GradingSlider("Brightness", b) { 
                                b = it
                                viewModel.setLiveColorGrading(b, c, s)
                            }
                            GradingSlider("Contrast", c) { 
                                c = it
                                viewModel.setLiveColorGrading(b, c, s)
                            }
                            GradingSlider("Saturation", s) { 
                                s = it
                                viewModel.setLiveColorGrading(b, c, s)
                            }
                        }
                    }
                }
            }
        }

        // FAB for adding layers
        FloatingActionButton(
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    launcher.launch(arrayOf(
                        Manifest.permission.READ_MEDIA_VIDEO,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ))
                } else {
                    launcher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                }
            },
            containerColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Layer")
        }

        // Play Button (will be wired to timeline playhead)
        FloatingActionButton(
            onClick = { viewModel.togglePlayPause() },
            containerColor = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .size(48.dp)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Play")
        }
    }
}

@Composable
fun GradingSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column {
        Text(
            text = "$label: ${String.format("%.2f", value)}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..2f,
            modifier = Modifier.height(32.dp)
        )
    }
}
