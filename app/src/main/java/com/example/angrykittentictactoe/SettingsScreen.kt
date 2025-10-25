package com.example.angrykittentictactoe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun SettingsScreen(viewModel: GameViewModel, navController: NavHostController) {
    val boardSize by viewModel.boardSize
    val aiDifficulty by viewModel.aiDifficulty

    val black = Color(0xFF000000)
    val white = Color(0xFFFFFFFF)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(white),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .fillMaxHeight()
        ) {
            // Move title 20dp higher
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "SETTINGS",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = black,
                modifier = Modifier.padding(bottom = 60.dp)
            )

            // --- Board Size Button (bigger like HomeScreen)
            Button(
                onClick = {
                    val newSize = when (boardSize) {
                        3 -> 5
                        5 -> 3
                        else -> 3
                    }
                    viewModel.setBoardSize(newSize)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = black,
                    contentColor = white
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp) // same as HomeScreen
                    .clip(CircleShape)
            ) {
                Text(
                    text = "Board Size: ${boardSize}x${boardSize}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            // --- AI Difficulty Button (bigger like HomeScreen)
            Button(
                onClick = {
                    val newDifficulty = when (aiDifficulty) {
                        AIDifficulty.Easy -> AIDifficulty.Medium
                        AIDifficulty.Medium -> AIDifficulty.Hard
                        AIDifficulty.Hard -> AIDifficulty.Easy
                    }
                    viewModel.setAIDifficulty(newDifficulty)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = black,
                    contentColor = white
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .clip(CircleShape)
            ) {
                Text(
                    text = "AI Difficulty: $aiDifficulty",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // --- Smaller Back Button
            Button(
                onClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = black,
                    contentColor = white
                ),
                modifier = Modifier
                    .width(120.dp)
                    .height(50.dp)
                    .clip(CircleShape)
            ) {
                Text("Back", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
