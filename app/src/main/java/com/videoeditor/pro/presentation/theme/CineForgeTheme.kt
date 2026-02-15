package com.videoeditor.pro.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.videoeditor.pro.presentation.animation.AnimationLevel
import com.videoeditor.pro.presentation.animation.MotionConfig
import com.videoeditor.pro.presentation.settings.SettingsViewModel

// Cinematic Studio Pro - Premium Dark Theme
private val CinematicDarkColorScheme = darkColorScheme(
    primary = Color(0xFF00D4FF),
    onPrimary = Color(0xFF003547),
    primaryContainer = Color(0xFF004D66),
    onPrimaryContainer = Color(0xFFB3E5FF),
    
    secondary = Color(0xFF7C5FFF),
    onSecondary = Color(0xFF2D006E),
    secondaryContainer = Color(0xFF4500A0),
    onSecondaryContainer = Color(0xFFD4BBFF),
    
    tertiary = Color(0xFF00BFA5),
    onTertiary = Color(0xFF003730),
    tertiaryContainer = Color(0xFF005047),
    onTertiaryContainer = Color(0xFF6FFFE0),
    
    error = Color(0xFFFF5252),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    
    background = Color(0xFF0A0A0F),
    onBackground = Color(0xFFE0E0E5),
    surface = Color(0xFF12121A),
    onSurface = Color(0xFFE0E0E5),
    surfaceVariant = Color(0xFF1E1E2E),
    onSurfaceVariant = Color(0xFFB0B0C0),
    
    outline = Color(0xFF404055),
    outlineVariant = Color(0xFF2A2A3A),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFFE0E0E5),
    inverseOnSurface = Color(0xFF1A1A2A),
    inversePrimary = Color(0xFF006685)
)

// Premium Light Theme
private val CinematicLightColorScheme = lightColorScheme(
    primary = Color(0xFF0091EA),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE1F5FE),
    onPrimaryContainer = Color(0xFF001D36),
    
    secondary = Color(0xFF6200EA),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE8DDFF),
    onSecondaryContainer = Color(0xFF1D0050),
    
    tertiary = Color(0xFF00BFA5),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFB2EBF2),
    onTertiaryContainer = Color(0xFF00201A),
    
    error = Color(0xFFD32F2F),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFEBEE),
    onErrorContainer = Color(0xFF410002),
    
    background = Color(0xFFFAFAFF),
    onBackground = Color(0xFF1C1C2A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1C1C2A),
    surfaceVariant = Color(0xFFF5F5FA),
    onSurfaceVariant = Color(0xFF494559),
    
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFF31303A),
    inverseOnSurface = Color(0xFFF4EFF4),
    inversePrimary = Color(0xFF90CAF9)
)

// Premium Cinematic Colors
object CinematicColors {
    // Background Gradients
    val BackgroundGradientStart = Color(0xFF0A0A0F)
    val BackgroundGradientEnd = Color(0xFF1A1A2E)
    val SurfaceGradientStart = Color(0xFF12121A)
    val SurfaceGradientEnd = Color(0xFF1E1E2E)
    
    // Accent Gradients
    val PrimaryGradientStart = Color(0xFF00D4FF)
    val PrimaryGradientEnd = Color(0xFF0091EA)
    val SecondaryGradientStart = Color(0xFF7C5FFF)
    val SecondaryGradientEnd = Color(0xFF6200EA)
    
    // Glassmorphism
    val GlassSurface = Color(0x1AFFFFFF)
    val GlassBorder = Color(0x33FFFFFF)
    val GlassShadow = Color(0x4D000000)
    
    // Timeline Colors
    val TimelineBackground = Color(0xFF0F0F15)
    val TimelineTrack = Color(0xFF1A1A2E)
    val TimelineTrackSelected = Color(0xFF25253A)
    val Playhead = Color(0xFFFF4081)
    val RulerBackground = Color(0xFF0D0D15)
    
    // Clip Colors
    val VideoClip = Color(0xFF4CAF50)
    val AudioClip = Color(0xFF2196F3)
    val TextClip = Color(0xFFFF9800)
    val ImageClip = Color(0xFF9C27B0)
    val EffectClip = Color(0xFF00BCD4)
    
