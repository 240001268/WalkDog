package com.example.walkdog.Screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

// IMPORT CORRETO DO R
import com.example.walkdog.R

@Composable
fun SplashScreen(onTimeout: () -> Unit) {

    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
        delay(5000)
        onTimeout()
    }

    // 💜 COR SUAVE DO NOVO SPLASH
    val backgroundColor = Color(0xFFEDE3FF) // Roxo pastel suave

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {

        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(animationSpec = tween(1000)),
            exit = fadeOut()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // TÍTULO DO APP
                Text(
                    text = "WalkDog",
                    fontSize = 40.sp,
                    color = Color.Blue,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(80.dp))

                // 🐶 IMAGEM DO CÃO COM LEVE ANIMAÇÃO
                Image(
                    painter = painterResource(id = R.drawable.dog),
                    contentDescription = null,
                    modifier = Modifier
                        .size(180.dp)
                        .scale(1.05f)
                        .alpha(0.95f)
                )

                Spacer(modifier = Modifier.height(90.dp))

                // 🌟 MENSAGEM DE BOAS-VINDAS (abaixo da imagem)
                Text(
                    text = "Bem-vindo ao Passeio do Cão!",
                    fontSize = 22.sp,
                    color = Color(0xFF6A1B9A),
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(40.dp))

                CircularProgressIndicator(
                    color = Color(0xFF6A1B9A),
                    strokeWidth = 4.dp
                )
            }
        }
    }
}

/* ---------------------------------------------------------
   PREVIEW DO SPLASH SCREEN
   --------------------------------------------------------- */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSplashScreen() {
    SplashScreen(onTimeout = {})
}

