package com.example.walkdog.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.walkdog.viewmodel.PasseiosEstadoViewModel
import com.example.walkdog.viewmodel.PasseioEstadoUi
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasseiosEstadoScreen(
    onBackClick: () -> Unit,
    onAvaliarFornecedor: (String, String) -> Unit
) {
    val viewModel: PasseiosEstadoViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPasseiosUser()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estado dos Pedidos", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF6A1B9A))
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (state.loading) CircularProgressIndicator()
            state.error?.let { Text(it, color = Color.Red) }

            state.passeios.forEach { passeio ->

                val podeEditarEstado =
                    state.isFornecedor && passeio.fornecedorId == state.userId

                val podeAvaliar =
                    !state.isFornecedor &&
                            passeio.estado == "concluido" &&
                            !passeio.avaliado

                PasseioEstadoCard(
                    passeio = passeio,
                    podeEditarEstado = podeEditarEstado,
                    podeAvaliar = podeAvaliar,
                    onEstadoChange = { viewModel.atualizarEstadoPasseio(passeio.id, it) },
                    onAvaliar = {
                        onAvaliarFornecedor(passeio.id, passeio.fornecedorId)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasseioEstadoCard(
    passeio: PasseioEstadoUi,
    podeEditarEstado: Boolean,
    podeAvaliar: Boolean,
    onEstadoChange: (String) -> Unit,
    onAvaliar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {

                if (passeio.fotoCaoUrl != null) {
                    AsyncImage(
                        model = passeio.fotoCaoUrl,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        Modifier.size(56.dp).clip(CircleShape).background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🐕", fontSize = 22.sp)
                    }
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(passeio.descricao, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(passeio.caoNome, fontSize = 14.sp, color = Color.Gray)
                }
            }

            Spacer(Modifier.height(12.dp))

            InfoRow("📍", "Localidade", passeio.localidade)
            InfoRow("⏰", "Hora", passeio.hora)
            InfoRow("💶", "Preço", String.format(Locale.getDefault(), "€ %s", passeio.preco))
            InfoRow(
                "👤",
                "Fornecedor",
                "${passeio.fornecedorNome}  ⭐ ${String.format(Locale.getDefault(), "%.1f", passeio.fornecedorRating)}"
            )

            Spacer(Modifier.height(12.dp))

            if (podeEditarEstado) {
                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(expanded, { expanded = !expanded }) {
                    OutlinedTextField(
                        value = passeio.estado.replaceFirstChar { it.uppercase() },
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Estado") }
                    )

                    ExposedDropdownMenu(expanded, { expanded = false }) {
                        listOf("aceite", "andamento", "concluido").forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    onEstadoChange(it)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            if (podeAvaliar) {
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onAvaliar,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A))
                ) {
                    Text("Avaliar Fornecedor", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: String, label: String, value: String) {
    Row(Modifier.fillMaxWidth()) {
        Text(icon)
        Spacer(Modifier.width(8.dp))
        Text("$label:", modifier = Modifier.width(110.dp), fontWeight = FontWeight.Medium)
        Text(value, modifier = Modifier.weight(1f))
    }
}
