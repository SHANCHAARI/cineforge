package com.videoeditor.pro.presentation.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.videoeditor.pro.presentation.theme.VideoEditorProTheme
import com.videoeditor.pro.presentation.theme.ThemeMode

@Composable
fun CameraAdjustmentPanel(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!isVisible) return
    
    var brightness by remember { mutableStateOf(0f) }
    var contrast by remember { mutableStateOf(0f) }
    var saturation by remember { mutableStateOf(0f) }
    var exposure by remember { mutableStateOf(0f) }
    var highlights by remember { mutableStateOf(0f) }
    var shadows by remember { mutableStateOf(0f) }
    var temperature by remember { mutableStateOf(0f) }
    var tint by remember { mutableStateOf(0f) }
    
    VideoEditorProTheme(themeMode = ThemeMode.SYSTEM) {
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            // Backdrop
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { onDismiss() }
            )
            
            // Panel
            Card(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .width(320.dp)
                    .height(500.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Camera Adjustments",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        IconButton(onClick = onDismiss) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Adjustment Controls
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Basic Adjustments
                        AdjustmentSection(title = "Basic") {
                            AdjustmentSlider(
                                label = "Brightness",
                                value = brightness,
                                onValueChange = { brightness = it },
                                range = -100f..100f,
                                icon = Icons.Default.Brightness6
                            )
                            
                            AdjustmentSlider(
                                label = "Contrast",
                                value = contrast,
                                onValueChange = { contrast = it },
                                range = -100f..100f,
                                icon = Icons.Default.Contrast
                            )
                            
                            AdjustmentSlider(
                                label = "Saturation",
                                value = saturation,
                                onValueChange = { saturation = it },
                                range = -100f..100f,
                                icon = Icons.Default.Palette
                            )
                        }
                        
                        // Advanced Adjustments
                        AdjustmentSection(title = "Advanced") {
                            AdjustmentSlider(
                                label = "Exposure",
                                value = exposure,
                                onValueChange = { exposure = it },
                                range = -2f..2f,
                                icon = Icons.Default.Exposure
                            )
                            
                            AdjustmentSlider(
                                label = "Highlights",
                                value = highlights,
                                onValueChange = { highlights = it },
                                range = -100f..100f,
                                icon = Icons.Default.Tonality
                            )
                            
                            AdjustmentSlider(
                                label = "Shadows",
                                value = shadows,
                                onValueChange = { shadows = it },
                                range = -100f..100f,
                                icon = Icons.Default.Tonality
                            )
                        }
                        
                        // Color Adjustments
                        AdjustmentSection(title = "Color") {
                            AdjustmentSlider(
                                label = "Temperature",
                                value = temperature,
                                onValueChange = { temperature = it },
                                range = -100f..100f,
                                icon = Icons.Default.WbSunny
                            )
                            
                            AdjustmentSlider(
                                label = "Tint",
                                value = tint,
                                onValueChange = { tint = it },
                                range = -100f..100f,
                                icon = Icons.Default.ColorLens
                            )
                        }
                    }
                    
                    // Action Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                // Reset all adjustments
                                brightness = 0f
                                contrast = 0f
                                saturation = 0f
                                exposure = 0f
                                highlights = 0f
                                shadows = 0f
                                temperature = 0f
                                tint = 0f
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Reset")
                        }
                        
                        Button(
                            onClick = {
                                // Apply adjustments
                                onDismiss()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Apply")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdjustmentSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            content()
        }
    }
}

@Composable
fun AdjustmentSlider(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float>,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
                
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Text(
                text = String.format("%.1f", value),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}

// Preset configurations for common camera adjustments
data class CameraPreset(
    val name: String,
    val brightness: Float = 0f,
    val contrast: Float = 0f,
    val saturation: Float = 0f,
    val exposure: Float = 0f,
    val highlights: Float = 0f,
    val shadows: Float = 0f,
    val temperature: Float = 0f,
    val tint: Float = 0f
)

object CameraPresets {
    val VIVID = CameraPreset(
        name = "Vivid",
        saturation = 20f,
        contrast = 10f,
        brightness = 5f
    )
    
    val CINEMATIC = CameraPreset(
        name = "Cinematic",
        contrast = 15f,
        saturation = -10f,
        shadows = -20f,
        temperature = -10f
    )
    
    val PORTRAIT = CameraPreset(
        name = "Portrait",
        brightness = 10f,
        contrast = 5f,
        saturation = 5f,
        highlights = -10f
    )
    
    val BLACK_AND_WHITE = CameraPreset(
        name = "B&W",
        saturation = -100f,
        contrast = 20f
    )
    
    val VINTAGE = CameraPreset(
        name = "Vintage",
        saturation = -20f,
        temperature = -30f,
        tint = 10f,
        shadows = -15f
    )
    
    fun getAllPresets(): List<CameraPreset> {
        return listOf(VIVID, CINEMATIC, PORTRAIT, BLACK_AND_WHITE, VINTAGE)
    }
}
