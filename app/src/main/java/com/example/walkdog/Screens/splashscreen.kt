package com.example.walkdog

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavHostController) {

    var startAnimation by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = ""
    )

    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.85f,
        animationSpec = tween(durationMillis = 800),
        label = ""
    )

    LaunchedEffect(true) {
        startAnimation = true
        delay(2500) // tempo da splash
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    // Cor de fundo idêntica à imagem
    val backgroundColor: androidx.compose.ui.graphics.Color = Color(0xFFF5EFD9)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp)
                .alpha(alpha)
                .scale(scale),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Logo
            Image(
                painter = painterResource(id = R.drawable.dog_rex),
                contentDescription = "Logo",
                modifier = Modifier.size(140.dp)
            )

            Spacer(Modifier.height(12.dp))

            // Nome abaixo do logo
            Text(
                text = "TidyNest",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = Color(0xFF154A41) // verde igual ao da imagem
                )
            )

            Spacer(Modifier.height(40.dp))

            CircularProgressIndicator(
                color = Color(0xFF154A41),
                strokeWidth = 3.dp,
                modifier = Modifier.size(45.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashPreview() {
    SplashScreen(navController = rememberNavController())
}