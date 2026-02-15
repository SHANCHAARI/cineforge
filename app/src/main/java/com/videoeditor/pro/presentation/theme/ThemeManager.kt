package com.videoeditor.pro.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Dark Theme - Professional Video Editor
private val DarkColorScheme = darkColorScheme(
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
    
    background = Color(0xFF0A0A0A),
    onBackground = Color(0xFFE0E0E0),
    surface = Color(0xFF121212),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF1E1E1E),
    onSurfaceVariant = Color(0xFFB0B0B0),
    
    outline = Color(0xFF404040),
    outlineVariant = Color(0xFF2A2A2A),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFFE0E0E0),
    inverseOnSurface = Color(0xFF1A1A1A),
    inversePrimary = Color(0xFF006685)
)

// Light Theme - Professional Clean Look
private val LightColorScheme = lightColorScheme(
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
    
    background = Color(0xFFFAFAFA),
    onBackground = Color(0xFF1C1C1C),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1C1C1C),
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF49454F),
    
    outline = Color(0xFF79747E),
    outlineVariant = Color(0xFFCAC4D0),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFF313033),
    inverseOnSurface = Color(0xFFF4EFF4),
    inversePrimary = Color(0xFF90CAF9)
)

// Custom Colors for Video Editor
object VideoEditorColors {
    // Timeline Colors
    val TimelineBackground = Color(0xFF1A1A1A)
    val TimelineTrack = Color(0xFF2D2D2D)
    val TimelineTrackSelected = Color(0xFF3D3D3D)
    val Playhead = Color(0xFFFF4081)
    val RulerBackground = Color(0xFF0D0D0D)
    
    // Clip Colors
    val VideoClip = Color(0xFF4CAF50)
    val AudioClip = Color(0xFF2196F3)
    val TextClip = Color(0xFFFF9800)
    val ImageClip = Color(0xFF9C27B0)
    val EffectClip = Color(0xFF00BCD4)
    
    // UI Elements
    val ToolbarBackground = Color(0xFF0F0F0F)
    val PanelBackground = Color(0xFF1A1A1A)
    val Accent = Color(0xFF00D4FF)
    val Warning = Color(0xFFFF9800)
    val Success = Color(0xFF4CAF50)
    
    // Export Colors
    val ExportProgress = Color(0xFF00D4FF)
    val ExportBackground = Color(0xFF0A0A0A)
}

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

@Composable
fun VideoEditorProTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit
) {
    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Extended theme functions for video editor
@Composable
fun isDarkTheme(): Boolean {
    return MaterialTheme.colorScheme.background == Color(0xFF0A0A0A)
}

@Composable
fun getTimelineColors(): TimelineColors {
    return if (isDarkTheme()) {
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
            background = VideoEditorColors.TimelineBackground,
            track = VideoEditorColors.TimelineTrack,
            trackSelected = VideoEditorColors.TimelineTrackSelected,
            playhead = VideoEditorColors.Playhead,
            ruler = VideoEditorColors.RulerBackground,
            text = Color.White
        )
        
        val Light = TimelineColors(
            background = Color(0xFFF5F5F5),
            track = Color(0xFFE0E0E0),
            trackSelected = Color(0xFFD0D0D0),
            playhead = Color(0xFFE91E63),
            ruler = Color(0xFFEEEEEE),
            text = Color.Black
        )
    }
}
