package com.example.walkdog.screens

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
import com.example.walkdog.viewmodel.LoginViewModel
import com.example.walkdog.viewmodel.LoginViewModelFactory
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

@Composable
fun LoginPage(
    onEntrarCliente: (String) -> Unit,
    onEntrarFornecedor: (String) -> Unit,
    onRegistarCliente: () -> Unit,
    onRegistarFornecedor: () -> Unit,
) {
    val viewModel: LoginViewModel = viewModel(
        factory = LoginViewModelFactory(AppwriteService)
    )

    val state by viewModel.state.collectAsState()

    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    var senhaVisivel by remember { mutableStateOf(false) }

    LaunchedEffect(state.success) {
        state.userId?.let { userId ->
            if (state.success) {
                when (state.route) {
                    "cliente" -> onEntrarCliente(userId)
                    "fornecedor" -> onEntrarFornecedor(userId)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(60.dp))

        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            LogotipoComponent(
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(30.dp))

        Text(
            "WalkDog",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6A1B9A)
        )

        Spacer(Modifier.height(12.dp))

        Text(
            "A melhor app para o seu patudo",
            color = Color.Gray,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(40.dp))

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
            singleLine = true,

            visualTransformation = if (senhaVisivel)
                VisualTransformation.None
            else
                PasswordVisualTransformation(),

            trailingIcon = {
                val icon = if (senhaVisivel)
                    Icons.Default.VisibilityOff
                else
                    Icons.Default.Visibility

                IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                    Icon(
                        imageVector = icon,
                        contentDescription = if (senhaVisivel)
                            "Ocultar senha"
                        else
                            "Mostrar senha"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        state.error?.let {
            Text(it, color = Color.Red, fontSize = 14.sp)
        }

        if (state.loading) {
            CircularProgressIndicator()
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (email.isNotBlank() && senha.isNotBlank()) {
                    viewModel.login(email, senha)
                } else {
                    viewModel.setError("Preencha todos os campos.")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A))
        ) {
            Text("Entrar como Cliente", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {
                if (email.isNotBlank() && senha.isNotBlank()) {
                    viewModel.login(email, senha, "fornecedor")
                } else {
                    viewModel.setError("Preencha todos os campos.")
                }
            },
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
