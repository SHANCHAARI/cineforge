package com.videoeditor.pro.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.videoeditor.pro.presentation.theme.VideoEditorProTheme
import com.videoeditor.pro.presentation.theme.ThemeMode
import com.videoeditor.pro.presentation.navigation.Screen
import com.videoeditor.pro.presentation.editor.EditorScreen
import com.videoeditor.pro.presentation.splash.SplashScreen
import com.videoeditor.pro.presentation.settings.SettingsScreen
import com.videoeditor.pro.presentation.projects.ProjectsScreen
import com.videoeditor.pro.presentation.export.ExportScreen
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        Timber.d("MainActivity created")
        
        setContent {
            var themeMode by remember { mutableStateOf(ThemeMode.SYSTEM) }
            var showSplash by remember { mutableStateOf(true) }
            
            VideoEditorProTheme(themeMode = themeMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (showSplash) {
                        SplashScreen(
                            onLoadingComplete = {
                                showSplash = false
                            }
                        )
                    } else {
                        val navController = rememberNavController()
                        
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Home.route
                        ) {
                            composable(Screen.Home.route) {
                                MainScreen(
                                    onNewProjectClick = {
                                        navController.navigate(Screen.Editor.route)
                                    },
                                    onOpenProjectClick = {
                                        navController.navigate(Screen.Projects.route)
                                    },
                                    onSettingsClick = {
                                        navController.navigate(Screen.Settings.route)
                                    }
                                )
                            }
                            
                            composable(Screen.Editor.route) {
                                EditorScreen(
                                    onBack = { navController.popBackStack() },
                                    onExportClick = {
                                        navController.navigate(Screen.Export.route)
                                    }
                                )
                            }
                            
                            composable(Screen.Settings.route) {
                                SettingsScreen(
                                    onBack = { navController.popBackStack() },
                                    themeMode = themeMode,
                                    onThemeChanged = { newTheme ->
                                        themeMode = newTheme
                                    }
                                )
                            }
                            
                            composable(Screen.Projects.route) {
                                ProjectsScreen(
                                    onBack = { navController.popBackStack() },
                                    onProjectSelected = { projectId ->
                                        navController.navigate(Screen.Editor.route)
                                    }
                                )
                            }
                            
                            composable(Screen.Export.route) {
                                ExportScreen(
                                    onBack = { navController.popBackStack() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    onNewProjectClick: () -> Unit,
    onOpenProjectClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Logo Area
            Card(
                modifier = Modifier.size(120.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "CF",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            // App Info
            Text(
                text = "CineForge",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "Professional Video Editing Engine",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Text(
                text = "Built by Vidya Sagar",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Action Buttons
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onNewProjectClick,
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("New Project")
                }
                
                OutlinedButton(
                    onClick = onOpenProjectClick,
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Open Project")
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Settings Button
            IconButton(
                onClick = onSettingsClick
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
