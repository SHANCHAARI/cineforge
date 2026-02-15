package com.videoeditor.pro.presentation.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.videoeditor.pro.presentation.animation.*
import com.videoeditor.pro.presentation.settings.SettingsViewModel
import com.videoeditor.pro.presentation.theme.CineForgeTheme
import com.videoeditor.pro.presentation.theme.CinematicColors
import com.videoeditor.pro.presentation.theme.getGlassmorphismPropertiesFromLevel

@Composable
fun PremiumHomeScreen(
    onNewProjectClick: () -> Unit,
    onOpenProjectClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onProjectSelected: (String) -> Unit,
    settingsViewModel: SettingsViewModel
) {
    val uiState by settingsViewModel.uiState.collectAsState()
    val config = uiState.motionConfig
    
    // Ambient gradient animation
    val gradientShift by MotionSystem.infiniteGradientShift(config, 25000)
    
    CineForgeTheme(
        darkTheme = uiState.isDarkTheme,
        animationLevel = uiState.animationLevel
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            CinematicColors.BackgroundGradientStart,
                            CinematicColors.BackgroundGradientEnd,
                            CinematicColors.BackgroundGradientStart
                        ),
                        startY = gradientShift * -150f,
                        endY = gradientShift * 150f
                    )
                )
        ) {
            // Ambient background elements
            PremiumBackgroundElements(config)
            
            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                // Hero section
                HeroSection(
                    onNewProjectClick = onNewProjectClick,
                    config = config
                )
                
                // Recent projects carousel
                RecentProjectsSection(
                    onProjectSelected = onProjectSelected,
                    config = config
                )
                
                // Quick actions
                QuickActionsSection(
                    onOpenProjectClick = onOpenProjectClick,
                    onSettingsClick = onSettingsClick,
                    config = config
                )
            }
        }
    }
}

@Composable
private fun PremiumBackgroundElements(config: MotionConfig) {
    val rotation by MotionSystem.infiniteRotation(config, 35000)
    val floating by AnimationPresets.Ambient.floating(config)
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Decorative floating elements
        repeat(6) {
            val startX = remember { (100..300).random().dp }
            val startY = remember { (100..500).random().dp }
            
            Box(
                modifier = Modifier
                    .offset(
                        x = startX + (floating * 30).dp,
                        y = startY + (floating * 20).dp
                    )
                    .size(6.dp)
                    .background(
                        CinematicColors.PrimaryGradientStart.copy(alpha = 0.2f),
                        CircleShape
                    )
                    .alpha(0.4f)
            )
        }
        
        // Rotating rings
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 3
            val radius = 150.dp.toPx()
            
            drawDecorativeRing(centerX, centerY, radius, rotation)
        }
    }
}

private fun DrawScope.drawDecorativeRing(
    centerX: Float,
    centerY: Float,
    radius: Float,
    rotation: Float
) {
    val numDots = 16
    val dotRadius = 2.dp.toPx()
    
    for (i in 0 until numDots) {
        val angle = (rotation + (i * 360f / numDots)) * (Math.PI / 180f).toFloat()
        val x = centerX + (radius * kotlin.math.cos(angle))
        val y = centerY + (radius * kotlin.math.sin(angle))
        
        val alpha = 0.2f + (0.1f * kotlin.math.sin(angle))
        
        drawCircle(
            color = CinematicColors.SecondaryGradientStart.copy(alpha = alpha),
            radius = dotRadius,
            center = Offset(x, y)
        )
    }
}