    // UI Elements
    val ToolbarBackground = Color(0xFF0F0F15)
    val PanelBackground = Color(0xFF1A1A2E)
    val Accent = Color(0xFF00D4FF)
    val Warning = Color(0xFFFF9800)
    val Success = Color(0xFF4CAF50)
    
    // Export Colors
    val ExportProgress = Color(0xFF00D4FF)
    val ExportBackground = Color(0xFF0A0A0F)
    
    // Premium Effects
    val GlowPrimary = Color(0x4D00D4FF)
    val GlowSecondary = Color(0x4D7C5FFF)
    val GlowSuccess = Color(0x4D4CAF50)
    val GlowWarning = Color(0x4DFFFF9800)
}

// Glassmorphism Properties
data class GlassmorphismProperties(
    val surfaceColor: Color,
    val borderColor: Color,
    val shadowColor: Color,
    val blurRadius: Float,
    val borderWidth: Float,
    val cornerRadius: Float
) {
    companion object {
        fun forLevel(level: AnimationLevel): GlassmorphismProperties {
            return when (level) {
                AnimationLevel.LOW -> GlassmorphismProperties(
                    surfaceColor = Color(0x0DFFFFFF),
                    borderColor = Color(0x1AFFFFFF),
                    shadowColor = Color(0x26000000),
                    blurRadius = 8f,
                    borderWidth = 1f,
                    cornerRadius = 20f
                )
                AnimationLevel.MEDIUM -> GlassmorphismProperties(
                    surfaceColor = Color(0x1AFFFFFF),
                    borderColor = Color(0x33FFFFFF),
                    shadowColor = Color(0x4D000000),
                    blurRadius = 16f,
                    borderWidth = 1.5f,
                    cornerRadius = 24f
                )
                AnimationLevel.HIGH -> GlassmorphismProperties(
                    surfaceColor = Color(0x26FFFFFF),
                    borderColor = Color(0x4DFFFFFF),
                    shadowColor = Color(0x80000000),
                    blurRadius = 24f,
                    borderWidth = 2f,
                    cornerRadius = 28f
                )
            }
        }
    }
}

@Composable
fun CineForgeTheme(
    darkTheme: Boolean = true, // Default to dark for cinematic feel
    animationLevel: AnimationLevel = AnimationLevel.MEDIUM,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        CinematicDarkColorScheme
    } else {
        CinematicLightColorScheme
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

@Composable
fun CineForgeThemeWithSettings(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val uiState by settingsViewModel.uiState.collectAsState()
    
    CineForgeTheme(
        darkTheme = uiState.isDarkTheme,
        animationLevel = uiState.animationLevel,
        content = content
    )
}

@Composable
fun isSystemDarkTheme(): Boolean {
    return MaterialTheme.colorScheme.background == Color(0xFF0A0A0F)
}

@Composable
fun getMotionConfigFromLevel(animationLevel: AnimationLevel): MotionConfig {
    return MotionConfig.forLevel(animationLevel)
}

@Composable
fun getGlassmorphismPropertiesFromLevel(animationLevel: AnimationLevel): GlassmorphismProperties {
    return GlassmorphismProperties.forLevel(animationLevel)
}

@Composable
fun getCinematicColorsSet(): CinematicColors {
    return CinematicColors
}

// Extended theme functions for video editor
@Composable
fun getTimelineColorsSet(): TimelineColors {
    return if (isSystemDarkTheme()) {
        TimelineColors.Dark
    } else {
        TimelineColors.Light
    }
}

data class TimelineColors(
    val background: Color,
    val track: Color,
    val trackSelected: Color,
    val playhead: Color,
    val ruler: Color,
    val text: Color
) {
    companion object {
        val Dark = TimelineColors(
            background = CinematicColors.TimelineBackground,
            track = CinematicColors.TimelineTrack,
            trackSelected = CinematicColors.TimelineTrackSelected,
            playhead = CinematicColors.Playhead,
            ruler = CinematicColors.RulerBackground,
            text = Color.White
        )
        
        val Light = TimelineColors(
            background = Color(0xFFF5F5FA),
            track = Color(0xFFE0E0E0),
            trackSelected = Color(0xFFD0D0D0),
            playhead = Color(0xFFE91E63),
            ruler = Color(0xFFEEEEEE),
            text = Color.Black
        )
    }
}
