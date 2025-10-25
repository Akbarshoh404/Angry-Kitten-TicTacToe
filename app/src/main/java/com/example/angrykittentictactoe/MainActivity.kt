package com.example.angrykittentictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.angrykittentictactoe.ui.theme.AngryKittenTicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeApp()
        }
    }
}

@Composable
fun TicTacToeApp(viewModel: GameViewModel = viewModel()) {
    var isDarkTheme by rememberSaveable { mutableStateOf(false) }
    val navController = rememberNavController()

    AngryKittenTicTacToeTheme(darkTheme = isDarkTheme) {
        NavigationHost(navController, viewModel)
    }
}
