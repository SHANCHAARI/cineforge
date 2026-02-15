package com.videoeditor.pro.presentation.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Centralized Motion System for CineForge
 * Provides consistent, premium animations across the entire app
 */
enum class AnimationLevel {
    LOW,      // Battery Saver - Minimal animations
    MEDIUM,   // Balanced - Standard smooth animations
    HIGH      // Cinematic - Premium, rich animations
}

/**
 * Global animation configuration based on AnimationLevel
 */
data class MotionConfig(
    val animationLevel: AnimationLevel = AnimationLevel.MEDIUM,
    val durationScale: Float = 1.0f,
    val springStiffness: Float = Spring.StiffnessMedium,
    val springDamping: Float = Spring.DampingRatioMediumBouncy,
    val blurIntensity: Float = 0.0f,
    val motionDistance: Dp = 16.dp,
    val enableAmbientMotion: Boolean = true
) {
    val level: AnimationLevel get() = animationLevel

    companion object {
        fun forLevel(level: AnimationLevel): MotionConfig {
            return when (level) {
                AnimationLevel.LOW -> MotionConfig(
                    animationLevel = level,
                    durationScale = 0.5f,
                    springStiffness = Spring.StiffnessMedium,
                    springDamping = Spring.DampingRatioNoBouncy,
                    blurIntensity = 0.0f,
                    motionDistance = 8.dp,
                    enableAmbientMotion = false
                )
                AnimationLevel.MEDIUM -> MotionConfig(
                    animationLevel = level,
                    durationScale = 0.8f,
                    springStiffness = Spring.StiffnessMediumLow,
                    springDamping = Spring.DampingRatioMediumBouncy,
                    blurIntensity = 0.3f,
                    motionDistance = 16.dp,
                    enableAmbientMotion = true
                )
                AnimationLevel.HIGH -> MotionConfig(
                    animationLevel = level,
                    durationScale = 1.2f,
                    springStiffness = Spring.StiffnessLow,
                    springDamping = Spring.DampingRatioMediumBouncy,
                    blurIntensity = 0.6f,
                    motionDistance = 24.dp,
                    enableAmbientMotion = true
                )
            }
        }
    }
}

/**
 * Transition directions for slide animations
 */
enum class TransitionDirection {
    START, END, UP, DOWN
}

/**
 * Centralized motion system providing animation presets and utilities
 */
object MotionSystem {
    
    /**
     * Duration presets for different animation types
     */
    object Durations {
        const val FAST = 150
        const val MEDIUM = 300
        const val SLOW = 500
        const val EXTRA_SLOW = 800
    }
    
    /**
     * Easing presets for smooth, professional animations
     */
    object Easing {
        val ENTER = EaseOutCubic
        val EXIT = EaseInCubic
        val BOUNCE = EaseOutBounce
        val ELASTIC = EaseOutElastic
        val SMOOTH = EaseInOutCubic
    }
    
    /**
     * Spring configurations for different interaction types
     */
    object Springs {
        val GENTLE = spring<Float>(stiffness = Spring.StiffnessMedium, dampingRatio = Spring.DampingRatioMediumBouncy)
        val BOUNCY = spring<Float>(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)
        val SNAPPY = spring<Float>(stiffness = Spring.StiffnessHigh, dampingRatio = Spring.DampingRatioLowBouncy)
        val ELASTIC = spring<Float>(stiffness = Spring.StiffnessVeryLow, dampingRatio = Spring.DampingRatioMediumBouncy)
    }
    
    /**
     * Creates a combined fade + scale transition for premium effects
     */
    @OptIn(ExperimentalAnimationApi::class)
    fun cinematicTransition(
        config: MotionConfig,
        duration: Int = Durations.MEDIUM
    ): ContentTransform {
        return fadeIn(
            animationSpec = tween(
                durationMillis = (duration * 0.6f * config.durationScale).toInt(),
                easing = Easing.ENTER
            )
        ) togetherWith fadeOut(
            animationSpec = tween(
                durationMillis = (duration * 0.4f * config.durationScale).toInt(),
                easing = Easing.EXIT
            )
        )
    }
    
    /**
     * Infinite animation for ambient motion effects
     */
    @Composable
    fun infiniteRotation(
        config: MotionConfig,
        duration: Int = 20000
    ): State<Float> {
        if (!config.enableAmbientMotion) {
            return remember { mutableStateOf(0f) }
        }
        
        return rememberInfiniteTransition(label = "rotation").animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = duration,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            ), label = "rotation"
        )
    }
    
    /**
     * Infinite gradient shift for ambient background motion
     */
    @Composable
    fun infiniteGradientShift(
        config: MotionConfig,
        duration: Int = 15000
    ): State<Float> {
        if (!config.enableAmbientMotion) {
            return remember { mutableStateOf(0f) }
        }
        
        return rememberInfiniteTransition(label = "gradient").animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = (duration * config.durationScale).toInt(),
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            ), label = "gradient"
        )
    }

    /**
     * Spring-based animation for interactive elements
     */
    @Composable
    fun springAnimation(
        config: MotionConfig,
        targetValue: Float,
        initialValue: Float = 0f,
        visibilityThreshold: Float = 0.01f
    ): State<Float> {
        return animateFloatAsState(
            targetValue = targetValue,
            animationSpec = spring(
                stiffness = config.springStiffness,
                dampingRatio = config.springDamping,
                visibilityThreshold = visibilityThreshold
            ),
            label = "springAnimation"
        )
    }

    /**
     * Micro-interaction animation for button presses
     */
    @Composable
    fun pressAnimation(
        config: MotionConfig,
        isPressed: Boolean
    ): State<Float> {
        val target = if (isPressed) 0.95f else 1f
        return springAnimation(
            config = config,
            targetValue = target,
            initialValue = 1f
        )
    }

    /**
     * Glow/pulse animation for selected elements
     */
    @Composable
    fun pulseAnimation(
        config: MotionConfig,
        isActive: Boolean
    ): State<Float> {
        return if (isActive && config.enableAmbientMotion) {
            rememberInfiniteTransition(label = "pulse").animateFloat(
                initialValue = 0.8f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = (2000 * config.durationScale).toInt(),
                        easing = Easing.ENTER
                    ),
                    repeatMode = RepeatMode.Reverse
                ), label = "pulse"
            )
        } else {
            remember { mutableStateOf(1f) }
        }
    }
}
