package com.videoeditor.pro.presentation.splash

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.videoeditor.pro.presentation.animation.*
import com.videoeditor.pro.presentation.settings.SettingsViewModel
import com.videoeditor.pro.R

private val primaryColor = Color(0xFF00D4FF)

@Composable
fun CinematicSplashScreen(
    onLoadingComplete: () -> Unit,
    settingsViewModel: SettingsViewModel
) {
    val uiState by settingsViewModel.uiState.collectAsState()
    val config = uiState.motionConfig
    
    var isLoading by remember { mutableStateOf(true) }
    var loadingProgress by remember { mutableStateOf(0f) }
    var currentStep by remember { mutableStateOf(0) }
    
    val loadingSteps = listOf(
        "Initializing CineForge Engine...",
        "Loading Media Libraries...",
        "Preparing Timeline...",
        "Optimizing Performance...",
        "Ready to Create!"
    )
    
    // Simulate loading process with animation
    LaunchedEffect(Unit) {
        for ((index, step) in loadingSteps.withIndex()) {
            currentStep = index
            loadingProgress = (index + 1).toFloat() / loadingSteps.size
            delay(600)
        }
        
        delay(800)
        isLoading = false
        onLoadingComplete()
    }
    
    // Ambient gradient animation
    val gradientShift by MotionSystem.infiniteGradientShift(config, 20000)
    
    // Logo reveal animation
    val logoScale by animateFloatAsState(
        targetValue = if (isLoading) 1f else 0.8f,
        animationSpec = spring(
            stiffness = config.springStiffness * 0.8f,
            dampingRatio = config.springDamping
        ), label = "logoScale"
    )
    
    val logoAlpha by animateFloatAsState(
        targetValue = if (isLoading) 1f else 0f,
        animationSpec = tween(
            durationMillis = (800 * config.durationScale).toInt(),
            easing = EaseInOutCubic
        ), label = "logoAlpha"
    )
    
    // Content fade animation
    val contentAlpha by animateFloatAsState(
        targetValue = if (isLoading) 1f else 0f,
        animationSpec = tween(
            durationMillis = (600 * config.durationScale).toInt(),
            easing = EaseInOutCubic
        ), label = "contentAlpha"
    )
    
    // Premium colors
    val backgroundColor = Color(0xFF0A0A0F)
    val surfaceColor = Color(0xFF1A1A2E)
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor,
                        surfaceColor,
                        backgroundColor
                    ),
                    startY = gradientShift * -100f,
                    endY = gradientShift * 100f
                )
            )
        ) {
            // Cinematic background elements
            CinematicBackgroundElements(config)
            
            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Logo with cinematic reveal
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .scale(logoScale)
                        .alpha(logoAlpha),
                    contentAlignment = Alignment.Center
                ) {
                    CinematicLogo(config)
                }
                
                Spacer(modifier = Modifier.height(48.dp))
                
                // User Profile Section
                AnimatedVisibility(
                    visible = isLoading,
                    enter = AnimationPresets.Entrance.slideUp(config, delay = 300),
                    exit = fadeOut(tween(300))
                ) {
                    UserProfileSection(modifier = Modifier.alpha(contentAlpha))
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // App name with cinematic entrance
                AnimatedVisibility(
                    visible = isLoading,
                    enter = AnimationPresets.Entrance.hero(config, delay = 200),
                    exit = fadeOut(tween(300))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "CineForge",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 4.sp,
                            modifier = Modifier.alpha(contentAlpha)
                        )
                        
                        Text(
                            text = "Professional Video Editing Engine",
                            fontSize = 16.sp,
                            color = Color.White.copy(alpha = 0.8f),
                            letterSpacing = 1.sp,
                            modifier = Modifier.alpha(contentAlpha)
                        )
                        
                        Text(
                            text = "Built by Vidya Sagar",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.6f),
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.alpha(contentAlpha * 0.7f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(64.dp))
                
                // Cinematic loading container
                AnimatedVisibility(
                    visible = isLoading,
                    enter = AnimationPresets.Entrance.slideUp(config, delay = 400),
                    exit = fadeOut(tween(300))
                ) {
                    CinematicLoadingContainer(
                        progress = loadingProgress,
                        currentStep = loadingSteps.getOrNull(currentStep) ?: "",
                        config = config,
                        modifier = Modifier.alpha(contentAlpha)
                    )
                }
            }
        }
}

