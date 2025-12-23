package com.example.walkdog.componentes

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.walkdog.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun LogotipoComponent(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.dog),
        contentDescription = "Logotipo WalkDog",
        modifier = modifier,
        contentScale = ContentScale.Fit
    )
}