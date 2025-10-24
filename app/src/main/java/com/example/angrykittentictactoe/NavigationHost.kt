package com.example.angrykittentictactoe

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.ui.Modifier

@Composable
fun NavigationHost(navController: NavHostController, viewModel: GameViewModel, modifier: Modifier) {
    NavHost(navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("home") { HomeScreen(navController, viewModel) }
        composable("game") { GameScreen(viewModel, navController) }
        composable("settings") { SettingsScreen(viewModel, navController) }
        composable("stats") { StatsScreen(viewModel, navController) }
    }
}