@Composable
private fun CinematicBackgroundElements(config: MotionConfig) {
    val rotation by MotionSystem.infiniteRotation(config, 30000)
    val floating by MotionSystem.infiniteGradientShift(config, 4000)
    
    Box(modifier = Modifier.fillMaxSize()) {
        // Decorative floating elements
        repeat(6) {
            val startX = remember { (100..300).random() }
            val startY = remember { (100..500).random() }
            
            Box(
                modifier = Modifier
                    .offset(
                        x = startX.dp + (floating * 30).dp,
                        y = startY.dp + (floating * 20).dp
                    )
                    .size(6.dp)
                    .background(
                        Color.White.copy(alpha = 0.3f),
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
    val numDots = 12
    val dotRadius = 3.dp.toPx()
    
    for (i in 0 until numDots) {
        val angle = (rotation + (i * 360f / numDots)) * (Math.PI / 180f).toFloat()
        val x = centerX + (radius * kotlin.math.cos(angle))
        val y = centerY + (radius * kotlin.math.sin(angle))
        
        val alpha = 0.3f + (0.2f * kotlin.math.sin(angle * 2))
        
        drawCircle(
            color = primaryColor.copy(alpha = alpha),
            radius = dotRadius,
            center = Offset(x, y)
        )
    }
}

@Composable
private fun CinematicLogo(config: MotionConfig) {
    val pulseAnimation by AnimationPresets.Interactive.pulse(config, true)
    
    Box(
        modifier = Modifier
            .scale(pulseAnimation)
            .fillMaxSize()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        primaryColor.copy(alpha = 0.3f),
                        primaryColor.copy(alpha = 0.1f)
                    )
                )
            )
            .border(
                width = 2.dp,
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(24.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        // Logo text
        Text(
            text = "CF",
            fontSize = 64.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 2.sp
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CinematicLoadingContainer(
    progress: Float,
    currentStep: String,
    config: MotionConfig,
    modifier: Modifier = Modifier
) {
    val cornerRadius = when (config.animationLevel) {
        AnimationLevel.LOW -> 20.dp
        AnimationLevel.MEDIUM -> 24.dp
        AnimationLevel.HIGH -> 28.dp
    }
    
    val borderWidth = when (config.animationLevel) {
        AnimationLevel.LOW -> 1.dp
        AnimationLevel.MEDIUM -> 1.5f.dp
        AnimationLevel.HIGH -> 2.dp
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(cornerRadius),
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
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x1AFFFFFF),
                            Color(0x0DFFFFFF)
                        )
                    ),
                    shape = RoundedCornerShape(cornerRadius)
                )
                .border(
                    width = borderWidth,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0x33FFFFFF),
                            Color(0x1AFFFFFF)
                        )
                    ),
                    shape = RoundedCornerShape(cornerRadius)
                )
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Progress bar with cinematic styling
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(
                            Color.White.copy(alpha = 0.1f),
                            RoundedCornerShape(4.dp)
                        )
                        .clip(RoundedCornerShape(4.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        primaryColor,
                                        Color(0xFF0091EA)
                                    )
                                )
                            )
                    )
                }
                
                // Loading text with cinematic fade
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = {
                        fadeIn(
                            animationSpec = tween(
                                durationMillis = (300 * config.durationScale).toInt(),
                                easing = EaseOutCubic
                            )
                        ).togetherWith(fadeOut(
                            animationSpec = tween(
                                durationMillis = (200 * config.durationScale).toInt(),
                                easing = EaseInCubic
                            )
                        ))
                    }, label = "currentStep"
                ) { step ->
                    Text(
                        text = step,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    )
                }
                
                // Progress percentage
                Text(
                    text = "${(progress * 100).toInt()}%",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun UserProfileSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Profile Avatar with cinematic styling
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            primaryColor.copy(alpha = 0.3f),
                            primaryColor.copy(alpha = 0.1f)
                        )
                    )
                )
                .border(
                    width = 2.dp,
                    color = Color.White.copy(alpha = 0.3f),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // User Profile Image - Replace with your actual image resource
            Image(
                painter = painterResource(id = R.drawable.profile_placeholder), // Replace with your photo
                contentDescription = "Vidya Sagar Profile",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
            )
        }
        
        // Profile Text
        Text(
            text = "Vidya Sagar",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.9f),
            letterSpacing = 0.5.sp
        )
        
        Text(
            text = "App Creator & Developer",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.6f),
            letterSpacing = 0.3.sp
        )
    }
}
