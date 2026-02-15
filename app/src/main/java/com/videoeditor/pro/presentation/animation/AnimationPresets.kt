package com.videoeditor.pro.presentation.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp

/**
 * Pre-defined animation presets for common UI patterns
 * Provides consistent, branded animations throughout the app
 */
@OptIn(ExperimentalAnimationApi::class)
object AnimationPresets {
    
    /**
     * Entrance animations for different UI elements
     */
    object Entrance {
        
        /**
         * Hero element entrance with scale and fade
         */
        fun hero(
            config: MotionConfig,
            delay: Int = 0
        ): EnterTransition {
            return fadeIn(
                animationSpec = tween(
                    durationMillis = (600 * config.durationScale).toInt(),
                    delayMillis = (delay * config.durationScale).toInt(),
                    easing = EaseOutBack
                )
            ) + scaleIn(
                initialScale = 0.8f,
                animationSpec = tween(
                    durationMillis = (600 * config.durationScale).toInt(),
                    delayMillis = (delay * config.durationScale).toInt(),
                    easing = EaseOutBack
                )
            )
        }
        
        /**
         * Slide-up entrance for panels and bottom sheets
         */
        fun slideUp(
            config: MotionConfig,
            delay: Int = 0
        ): EnterTransition {
            return slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(
                    stiffness = config.springStiffness,
                    dampingRatio = config.springDamping
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = (300 * config.durationScale).toInt(),
                    delayMillis = (delay * config.durationScale).toInt()
                )
            )
        }
        
        /**
         * Fade-in entrance for content
         */
        fun fadeIn(
            config: MotionConfig,
            delay: Int = 0
        ): EnterTransition {
            return fadeIn(
                animationSpec = tween(
                    durationMillis = (450 * config.durationScale).toInt(),
                    delayMillis = (delay * config.durationScale).toInt(),
                    easing = EaseOutCubic
                )
            )
        }
        