@Composable
private fun HeroSection(
    onNewProjectClick: () -> Unit,
    config: MotionConfig
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by AnimationPresets.Interactive.buttonPress(config, isPressed)
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // App logo with cinematic entrance
        AnimatedVisibility(
            visible = true,
            enter = AnimationPresets.Entrance.hero(config, delay = 0)
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                CinematicColors.PrimaryGradientStart.copy(alpha = 0.3f),
                                CinematicColors.PrimaryGradientEnd.copy(alpha = 0.1f)
                            )
                        )
                    )
                    .border(
                        width = 2.dp,
                        color = CinematicColors.GlassBorder,
                        shape = RoundedCornerShape(28.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "CF",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 2.sp
                )
            }
        }
        
        // App name and tagline
        AnimatedVisibility(
            visible = true,
            enter = AnimationPresets.Entrance.fadeIn(config, delay = 200)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "CineForge",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 3.sp
                )
                
                Text(
                    text = "Professional Video Editing Engine",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    letterSpacing = 1.sp
                )
            }
        }
        
        // Premium new project button
        AnimatedVisibility(
            visible = true,
            enter = AnimationPresets.Entrance.slideUp(config, delay = 400)
        ) {
            val glassProps = getGlassmorphismPropertiesFromLevel(config.level)
            
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(80.dp)
                    .scale(scale)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = { 
                                isPressed = true
                                awaitRelease()
                                isPressed = false
                                onNewProjectClick()
                            }
                        )
                    },
                shape = RoundedCornerShape(glassProps.cornerRadius),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 12.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    CinematicColors.PrimaryGradientStart.copy(alpha = 0.8f),
                                    CinematicColors.PrimaryGradientEnd.copy(alpha = 0.6f)
                                )
                            ),
                            shape = RoundedCornerShape(glassProps.cornerRadius)
                        )
                        .border(
                            width = glassProps.borderWidth.dp,
                            color = CinematicColors.GlassBorder,
                            shape = RoundedCornerShape(glassProps.cornerRadius)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "New Project",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Text(
                            text = "Create New Project",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecentProjectsSection(
    onProjectSelected: (String) -> Unit,
    config: MotionConfig
) {
    val listState = rememberLazyListState()
    val sampleProjects = remember {
        listOf(
            RecentProject("Summer Vacation 2024", "2 days ago", "02:34"),
            RecentProject("Product Demo", "1 week ago", "01:15"),
            RecentProject("Birthday Video", "2 weeks ago", "05:42"),
            RecentProject("Tutorial Series", "1 month ago", "12:30"),
            RecentProject("Wedding Highlights", "2 months ago", "08:20")
        )
    }
    
    AnimatedVisibility(
        visible = true,
        enter = AnimationPresets.Entrance.slideUp(config, delay = 600)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Recent Projects",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.sp
            )
            
            LazyRow(
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sampleProjects) { project ->
                    RecentProjectCard(
                        project = project,
                        onClick = { onProjectSelected(project.id) },
                        config = config
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentProjectCard(
    project: RecentProject,
    onClick: () -> Unit,
    config: MotionConfig
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by AnimationPresets.Interactive.cardHover(config, isPressed)
    
    val glassProps = getGlassmorphismPropertiesFromLevel(config.level)
    
    Card(
        modifier = Modifier
            .width(200.dp)
            .height(120.dp)
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { 
                        isPressed = true
                        awaitRelease()
                        isPressed = false
                        onClick()
                    }
                )
            },
        shape = RoundedCornerShape(glassProps.cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    CinematicColors.GlassSurface,
                    shape = RoundedCornerShape(glassProps.cornerRadius)
                )
                .border(
                    width = glassProps.borderWidth.dp,
                    color = CinematicColors.GlassBorder,
                    shape = RoundedCornerShape(glassProps.cornerRadius)
                )
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = project.name,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                
                Text(
                    text = project.lastModified,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
                
                Text(
                    text = "Duration: ${project.duration}",
                    color = Color.White.copy(alpha = 0.5f),
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun QuickActionsSection(
    onOpenProjectClick: () -> Unit,
    onSettingsClick: () -> Unit,
    config: MotionConfig
) {
    AnimatedVisibility(
        visible = true,
        enter = AnimationPresets.Entrance.slideUp(config, delay = 800)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuickActionButton(
                icon = Icons.Default.FolderOpen,
                label = "Open Project",
                onClick = onOpenProjectClick,
                config = config
            )
            
            QuickActionButton(
                icon = Icons.Default.Settings,
                label = "Settings",
                onClick = onSettingsClick,
                config = config
            )
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    config: MotionConfig
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by AnimationPresets.Interactive.buttonPress(config, isPressed)
    
    val glassProps = getGlassmorphismPropertiesFromLevel(config.level)
    
    Card(
        modifier = Modifier
            .size(100.dp)
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { 
                        isPressed = true
                        awaitRelease()
                        isPressed = false
                        onClick()
                    }
                )
            },
        shape = RoundedCornerShape(glassProps.cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    CinematicColors.GlassSurface,
                    shape = RoundedCornerShape(glassProps.cornerRadius)
                )
                .border(
                    width = glassProps.borderWidth.dp,
                    color = CinematicColors.GlassBorder,
                    shape = RoundedCornerShape(glassProps.cornerRadius)
                )
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = label,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                
                Text(
                    text = label,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

data class RecentProject(
    val name: String,
    val lastModified: String,
    val duration: String,
    val id: String = name.hashCode().toString()
)
