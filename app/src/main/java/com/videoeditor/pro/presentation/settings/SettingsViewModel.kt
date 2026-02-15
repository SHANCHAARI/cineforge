package com.videoeditor.pro.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.videoeditor.pro.data.preferences.UserPreferences
import com.videoeditor.pro.presentation.animation.AnimationLevel
import com.videoeditor.pro.presentation.animation.MotionConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Settings ViewModel managing app settings and animation preferences
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadPreferences()
    }
    
    private fun loadPreferences() {
        viewModelScope.launch {
            combine(
                userPreferences.animationLevelFlow,
                userPreferences.darkThemeFlow,
                userPreferences.autoSaveFlow,
                userPreferences.showTipsFlow,
                userPreferences.exportQualityFlow,
                userPreferences.exportFormatFlow
            ) { params: Array<Any> ->
                val animationLevel = params[0] as AnimationLevel
                val darkTheme = params[1] as Boolean
                val autoSave = params[2] as Boolean
                val showTips = params[3] as Boolean
                val exportQuality = params[4] as String
                val exportFormat = params[5] as String

                SettingsUiState(
                    animationLevel = animationLevel,
                    isDarkTheme = darkTheme,
                    autoSave = autoSave,
                    showTips = showTips,
                    exportQuality = exportQuality,
                    exportFormat = exportFormat,
                    motionConfig = MotionConfig.forLevel(animationLevel)
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }
    
    fun setAnimationLevel(level: AnimationLevel) {
        viewModelScope.launch {
            userPreferences.setAnimationLevel(level)
        }
    }
    
    fun setDarkTheme(isDark: Boolean) {
        viewModelScope.launch {
            userPreferences.setDarkTheme(isDark)
        }
    }
    
    fun setAutoSave(enabled: Boolean) {
        viewModelScope.launch {
            userPreferences.setAutoSave(enabled)
        }
    }
    
    fun setShowTips(show: Boolean) {
        viewModelScope.launch {
            userPreferences.setShowTips(show)
        }
    }
    
    fun setExportQuality(quality: String) {
        viewModelScope.launch {
            userPreferences.setExportQuality(quality)
        }
    }
    
    fun setExportFormat(format: String) {
        viewModelScope.launch {
            userPreferences.setExportFormat(format)
        }
    }
}

/**
 * UI state for settings screen
 */
data class SettingsUiState(
    val animationLevel: AnimationLevel = AnimationLevel.MEDIUM,
    val isDarkTheme: Boolean = true,
    val autoSave: Boolean = true,
    val showTips: Boolean = true,
    val exportQuality: String = "1080p",
    val exportFormat: String = "MP4",
    val motionConfig: MotionConfig = MotionConfig.forLevel(AnimationLevel.MEDIUM)
)
