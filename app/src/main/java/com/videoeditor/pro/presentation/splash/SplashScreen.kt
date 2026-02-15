package com.videoeditor.pro.presentation.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.videoeditor.pro.presentation.theme.VideoEditorProTheme
import com.videoeditor.pro.presentation.theme.ThemeMode

@Composable
fun SplashScreen(
    onLoadingComplete: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var loadingProgress by remember { mutableStateOf(0f) }
    
    // Simulate loading process
    LaunchedEffect(Unit) {
        val steps = listOf(
            "Initializing Engine...",
            "Loading Media Libraries...",
            "Preparing Timeline...",
            "Ready to Create!"
        )
        
        for ((index, step) in steps.withIndex()) {
            loadingProgress = (index + 1).toFloat() / steps.size
            delay(800)
        }
        
        delay(500)
        isLoading = false
        onLoadingComplete()
    }
    
    VideoEditorProTheme(themeMode = ThemeMode.DARK) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0F0F0F),
                            Color(0xFF1A1A1A),
                            Color(0xFF0F0F0F)
                        )
                    )
                )
        ) {
            // Docker-style animated background
            DockerAnimationBackground(
                modifier = Modifier.fillMaxSize()
            )
            
            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo with animation
                LogoAnimation()
                
                Spacer(modifier = Modifier.height(48.dp))
                
                // App name and tagline
                Text(
                    text = "CineForge",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
                
                Text(
                    text = "Professional Video Editing",
                    fontSize = 16.sp,
                    color = Color(0xFFB0B0B0),
                    letterSpacing = 1.sp
                )
                
                Spacer(modifier = Modifier.height(64.dp))
                
                // Docker-style loading container
                LoadingContainer(
                    isLoading = isLoading,
                    progress = loadingProgress
                )
            }
        }
    }
}

@Composable
fun DockerAnimationBackground(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )
    
    Canvas(modifier = modifier) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        
        // Rotating circles
        drawDockerCircles(centerX, centerY, rotation)
    }
}

private fun DrawScope.drawDockerCircles(
    centerX: Float,
    centerY: Float,
    rotation: Float
) {
    val radius = 150.dp.toPx()
    val circleRadius = 8.dp.toPx()
    val numCircles = 8
    
    for (i in 0 until numCircles) {
        val angle = (rotation + (i * 360f / numCircles)) * (Math.PI / 180f).toFloat()
        val x = centerX + (radius * kotlin.math.cos(angle))
        val y = centerY + (radius * kotlin.math.sin(angle))
        
        drawCircle(
            color = Color(0xFF00D4FF).copy(alpha = 0.3f),
            radius = circleRadius,
            center = Offset(x, y)
        )
    }
}

@Composable
fun LogoAnimation() {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        )
    )
    
    Box(
        modifier = Modifier.size(120.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(120.dp)) {
            val size = this.size
            val strokeWidth = 4.dp.toPx()
            
            // Outer rotating ring
            drawArc(
                color = Color(0xFF00D4FF),
                startAngle = 0f,
                sweepAngle = 270f,
                useCenter = false,
                style = Stroke(width = strokeWidth),
                size = Size(size.width - strokeWidth, size.height - strokeWidth),
                topLeft = Offset(strokeWidth / 2, strokeWidth / 2)
            )
            
            // Film strip decoration
        }
    }
}

private fun DrawScope.drawFilmStrip(size: Size) {
    val stripHeight = 6.dp.toPx()
    val stripWidth = size.width * 0.8f
    val stripX = (size.width - stripWidth) / 2
    
    // Top strip
    drawRect(
        color = Color(0xFF00D4FF).copy(alpha = 0.7f),
        size = Size(stripWidth, stripHeight),
        topLeft = Offset(stripX, 0f)
    )
    
    // Bottom strip
    drawRect(
        color = Color(0xFF00D4FF).copy(alpha = 0.7f),
        size = Size(stripWidth, stripHeight),
        topLeft = Offset(stripX, size.height - stripHeight)
    )
}

@Composable
fun LoadingContainer(
    isLoading: Boolean,
    progress: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isLoading) {
                // Docker-style progress bar
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = Color(0xFF00D4FF),
                    trackColor = Color(0xFF333333)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Loading... ${(progress * 100).toInt()}%",
                    color = Color.White,
                    fontSize = 14.sp
                )
            } else {
                Text(
                    text = "✓ Ready",
                    color = Color(0xFF00FF88),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
