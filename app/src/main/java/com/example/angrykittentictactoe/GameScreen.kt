package com.example.angrykittentictactoe

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

    LaunchedEffect(winner) {
        if (winner != null) showDialog = true
    }

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
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            val statusText = if (winner == null)
                if (gameMode == GameMode.PlayerVsAI && currentPlayer == "X") "Your Turn"
                else "$currentPlayer's Turn"
            else ""

            // 🆙 Bigger and slightly higher text
            Text(
                text = statusText,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(40.dp)) // moved slightly higher

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

            Spacer(modifier = Modifier.height(50.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { viewModel.restartGame(); showDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .height(56.dp)
                        .width(140.dp)
                ) {
                    Text(
                        "Restart",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier
                        .height(56.dp)
                        .width(140.dp)
                ) {
                    Text(
                        "Home",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }//hello

        if (showDialog && winner != null) {
            AlertDialog(
                onDismissRequest = {  },
                confirmButton = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = {
                                showDialog = false
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier
                                .width(160.dp)
                                .height(50.dp)
                        ) {
                            Text(
                                "Home",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                },
                title = {
                    val dialogText = when {
                        winner == "Draw" -> "It's a Draw!"
                        gameMode == GameMode.PlayerVsAI -> if (winner == "X") "You Win!" else "You Lose!"
                        else -> "$winner Wins!"
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AnimatedVisibility(
                            visible = true,
                            enter = scaleIn(tween(300)) + fadeIn(tween(300)),
                            exit = scaleOut() + fadeOut()
                        ) {
                            Text(
                                text = dialogText,
                                fontSize = 36.sp, // bigger text
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp), // makes the dialog larger
                containerColor = Color.White
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
    val lineColor = Color.Black
    val lineThickness = 1.dp
    val boardDimension = 360.dp

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        for (row in 0 until boardSize) {
            Row(horizontalArrangement = Arrangement.Center) {
                for (col in 0 until boardSize) {
                    val index = row * boardSize + col
                    val topBorder = if (row > 0) lineThickness else 0.dp
                    val startBorder = if (col > 0) lineThickness else 0.dp

                    Box(
                        modifier = Modifier
                            .size(boardDimension / boardSize)
                            .padding(start = startBorder, top = topBorder)
                            .clickable(enabled = enabled && board[index].isEmpty()) {
                                onCellClick(index)
                            }
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        if (col < boardSize - 1) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterEnd)
                                    .width(lineThickness)
                                    .fillMaxHeight()
                                    .background(lineColor)
                            )
                        }
                        if (row < boardSize - 1) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .height(lineThickness)
                                    .fillMaxWidth()
                                    .background(lineColor)
                            )
                        }

                        this@Row.AnimatedVisibility(
                            visible = board[index].isNotEmpty(),
                            enter = scaleIn(tween(200)) + fadeIn(),
                            exit = scaleOut() + fadeOut()
                        ) {
                            Text(
                                text = board[index],
                                fontSize = (110 / boardSize).sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
