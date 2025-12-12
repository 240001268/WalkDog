package com.example.walkdog.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.walkdog.componentes.LogotipoComponent
import com.example.walkdog.service.AppwriteService
import com.example.walkdog.viewmodel.LoginUiState
import com.example.walkdog.viewmodel.LoginViewModel
import com.example.walkdog.viewmodel.LoginViewModelFactory

@Composable
fun LoginPage(
    onEntrarCliente: () -> Unit,
    onEntrarFornecedor: (state: LoginUiState) -> Unit,
    onRegistarCliente: () -> Unit,
    onRegistarFornecedor: () -> Unit,
) {
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(AppwriteService)
    )

    val state by viewModel.state.collectAsState()

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    LaunchedEffect(state.success) {
        if (state.route == "cliente") onEntrarCliente()
        if (state.route == "fornecedor") onEntrarFornecedor(state)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ================================
        // LOGOTIPO GRANDE + TÍTULO
        // ================================
        Spacer(modifier = Modifier.height(60.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(180.dp), contentAlignment = Alignment.Center) {
                    LogotipoComponent()
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    "WalkDog",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6A1B9A)
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    "O melhor site para o seu melhor amigo",
                    color = Color.Gray,
                    fontSize = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        // ================================
        // CAMPOS DE LOGIN
        // ================================
        TextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        TextField(
            value = senha,
            onValueChange = { senha = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Senha") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (state.error != null)
            Text(state.error ?: "", color = Color.Red, fontSize = 14.sp)

        if (state.loading)
            CircularProgressIndicator()

        // ================================
        // BOTÕES ABAIXO — ORGANIZADOS
        // ================================
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { viewModel.login(email, senha, "cliente") },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A))
        ) {
            Text("Entrar como Cliente", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = { viewModel.login(email, senha, "fornecedor") },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF9C27B0))
        ) {
            Text("Entrar como Fornecedor", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            onClick = onRegistarCliente,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Registar Cliente")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onRegistarFornecedor,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Registar Fornecedor")
        }

        Spacer(modifier = Modifier.height(40.dp))
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
