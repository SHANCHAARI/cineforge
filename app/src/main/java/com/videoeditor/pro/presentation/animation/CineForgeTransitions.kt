package com.videoeditor.pro.presentation.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp

/**
 * CineForge-specific transition definitions
 * Provides branded, cinematic transitions for the app
 */
@OptIn(ExperimentalAnimationApi::class)
object CineForgeTransitions {
    
    /**
     * Signature CineForge hero transition
     * Used for major screen transitions and hero elements
     */
    fun heroTransition(
        config: MotionConfig
    ): ContentTransform {
        return fadeIn(
            animationSpec = tween(
                durationMillis = (600 * config.durationScale).toInt(),
                easing = EaseOutBack
            )
        ) + scaleIn(
            initialScale = 0.85f,
            animationSpec = tween(
                durationMillis = (600 * config.durationScale).toInt(),
                easing = EaseOutBack
            )
        ) togetherWith fadeOut(
            animationSpec = tween(
                durationMillis = (300 * config.durationScale).toInt(),
                easing = EaseInQuint
            )
        ) + scaleOut(
            targetScale = 1.1f,
            animationSpec = tween(
                durationMillis = (300 * config.durationScale).toInt(),
                easing = EaseInQuint
            )
        )
    }
    
    /**
     * Smooth cinematic fade with subtle slide
     * Perfect for content transitions within screens
     */
    fun cinematicFade(
        config: MotionConfig,
        slideOffset: Float = 0.1f
    ): ContentTransform {
        return slideInVertically(
            initialOffsetY = { (it * slideOffset).toInt() },
            animationSpec = tween(
                durationMillis = (450 * config.durationScale).toInt(),
                easing = EaseOutCubic
            )
        ) + fadeIn(
            animationSpec = tween(
                durationMillis = (450 * config.durationScale).toInt(),
                easing = EaseOutCubic
            )
        ) togetherWith fadeOut(
            animationSpec = tween(
                durationMillis = (300 * config.durationScale).toInt(),
                easing = EaseInCubic
            )
        )
    }
    
