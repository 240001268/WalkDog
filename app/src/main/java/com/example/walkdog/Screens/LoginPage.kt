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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.walkdog.componentes.LogotipoComponent
import com.example.walkdog.service.AppwriteService
import com.example.walkdog.viewmodel.LoginViewModel
import com.example.walkdog.viewmodel.LoginViewModelFactory

@Composable
fun LoginPage(
    onEntrarCliente: () -> Unit,
    onEntrarFornecedor: () -> Unit,
    onRegistarCliente: () -> Unit,
    onRegistarFornecedor: () -> Unit
) {
    // ✅ agora seguro porque MainActivity já inicializou AppwriteService
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(AppwriteService)
    )

    val state by viewModel.state.collectAsState()

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    LaunchedEffect(state.success) {
        if (state.success) {
            if (email.contains("fornecedor")) onEntrarFornecedor()
            else onEntrarCliente()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        LogotipoComponent()
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = senha,
            onValueChange = { senha = it },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth()
        )
        if (state.error != null) {
            Text(text = state.error ?: "", color = Color.Red, modifier = Modifier.padding(4.dp))
        }
        if (state.loading) {
            CircularProgressIndicator()
        }
        Button(
            onClick = { viewModel.login(email, senha) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.loading
        ) { Text("Entrar como Cliente") }

        Button(
            onClick = { viewModel.login(email, senha) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.loading
        ) { Text("Entrar como Fornecedor") }

        OutlinedButton(
            onClick = onRegistarCliente,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.loading
        ) { Text("Registar Cliente") }

        OutlinedButton(
            onClick = onRegistarFornecedor,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.loading
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
