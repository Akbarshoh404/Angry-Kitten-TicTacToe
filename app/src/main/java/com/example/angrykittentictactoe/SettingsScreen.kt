package com.example.angrykittentictactoe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun SettingsScreen(viewModel: GameViewModel, navController: NavHostController) {
    var showBoardSizeDialog by remember { mutableStateOf(false) }
    var showAIDifficultyDialog by remember { mutableStateOf(false) }
    val boardSize by viewModel.boardSize
    val aiDifficulty by viewModel.aiDifficulty

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = true,
            enter = scaleIn(animationSpec = tween(800)) + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(0.9f),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 32.sp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Board Size: $boardSize x $boardSize",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { showBoardSizeDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Text(
                            "Change Board Size",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "AI Difficulty: $aiDifficulty",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { showAIDifficultyDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Text(
                            "Change AI Difficulty",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { viewModel.resetScores() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Text(
                            "Reset Scores",
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { navController.navigate("home") { popUpTo("home") { inclusive = true } } },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Text(
                            "Back",
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // Board Size Modal
        if (showBoardSizeDialog) {
            AlertDialog(
                onDismissRequest = { showBoardSizeDialog = false },
                confirmButton = {
                    TextButton(onClick = { showBoardSizeDialog = false }) {
                        Text(
                            "Cancel",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp
                        )
                    }
                },
                modifier = Modifier.clip(RoundedCornerShape(24.dp)),
                title = {
                    AnimatedVisibility(
                        visible = true,
                        enter = scaleIn(tween(300)) + fadeIn(tween(300)),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Select Board Size",
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = {
                                        viewModel.setBoardSize(3)
                                        showBoardSizeDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                ) {
                                    Text("3x3", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
                                }
                                Button(
                                    onClick = {
                                        viewModel.setBoardSize(5)
                                        showBoardSizeDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                ) {
                                    Text("5x5", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
                                }
                                Button(
                                    onClick = {
                                        viewModel.setBoardSize(7)
                                        showBoardSizeDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                ) {
                                    Text("7x7", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
                                }
                            }
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        // AI Difficulty Modal
        if (showAIDifficultyDialog) {
            AlertDialog(
                onDismissRequest = { showAIDifficultyDialog = false },
                confirmButton = {
                    TextButton(onClick = { showAIDifficultyDialog = false }) {
                        Text(
                            "Cancel",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 18.sp
                        )
                    }
                },
                modifier = Modifier.clip(RoundedCornerShape(24.dp)),
                title = {
                    AnimatedVisibility(
                        visible = true,
                        enter = scaleIn(tween(300)) + fadeIn(tween(300)),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Select AI Difficulty",
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 28.sp),
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = {
                                        viewModel.setAIDifficulty(AIDifficulty.Easy)
                                        showAIDifficultyDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                ) {
                                    Text("Easy", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
                                }
                                Button(
                                    onClick = {
                                        viewModel.setAIDifficulty(AIDifficulty.Medium)
                                        showAIDifficultyDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                ) {
                                    Text("Medium", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
                                }
                                Button(
                                    onClick = {
                                        viewModel.setAIDifficulty(AIDifficulty.Hard)
                                        showAIDifficultyDialog = false
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                ) {
                                    Text("Hard", color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp)
                                }
                            }
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}