    /**
     * Elastic panel transition
     * Used for bottom sheets, side panels, and floating controls
     */
    fun elasticPanel(
        config: MotionConfig,
        direction: TransitionDirection = TransitionDirection.UP
    ): EnterTransition {
        val slideTransition = when (direction) {
            TransitionDirection.UP -> slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(
                    stiffness = config.springStiffness,
                    dampingRatio = config.springDamping
                )
            )
            TransitionDirection.DOWN -> slideInVertically(
                initialOffsetY = { -it },
                animationSpec = spring(
                    stiffness = config.springStiffness,
                    dampingRatio = config.springDamping
                )
            )
            TransitionDirection.START -> slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = spring(
                    stiffness = config.springStiffness,
                    dampingRatio = config.springDamping
                )
            )
            TransitionDirection.END -> slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = spring(
                    stiffness = config.springStiffness,
                    dampingRatio = config.springDamping
                )
            )
        }
        
        return slideTransition + fadeIn(
            animationSpec = tween(
                durationMillis = (300 * config.durationScale).toInt()
            )
        )
    }
    
    /**
     * Morphing transition for buttons and interactive elements
     */
    @Composable
    fun morphTransition(
        config: MotionConfig,
        isExpanded: Boolean
    ): State<Float> {
        return animateFloatAsState(
            targetValue = if (isExpanded) 1.2f else 1f,
            animationSpec = spring(
                stiffness = config.springStiffness,
                dampingRatio = config.springDamping
            ), label = "morph"
        )
    }
    
    /**
     * Reveal animation for splash screen and loading states
     */
    fun revealTransition(
        config: MotionConfig
    ): EnterTransition {
        return fadeIn(
            animationSpec = tween(
                durationMillis = (800 * config.durationScale).toInt(),
                easing = EaseOutCubic
            )
        ) + scaleIn(
            initialScale = 0.3f,
            animationSpec = tween(
                durationMillis = (800 * config.durationScale).toInt(),
                easing = EaseOutBack
            )
        )
    }
    
    /**
     * Timeline-specific transitions
     */
    object Timeline {
        
        /**
         * Clip selection animation with glow effect
         */
        @Composable
        fun clipSelect(
            config: MotionConfig,
            isSelected: Boolean
        ): State<Float> {
            return animateFloatAsState(
                targetValue = if (isSelected) 1.1f else 1f,
                animationSpec = spring(
                    stiffness = config.springStiffness * 1.2f,
                    dampingRatio = config.springDamping
                ), label = "clipSelect"
            )
        }
        
        /**
         * Playhead movement animation
         */
        @Composable
        fun playheadMove(
            config: MotionConfig,
            position: Float
        ): State<Float> {
            return animateFloatAsState(
                targetValue = position,
                animationSpec = spring(
                    stiffness = config.springStiffness * 1.5f,
                    dampingRatio = config.springDamping * 0.8f
                ), label = "playheadMove"
            )
        }
        
        /**
         * Zoom animation for timeline
         */
        @Composable
        fun zoomTransition(
            config: MotionConfig,
            zoomLevel: Float
        ): State<Float> {
            return animateFloatAsState(
                targetValue = zoomLevel,
                animationSpec = spring(
                    stiffness = config.springStiffness,
                    dampingRatio = config.springDamping
                ), label = "zoom"
            )
        }
        
        /**
         * Clip snap animation when aligning to grid
         */
        @Composable
        fun snapAnimation(
            config: MotionConfig,
            targetPosition: Float,
            currentPosition: Float
        ): State<Float> {
            return animateFloatAsState(
                targetValue = targetPosition,
                animationSpec = spring(
                    stiffness = config.springStiffness * 2f,
                    dampingRatio = config.springDamping * 0.6f
                ), label = "snap"
            )
        }
    }
    
    /**
     * Navigation-specific transitions
     */
    object Navigation {
        
        /**
         * Screen transition with depth effect
         */
        fun screenTransition(
            config: MotionConfig,
            forward: Boolean = true
        ): ContentTransform {
            return slideInHorizontally(
                initialOffsetX = { if (forward) it else -it },
                animationSpec = tween(
                    durationMillis = (400 * config.durationScale).toInt(),
                    easing = EaseOutCubic
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = (400 * config.durationScale).toInt(),
                    easing = EaseOutCubic
                )
            ) togetherWith fadeOut(
                animationSpec = tween(
                    durationMillis = (300 * config.durationScale).toInt(),
                    easing = EaseInCubic
                )
            )
        }
        
        /**
         * Tab transition with subtle scale
         */
        fun tabTransition(
            config: MotionConfig
        ): ContentTransform {
            return fadeIn(
                animationSpec = tween(
                    durationMillis = (250 * config.durationScale).toInt(),
                    easing = EaseOutCubic
                )
            ) + scaleIn(
                initialScale = 0.95f,
                animationSpec = tween(
                    durationMillis = (250 * config.durationScale).toInt(),
                    easing = EaseOutCubic
                )
            ) togetherWith fadeOut(
                animationSpec = tween(
                    durationMillis = (200 * config.durationScale).toInt(),
                    easing = EaseInCubic
                )
            )
        }
    }
    
    /**
     * Micro-interaction presets
     */
    object MicroInteractions {
        
        /**
         * Button press with haptic-like feedback
         */
        @Composable
        fun buttonPress(
            config: MotionConfig,
            isPressed: Boolean
        ): State<Float> {
            return animateFloatAsState(
                targetValue = if (isPressed) 0.92f else 1f,
                animationSpec = spring(
                    stiffness = config.springStiffness * 1.5f,
                    dampingRatio = config.springDamping * 0.7f
                ), label = "buttonPress"
            )
        }
        
        /**
         * Card hover/lift effect
         */
        @Composable
        fun cardLift(
            config: MotionConfig,
            isHovered: Boolean
        ): State<Float> {
            return animateFloatAsState(
                targetValue = if (isHovered) 1.05f else 1f,
                animationSpec = spring(
                    stiffness = config.springStiffness * 0.8f,
                    dampingRatio = config.springDamping
                ), label = "cardLift"
            )
        }
        
        /**
         * Icon rotation for loading states
         */
        @Composable
        fun loadingRotation(
            config: MotionConfig,
            isLoading: Boolean
        ): State<Float> {
            return if (isLoading && config.enableAmbientMotion) {
                rememberInfiniteTransition(label = "loading").animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = (1500 * config.durationScale).toInt(),
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Restart
                    ), label = "rotation"
                )
            } else {
                remember { mutableStateOf(0f) }
            }
        }
        
        /**
         * Pulse effect for notifications and alerts
         */
        @Composable
        fun notificationPulse(
            config: MotionConfig,
            isActive: Boolean
        ): State<Float> {
            return if (isActive && config.enableAmbientMotion) {
                rememberInfiniteTransition(label = "pulse").animateFloat(
                    initialValue = 1f,
                    targetValue = 1.3f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = (1000 * config.durationScale).toInt(),
                            easing = EaseInOutCubic
                        ),
                        repeatMode = RepeatMode.Reverse
                    ), label = "pulse"
                )
            } else {
                remember { mutableStateOf(1f) }
            }
        }
    }
}
