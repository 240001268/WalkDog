package com.example.walkdog.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.walkdog.viewmodel.AvaliarFornecedorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvaliarFornecedorScreen(
    passeioId: String,
    fornecedorId: String,
    onSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: AvaliarFornecedorViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.success) {
        if (state.success) onSuccess()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Avaliar Fornecedor", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.Star, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6A1B9A)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text(
                "Como foi o passeio?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            // ⭐ ESTRELAS
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (i in 1..5) {
                    IconButton(onClick = { viewModel.setRating(i) }) {
                        Icon(
                            imageVector =
                                if (state.rating >= i) Icons.Filled.Star
                                else Icons.Outlined.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }

            OutlinedTextField(
                value = state.comentario,
                onValueChange = { viewModel.setComentario(it) },
                label = { Text("Comentário (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 3
            )

            state.error?.let {
                Text(it, color = Color.Red)
            }

            Button(
                onClick = {
                    viewModel.enviarAvaliacao(
                        passeioId = passeioId,
                        fornecedorId = fornecedorId
                    )
                },
                enabled = !state.loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A))
            ) {
                if (state.loading) {
                    CircularProgressIndicator(color = Color.White)
                } else {
                    Text("Enviar Avaliação", color = Color.White)
                }
            }
        }
    }
}
