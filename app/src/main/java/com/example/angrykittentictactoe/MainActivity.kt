package com.example.angrykittentictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicTacToeApp() {
    var isDarkTheme by rememberSaveable { mutableStateOf(false) }

    AngryKittenTicTacToeTheme(darkTheme = isDarkTheme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = "Tic Tac Toe") },
                    actions = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isDarkTheme) "Dark" else "Light",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Switch(
                                checked = isDarkTheme,
                                onCheckedChange = { isDarkTheme = it }
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            TicTacToeScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}

@Composable
fun TicTacToeScreen(modifier: Modifier = Modifier) {
    var board by rememberSaveable { mutableStateOf(List(9) { "" }) }
    var currentPlayer by rememberSaveable { mutableStateOf("X") }
    val winner = rememberWinner(board)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = when {
                    winner == "Draw" -> "It's a draw!"
                    winner != null -> "$winner wins!"
                    else -> "$currentPlayer's turn"
                },
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Board(
                board = board,
                enabled = winner == null,
                onCellClick = { index ->
                    if (board[index].isEmpty() && winner == null) {
                        val next = board.toMutableList()
                        next[index] = currentPlayer
                        board = next
                        currentPlayer = if (currentPlayer == "X") "O" else "X"
                    }
                }
            )
        }

        Button(onClick = {
            board = List(9) { "" }
            currentPlayer = "X"
        }) {
            Text("Reset")
        }
    }
}

@Composable
private fun Board(
    board: List<String>,
    enabled: Boolean,
    onCellClick: (Int) -> Unit
) {
    val borderColor = MaterialTheme.colorScheme.outline
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        for (row in 0 until 3) {
            Row {
                for (col in 0 until 3) {
                    val index = row * 3 + col
                    BoardCell(
                        value = board[index],
                        enabled = enabled,
                        onClick = { onCellClick(index) },
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                    )
                }
            }
        }
    }
}

@Composable
private fun BoardCell(
    value: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textColor = MaterialTheme.colorScheme.onSurface
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clickable(enabled = enabled && value.isEmpty()) { onClick() }
    ) {
        Text(
            text = value,
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun rememberWinner(board: List<String>): String? {
    // Returns "X" or "O" when someone wins, "Draw" when board is full, or null to continue.
    val winningLines = listOf(
        listOf(0, 1, 2),
        listOf(3, 4, 5),
        listOf(6, 7, 8),
        listOf(0, 3, 6),
        listOf(1, 4, 7),
        listOf(2, 5, 8),
        listOf(0, 4, 8),
        listOf(2, 4, 6)
    )
    for (line in winningLines) {
        val (a, b, c) = line
        if (board[a].isNotEmpty() && board[a] == board[b] && board[b] == board[c]) {
            return board[a]
        }
    }
    return if (board.all { it.isNotEmpty() }) "Draw" else null
}

@Preview(showBackground = true)
@Composable
fun TicTacToePreviewLight() {
    AngryKittenTicTacToeTheme(darkTheme = false) {
        TicTacToeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun TicTacToePreviewDark() {
    AngryKittenTicTacToeTheme(darkTheme = true) {
        TicTacToeScreen()
    }
}