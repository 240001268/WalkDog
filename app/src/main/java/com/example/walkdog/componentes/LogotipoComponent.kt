package com.example.walkdog.componentes

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.walkdog.R

@Composable
fun LogotipoComponent() {
    Image(
        painter = painterResource(id = R.drawable.captura_de_tela_2025_11_05_213258),
        contentDescription = "Logo",
        modifier = Modifier.Companion


    )

    // TÍTULO
    Text(
        text = "WalkDog",
        color = Color.Companion.Black,
        style = MaterialTheme.typography.headlineSmall
    )
}