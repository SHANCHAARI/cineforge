package com.videoeditor.pro.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import com.videoeditor.pro.presentation.animation.AnimationLevel

/**
 * DataStore implementation for storing user preferences
 * Handles animation level and other app settings
 */
@Singleton
class UserPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    
    companion object {
        private val ANIMATION_LEVEL = stringPreferencesKey("animation_level")
        private val DARK_THEME = booleanPreferencesKey("dark_theme")
        private val AUTO_SAVE = booleanPreferencesKey("auto_save")
        private val SHOW_TIPS = booleanPreferencesKey("show_tips")
        private val EXPORT_QUALITY = stringPreferencesKey("export_quality")
        private val EXPORT_FORMAT = stringPreferencesKey("export_format")
    }
    
    /**
     * Animation level flow
     */
    val animationLevelFlow: Flow<AnimationLevel> = dataStore.data.map { preferences ->
        val levelString = preferences[ANIMATION_LEVEL] ?: AnimationLevel.MEDIUM.name
        try {
            AnimationLevel.valueOf(levelString)
        } catch (e: IllegalArgumentException) {
            AnimationLevel.MEDIUM
        }
    }
    
    /**
     * Dark theme flow
     */
    val darkThemeFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DARK_THEME] ?: true
    }
    
    /**
     * Auto save flow
     */
    val autoSaveFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[AUTO_SAVE] ?: true
    }
    
    /**
     * Show tips flow
     */
    val showTipsFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[SHOW_TIPS] ?: true
    }
    
    /**
     * Export quality flow
     */
    val exportQualityFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[EXPORT_QUALITY] ?: "1080p"
    }
    
    /**
     * Export format flow
     */
    val exportFormatFlow: Flow<String> = dataStore.data.map { preferences ->
        preferences[EXPORT_FORMAT] ?: "MP4"
    }
    
    /**
     * Save animation level
     */
    suspend fun setAnimationLevel(level: AnimationLevel) {
        dataStore.edit { preferences ->
            preferences[ANIMATION_LEVEL] = level.name
        }
    }
    
    /**
     * Save dark theme preference
     */
    suspend fun setDarkTheme(isDark: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_THEME] = isDark
        }
    }
    
    /**
     * Save auto save preference
     */
    suspend fun setAutoSave(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUTO_SAVE] = enabled
        }
    }
    
    /**
     * Save show tips preference
     */
    suspend fun setShowTips(show: Boolean) {
        dataStore.edit { preferences ->
            preferences[SHOW_TIPS] = show
        }
    }
    
    /**
     * Save export quality preference
     */
    suspend fun setExportQuality(quality: String) {
        dataStore.edit { preferences ->
            preferences[EXPORT_QUALITY] = quality
        }
    }
    
    /**
     * Save export format preference
     */
    suspend fun setExportFormat(format: String) {
        dataStore.edit { preferences ->
            preferences[EXPORT_FORMAT] = format
        }
    }
}
