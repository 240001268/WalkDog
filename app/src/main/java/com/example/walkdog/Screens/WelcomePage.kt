package com.example.walkdog.Screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.walkdog.R

@Preview(showBackground = true)
@Composable
fun WelcomePage(navController: NavHostController = rememberNavController()) {
    val horizontalPadding = 15.dp

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TOPO (Imagem + Nome do Cliente)
        ProfileHeader(
            name = "Nome do Cliente",
            location = "Localidade"
        )

        // TÍTULO
        Text(
            text = "Perfil",
            color = Color.Black,
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 30.sp
        )

        // SUBTÍTULO
        Text(
            text = "Cães Registados",
            color = Color.Black,
            style = MaterialTheme.typography.headlineSmall,
            fontSize = 20.sp
        )

        // LISTA DE CÃES
        ProfileHeader(
            name = "Max",
            location = "Labrador Retriever"
        )

        ProfileHeader(
            name = "Luna",
            location = "Golden Retriever"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // BOTÃO
        Button(
            onClick = { println("Fornecedor clicado!") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0E2244))
        ) {
            Text("Entrar como Fornecedor", color = Color.White)
        }
    }
}

// 🔹 Componente reutilizável para perfis / cães
@Composable
fun ProfileHeader(name: String, location: String) {
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
                text = name,
                color = Color.Black,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = location,
                color = Color.DarkGray,
                fontSize = 12.sp
            )
        }
    }
}
