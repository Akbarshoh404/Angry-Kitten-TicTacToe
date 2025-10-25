package com.example.angrykittentictactoe

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    var startAnimation by remember { mutableStateOf(false) }

    val alphaAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "alphaAnim"
    )

    val scaleAnim by animateFloatAsState(
        targetValue = if (startAnimation) 1.1f else 0.9f,
        animationSpec = tween(durationMillis = 1000),
        label = "scaleAnim"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(3000) // ⏱ stays for 3 seconds
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.angry_kitten_logo), // replace with your PNG
            contentDescription = null,
            modifier = Modifier
                .size(220.dp) // adjust image size if needed
                .alpha(alphaAnim)
                .scale(scaleAnim)
        )
    }
}
