package com.videoeditor.pro.presentation.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object Editor : Screen("editor")
    object Settings : Screen("settings")
    object Export : Screen("export")
    object Projects : Screen("projects")
}
