package com.example.angrykittentictactoe

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlin.random.Random

enum class GameMode { TwoPlayer, PlayerVsAI }
enum class AIDifficulty { Easy, Medium, Hard }

data class GameStats(
    val xWins: Int = 0,
    val oWins: Int = 0,
    val draws: Int = 0
)

class GameViewModel : ViewModel() {
    var boardSize = mutableIntStateOf(3)
    var board = mutableStateOf(List(9) { "" })
    var currentPlayer = mutableStateOf("X")
    var winner = mutableStateOf<String?>(null)
    var gameMode = mutableStateOf(GameMode.TwoPlayer)
    var aiDifficulty = mutableStateOf(AIDifficulty.Medium)
    var stats = mutableStateOf(GameStats())
    private val moveHistory = mutableListOf<List<String>>()

    fun setBoardSize(size: Int) {
        boardSize.intValue = size
        board.value = List(size * size) { "" }
        winner.value = null
        currentPlayer.value = "X"
        moveHistory.clear()
    }

    fun setGameMode(mode: GameMode) {
        gameMode.value = mode
        restartGame()
    }

    fun setAIDifficulty(difficulty: AIDifficulty) {
        aiDifficulty.value = difficulty
    }

    fun makeMove(index: Int) {
        if (board.value[index].isEmpty() && winner.value == null) {
            val newBoard = board.value.toMutableList().apply { this[index] = currentPlayer.value }
            board.value = newBoard
            moveHistory.add(newBoard)
            winner.value = checkWinner()
            if (winner.value != null) {
                updateStats()
            } else if (board.value.all { it.isNotEmpty() }) {
                winner.value = "Draw"
                stats.value = stats.value.copy(draws = stats.value.draws + 1)
            } else {
                currentPlayer.value = if (currentPlayer.value == "X") "O" else "X"
            }
        }
    }

    fun makeAIMove() {
        if (winner.value == null && currentPlayer.value == "O") {
            val move = when (aiDifficulty.value) {
                AIDifficulty.Easy -> randomMove()
                AIDifficulty.Medium -> mediumMove()
                AIDifficulty.Hard -> minimaxMove()
            }
            move?.let { makeMove(it) }
        }
    }

    fun undoMove() {
        if (moveHistory.size > 1) {
            moveHistory.removeLast()
            board.value = moveHistory.last()
            currentPlayer.value = if (currentPlayer.value == "X") "O" else "X"
            winner.value = null
        }
    }

    fun restartGame() {
        board.value = List(boardSize.intValue * boardSize.intValue) { "" }
        currentPlayer.value = "X"
        winner.value = null
        moveHistory.clear()
        moveHistory.add(board.value)
    }

    fun resetScores() {
        stats.value = GameStats()
    }

    private fun checkWinner(): String? {
        val size = boardSize.intValue
        val winningLines = mutableListOf<List<Int>>()

        // Rows
        for (row in 0 until size) {
            winningLines.add((row * size until (row + 1) * size).toList())
        }
        // Columns
        for (col in 0 until size) {
            winningLines.add((col until size * size step size).toList())
        }
        // Diagonals
        winningLines.add((0 until size * size step size + 1).toList())
        if (size > 1) {
            winningLines.add((size - 1 until size * size - 1 step size - 1).toList())
        }

        for (line in winningLines) {
            val symbols = line.map { board.value.getOrNull(it) ?: "" }
            if (symbols.isNotEmpty() && symbols.all { it == symbols[0] && it.isNotEmpty() }) {
                return symbols[0]
            }
        }
        return null
    }

    private fun updateStats() {
        when (winner.value) {
            "X" -> stats.value = stats.value.copy(xWins = stats.value.xWins + 1)
            "O" -> stats.value = stats.value.copy(oWins = stats.value.oWins + 1)
        }
    }

    private fun randomMove(): Int? {
        val emptyCells = board.value.indices.filter { board.value[it].isEmpty() }
        return emptyCells.ifEmpty { null }?.let { emptyCells[Random.nextInt(emptyCells.size)] }
    }

    private fun mediumMove(): Int? {
        val size = boardSize.intValue
        for (i in board.value.indices) {
            if (board.value[i].isEmpty()) {
                val testBoard = board.value.toMutableList().apply { this[i] = "O" }
                if (checkWinner(testBoard) == "O") return i
                testBoard[i] = "X"
                if (checkWinner(testBoard) == "X") return i
            }
        }
        return randomMove()
    }

    private fun minimaxMove(): Int? {
        var bestScore = Int.MIN_VALUE
        var bestMove: Int? = null
        for (i in board.value.indices) {
            if (board.value[i].isEmpty()) {
                val testBoard = board.value.toMutableList().apply { this[i] = "O" }
                val score = minimax(testBoard, 0, false)
                if (score > bestScore) {
                    bestScore = score
                    bestMove = i
                }
            }
        }
        return bestMove
    }

    private fun minimax(board: List<String>, depth: Int, isMaximizing: Boolean): Int {
        val result = checkWinner(board)
        if (result != null) {
            return if (result == "O") 10 - depth else if (result == "X") depth - 10 else 0
        }
        if (board.all { it.isNotEmpty() }) return 0

        return if (isMaximizing) {
            var bestScore = Int.MIN_VALUE
            for (i in board.indices) {
                if (board[i].isEmpty()) {
                    val testBoard = board.toMutableList().apply { this[i] = "O" }
                    val score = minimax(testBoard, depth + 1, false)
                    bestScore = maxOf(bestScore, score)
                }
            }
            bestScore
        } else {
            var bestScore = Int.MAX_VALUE
            for (i in board.indices) {
                if (board[i].isEmpty()) {
                    val testBoard = board.toMutableList().apply { this[i] = "X" }
                    val score = minimax(testBoard, depth + 1, true)
                    bestScore = minOf(bestScore, score)
                }
            }
            bestScore
        }
    }

    private fun checkWinner(board: List<String>): String? {
        val size = boardSize.intValue
        val winningLines = mutableListOf<List<Int>>()

        // Rows
        for (row in 0 until size) {
            winningLines.add((row * size until (row + 1) * size).toList())
        }
        // Columns
        for (col in 0 until size) {
            winningLines.add((col until size * size step size).toList())
        }
        // Diagonals
        winningLines.add((0 until size * size step size + 1).toList())
        if (size > 1) {
            winningLines.add((size - 1 until size * size - 1 step size - 1).toList())
        }

        for (line in winningLines) {
            val symbols = line.map { board.getOrNull(it) ?: "" }
            if (symbols.isNotEmpty() && symbols.all { it == symbols[0] && it.isNotEmpty() }) {
                return symbols[0]
            }
        }
        return null
    }
}