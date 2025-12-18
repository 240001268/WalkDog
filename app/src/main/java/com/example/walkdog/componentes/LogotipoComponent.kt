package com.example.walkdog.componentes

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.walkdog.R

@Composable
fun LogotipoComponent() {
    Image(
        painter = painterResource(id = R.drawable.dog),
        contentDescription = "Logo",
        modifier = Modifier


    )

}