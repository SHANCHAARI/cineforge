package com.videoeditor.pro.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Editor : Screen("editor")
}