        /**
         * Staggered entrance for list items
         */
        fun staggered(
            config: MotionConfig,
            index: Int,
            staggerDelay: Int = 100
        ): EnterTransition {
            return fadeIn(
                animationSpec = tween(
                    durationMillis = (400 * config.durationScale).toInt(),
                    delayMillis = (index * staggerDelay * config.durationScale).toInt(),
                    easing = EaseOutCubic
                )
            ) + slideInHorizontally(
                initialOffsetX = { it / 4 },
                animationSpec = tween(
                    durationMillis = (400 * config.durationScale).toInt(),
                    delayMillis = (index * staggerDelay * config.durationScale).toInt(),
                    easing = EaseOutCubic
                )
            )
        }
    }
    
    /**
     * Exit animations for different UI elements
     */
    object Exit {
        
        /**
         * Hero element exit with scale and fade
         */
        fun hero(
            config: MotionConfig
        ): ExitTransition {
            return fadeOut(
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
         * Slide-down exit for panels
         */
        fun slideDown(
            config: MotionConfig
        ): ExitTransition {
            return slideOutVertically(
                targetOffsetY = { it },
                animationSpec = spring(
                    stiffness = config.springStiffness,
                    dampingRatio = config.springDamping
                )
            ) + fadeOut(
                animationSpec = tween(
                    durationMillis = (250 * config.durationScale).toInt(),
                    easing = EaseInCubic
                )
            )
        }
        
        /**
         * Fade-out exit for content
         */
        fun fadeOut(
            config: MotionConfig
        ): ExitTransition {
            return fadeOut(
                animationSpec = tween(
                    durationMillis = (300 * config.durationScale).toInt(),
                    easing = EaseInCubic
                )
            )
        }
    }
    
    /**
     * Content transitions between screens
     */
    object Content {
        
        /**
         * Smooth content transition with slide
         */
        fun slide(
            config: MotionConfig,
            direction: TransitionDirection = TransitionDirection.START
        ): ContentTransform {
            return when (direction) {
                TransitionDirection.START -> slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(
                        durationMillis = (400 * config.durationScale).toInt(),
                        easing = EaseOutCubic
                    )
                ) + fadeIn(
                    animationSpec = tween(
                        durationMillis = (400 * config.durationScale).toInt(),
                        easing = EaseOutCubic
                    )
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(
                        durationMillis = (300 * config.durationScale).toInt(),
                        easing = EaseInCubic
                    )
                ) + fadeOut(
                    animationSpec = tween(
                        durationMillis = (300 * config.durationScale).toInt(),
                        easing = EaseInCubic
                    )
                )
                
                TransitionDirection.END -> slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(
                        durationMillis = (400 * config.durationScale).toInt(),
                        easing = EaseOutCubic
                    )
                ) + fadeIn(
                    animationSpec = tween(
                        durationMillis = (400 * config.durationScale).toInt(),
                        easing = EaseOutCubic
                    )
                ) togetherWith slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(
                        durationMillis = (300 * config.durationScale).toInt(),
                        easing = EaseInCubic
                    )
                ) + fadeOut(
                    animationSpec = tween(
                        durationMillis = (300 * config.durationScale).toInt(),
                        easing = EaseInCubic
                    )
                )
                
                TransitionDirection.UP -> slideInVertically(
                    initialOffsetY = { it },
                    animationSpec = tween(
                        durationMillis = (400 * config.durationScale).toInt(),
                        easing = EaseOutCubic
                    )
                ) + fadeIn(
                    animationSpec = tween(
                        durationMillis = (400 * config.durationScale).toInt(),
                        easing = EaseOutCubic
                    )
                ) togetherWith slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(
                        durationMillis = (300 * config.durationScale).toInt(),
                        easing = EaseInCubic
                    )
                ) + fadeOut(
                    animationSpec = tween(
                        durationMillis = (300 * config.durationScale).toInt(),
                        easing = EaseInCubic
                    )
                )
                
                TransitionDirection.DOWN -> slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(
                        durationMillis = (400 * config.durationScale).toInt(),
                        easing = EaseOutCubic
                    )
                ) + fadeIn(
                    animationSpec = tween(
                        durationMillis = (400 * config.durationScale).toInt(),
                        easing = EaseOutCubic
                    )
                ) togetherWith slideOutVertically(
                    targetOffsetY = { it },
                    animationSpec = tween(
                        durationMillis = (300 * config.durationScale).toInt(),
                        easing = EaseInCubic
                    )
                ) + fadeOut(
                    animationSpec = tween(
                        durationMillis = (300 * config.durationScale).toInt(),
                        easing = EaseInCubic
                    )
                )
            }
        }
        
        /**
         * Scale transition for hero elements
         */
        fun scale(
            config: MotionConfig
        ): ContentTransform {
            return scaleIn(
                initialScale = 0.8f,
                animationSpec = tween(
                    durationMillis = (500 * config.durationScale).toInt(),
                    easing = EaseOutBack
                )
            ) + fadeIn(
                animationSpec = tween(
                    durationMillis = (500 * config.durationScale).toInt(),
                    easing = EaseOutBack
                )
            ) togetherWith scaleOut(
                targetScale = 1.2f,
                animationSpec = tween(
                    durationMillis = (300 * config.durationScale).toInt(),
                    easing = EaseInBack
                )
            ) + fadeOut(
                animationSpec = tween(
                    durationMillis = (300 * config.durationScale).toInt(),
                    easing = EaseInBack
                )
            )
        }
        
        /**
         * Fade transition for simple content changes
         */
        fun fade(
            config: MotionConfig
        ): ContentTransform {
            return fadeIn(
                animationSpec = tween(
                    durationMillis = (350 * config.durationScale).toInt(),
                    easing = EaseOutCubic
                )
            ) togetherWith fadeOut(
                animationSpec = tween(
                    durationMillis = (250 * config.durationScale).toInt(),
                    easing = EaseInCubic
                )
            )
        }
    }
    
    /**
     * Interactive animations for user interactions
     */
    object Interactive {
        
        /**
         * Button press animation
         */
        @Composable
        fun buttonPress(
            config: MotionConfig,
            isPressed: Boolean
        ): State<Float> {
            return animateFloatAsState(
                targetValue = if (isPressed) 0.95f else 1f,
                animationSpec = spring(
                    stiffness = config.springStiffness * 1.5f,
                    dampingRatio = config.springDamping * 0.8f
                ), label = "buttonPress"
            )
        }
        
        /**
         * Card hover/lift animation
         */
        @Composable
        fun cardHover(
            config: MotionConfig,
            isHovered: Boolean
        ): State<Float> {
            return animateFloatAsState(
                targetValue = if (isHovered) 1.05f else 1f,
                animationSpec = spring(
                    stiffness = config.springStiffness * 0.8f,
                    dampingRatio = config.springDamping
                ), label = "cardHover"
            )
        }
        
        /**
         * Selection animation for items
         */
        @Composable
        fun selection(
            config: MotionConfig,
            isSelected: Boolean
        ): State<Float> {
            return animateFloatAsState(
                targetValue = if (isSelected) 1.1f else 1f,
                animationSpec = spring(
                    stiffness = config.springStiffness,
                    dampingRatio = config.springDamping
                ), label = "selection"
            )
        }
        
        /**
         * Loading rotation animation
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
                            durationMillis = (1200 * config.durationScale).toInt(),
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Restart
                    ), label = "loadingRotation"
                )
            } else {
                remember { mutableStateOf(0f) }
            }
        }
        
        /**
         * Pulse animation for notifications
         */
        @Composable
        fun pulse(
            config: MotionConfig,
            isActive: Boolean
        ): State<Float> {
            return if (isActive && config.enableAmbientMotion) {
                rememberInfiniteTransition(label = "pulse").animateFloat(
                    initialValue = 1f,
                    targetValue = 1.2f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = (800 * config.durationScale).toInt(),
                            easing = EaseInOutCubic
                        ),
                        repeatMode = RepeatMode.Reverse
                    ), label = "pulse"
                )
            } else {
                remember { mutableStateOf(1f) }
            }
        }
        
        /**
         * Shake animation for errors
         */
        @Composable
        fun shake(
            config: MotionConfig,
            trigger: Int
        ): State<Float> {
            return animateFloatAsState(
                targetValue = if (trigger % 2 == 0) 0f else 10f,
                animationSpec = tween(
                    durationMillis = (100 * config.durationScale).toInt(),
                    easing = EaseInOutCubic
                ), label = "shake"
            )
        }
    }
    
    /**
     * Ambient animations for background effects
     */
    object Ambient {
        
        /**
         * Slow gradient shift animation
         */
        @Composable
        fun gradientShift(
            config: MotionConfig
        ): State<Float> {
            return if (config.enableAmbientMotion) {
                rememberInfiniteTransition(label = "gradient").animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = (20000 * config.durationScale).toInt(),
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Reverse
                    ), label = "gradientShift"
                )
            } else {
                remember { mutableStateOf(0f) }
            }
        }
        
        /**
         * Subtle floating animation
         */
        @Composable
        fun floating(
            config: MotionConfig
        ): State<Float> {
            return if (config.enableAmbientMotion) {
                rememberInfiniteTransition(label = "floating").animateFloat(
                    initialValue = 0f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = (4000 * config.durationScale).toInt(),
                            easing = EaseInOutSine
                        ),
                        repeatMode = RepeatMode.Reverse
                    ), label = "floating"
                )
            } else {
                remember { mutableStateOf(0f) }
            }
        }
        
        /**
         * Rotation animation for decorative elements
         */
        @Composable
        fun rotation(
            config: MotionConfig,
            duration: Int = 15000
        ): State<Float> {
            return if (config.enableAmbientMotion) {
                rememberInfiniteTransition(label = "rotation").animateFloat(
                    initialValue = 0f,
                    targetValue = 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = (duration * config.durationScale).toInt(),
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Restart
                    ), label = "rotation"
                )
            } else {
                remember { mutableStateOf(0f) }
            }
        }
    }
}
