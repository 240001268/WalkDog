package com.example.walkdog.screens

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.walkdog.viewmodel.PasseiosMarcadosFornecedorViewModel
import androidx.compose.material.icons.automirrored.filled.ArrowBack



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasseiosMarcadosFornecedorScreen(
    tipoPasseio: String,
    onBackClick: () -> Unit
) {
    val viewModel: PasseiosMarcadosFornecedorViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(tipoPasseio) {
        viewModel.getPasseiosMarcadosPorTipo(tipoPasseio)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pedidos de Passeio", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF6A1B9A))
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            if (state.loading) {
                CircularProgressIndicator()
            }

            state.error?.let {
                Text("Erro: $it", color = Color.Red)
            }

            if (state.passeios.isEmpty() && !state.loading) {
                Text("Sem pedidos pendentes")
            }

            state.passeios.forEach { passeio ->

                val data = passeio.data["data"]?.toString() ?: ""
                val hora = passeio.data["hora"]?.toString() ?: ""
                val clienteId = passeio.data["clienteId"]?.toString() ?: ""

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(3.dp)
                ) {
                    Column(Modifier.padding(16.dp)) {

                        Text("Cliente: $clienteId", fontWeight = FontWeight.Bold)
                        Text("Data: $data")
                        Text("Hora: $hora")

                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = {
                                viewModel.aceitarPasseio(passeio.id)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A))
                        ) {
                            Text("Aceitar Passeio", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
