package com.example.walkdog.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.walkdog.componentes.LogotipoComponent

@Preview(showBackground = true)
@Composable
fun LoginPage(navController: NavHostController = rememberNavController()) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // LOGO
        LogotipoComponent()

        // EMAIL
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        // SENHA
        TextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth()
        )

        // BOTÃO ENTRAR CLIENTE
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar como Cliente")
        }

        // BOTÃO REGISTRAR
        OutlinedButton(
            onClick = { println("Registrar clicado!") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // TEXTO
        Text(
            text = "Entrar como Fornecedor",
            color = Color.Black
        )

        // BOTÃO FORNECEDOR
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar como Fornecedor")
        }
    }
}

