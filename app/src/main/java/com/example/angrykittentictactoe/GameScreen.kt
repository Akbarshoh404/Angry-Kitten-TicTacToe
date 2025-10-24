package com.example.angrykittentictactoe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
fun GameScreen(viewModel: GameViewModel, navController: NavHostController) {
    val board by viewModel.board
    val currentPlayer by viewModel.currentPlayer
    val winner by viewModel.winner
    val gameMode by viewModel.gameMode
    var showDialog by remember { mutableStateOf(false) }
    var aiMoveTriggered by remember { mutableStateOf(false) }

    // Handle winner dialog
    LaunchedEffect(winner) {
        if (winner != null) {
            showDialog = true
        }
    }

    // Handle AI move with delay
    LaunchedEffect(aiMoveTriggered) {
        if (aiMoveTriggered) {
            delay(500L)
            viewModel.makeAIMove()
            aiMoveTriggered = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
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
                val statusText = if (winner == null) "${if (gameMode == GameMode.PlayerVsAI && currentPlayer == "X") "Your Turn" else "$currentPlayer's Turn"}" else ""
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 28.sp),
                    fontWeight = FontWeight.Bold,
                    color = if (currentPlayer == "X") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(24.dp))
                Board(
                    board = board,
                    enabled = winner == null && (gameMode == GameMode.TwoPlayer || currentPlayer == "X"),
                    boardSize = viewModel.boardSize.intValue,
                    onCellClick = { index ->
                        if (board[index].isEmpty() && winner == null) {
                            viewModel.makeMove(index)
                            if (gameMode == GameMode.PlayerVsAI && winner == null) {
                                aiMoveTriggered = true
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = { viewModel.restartGame(); showDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Text("Restart", color = MaterialTheme.colorScheme.onPrimary, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = { navController.navigate("home") { popUpTo("home") { inclusive = true } } },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier
                            .height(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Text("Home", color = MaterialTheme.colorScheme.onSecondary, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // Victory/Lose/Draw Dialog
        if (showDialog && winner != null) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Close", color = MaterialTheme.colorScheme.onSurface, fontSize = 18.sp)
                    }
                },
                modifier = Modifier.clip(RoundedCornerShape(24.dp)),
                title = {
                    val dialogText = when {
                        winner == "Draw" -> "It's a Draw!"
                        gameMode == GameMode.PlayerVsAI -> if (winner == "X") "You Win!" else "You Lose!"
                        else -> "$winner Wins!"
                    }
                    val dialogColor = when {
                        winner == "Draw" -> Color.Gray
                        winner == "X" -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.secondary
                    }
                    AnimatedVisibility(
                        visible = true,
                        enter = scaleIn(tween(300)) + fadeIn(tween(300)),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Text(
                            text = dialogText,
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 32.sp),
                            fontWeight = FontWeight.ExtraBold,
                            color = dialogColor,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
            )
        }
    }
}

@Composable
fun Board(
    board: List<String>,
    enabled: Boolean,
    boardSize: Int,
    onCellClick: (Int) -> Unit
) {
    val borderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        for (row in 0 until boardSize) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (col in 0 until boardSize) {
                    val index = row * boardSize + col
                    Card(
                        modifier = Modifier
                            .size((300 / boardSize).dp)
                            .padding(4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        BoardCell(
                            value = board[index],
                            enabled = enabled,
                            onClick = { onCellClick(index) },
                            modifier = Modifier.fillMaxSize(),
                            boardSize = boardSize
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BoardCell(
    value: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    boardSize: Int
) {
    val textColor = if (value == "X") MaterialTheme.colorScheme.primary else if (value == "O") MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clickable(enabled = enabled && value.isEmpty()) { onClick() }
    ) {
        AnimatedVisibility(
            visible = value.isNotEmpty(),
            enter = scaleIn(animationSpec = tween(200)) + fadeIn(),
            exit = scaleOut() + fadeOut()
        ) {
            Text(
                text = value,
                fontSize = (88 / boardSize).sp,
                fontWeight = FontWeight.ExtraBold,
                color = textColor
            )
        }
    }
}