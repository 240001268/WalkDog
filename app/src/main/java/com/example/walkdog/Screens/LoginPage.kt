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
import com.example.walkdog.componentes.LogotipoComponent

@Composable
fun LoginPage(
    onEntrarCliente: () -> Unit,
    onEntrarFornecedor: () -> Unit,
    onRegistarCliente: () -> Unit,
    onRegistarFornecedor: () -> Unit
) {
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
            onClick = onEntrarCliente,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Entrar como Cliente") }

        // BOTÃO ENTRAR FORNECEDOR
        Button(
            onClick = onEntrarFornecedor,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Entrar como Fornecedor") }

        // REGISTAR CLIENTE
        OutlinedButton(
            onClick = onRegistarCliente,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Registar Cliente") }

        // REGISTAR FORNECEDOR
        OutlinedButton(
            onClick = onRegistarFornecedor,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Registar Fornecedor") }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginPage() {
    LoginPage(
        onEntrarCliente = {},
        onEntrarFornecedor = {},
        onRegistarCliente = {},
        onRegistarFornecedor = {}
    )
}


