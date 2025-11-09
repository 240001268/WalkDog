package com.example.screenprojeto


import android.R.attr.name
import android.R.attr.text
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource


@Composable
fun PerfilScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Passeio em Andamento",
            color = Color.Blue,
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 30.sp
        )
        Box (
            modifier = Modifier
                .size(300.dp)
                .clip(RectangleShape)
                .background(Color.LightGray),
        ){
            Text(text = "Aqui entra o Api do mapa!!")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
            }


            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.weight(1f).padding(start = 10.dp)
            ) {
                Text(
                    text = "name",
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = "location",
                    color = Color.DarkGray,
                    fontSize = 12.sp
                )
            }
        }


    }

}

@Preview(showBackground = true)
@Composable
fun PreviewPerfilScreen() {
    PerfilScreen()
}