package com.example.walkdog.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.MenuAnchorType
import com.example.walkdog.viewmodel.PasseiosEstadoViewModel
import io.appwrite.models.Document
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasseiosEstadoScreen(
    onBackClick: () -> Unit
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
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // --------------------------------------------------
            // DROPDOWN ESTADO PEDIDOS
            // --------------------------------------------------
            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = state.estadoSelecionado.replaceFirstChar { it.uppercase() },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado Pedidos") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listOf("todos","aceite", "andamento", "concluido").forEach { estado ->
                        DropdownMenuItem(
                            text = {
                                Text(estado.replaceFirstChar { it.uppercase() })
                            },
                            onClick = {
                                viewModel.alterarEstadoFiltro(estado)
                                expanded = false
                            }
                        )
                    }
                }
            }

            if (state.loading) {
                CircularProgressIndicator()
            }

            state.error?.let {
                Text(it, color = Color.Red)
            }

            // --------------------------------------------------
            // LISTA DE PASSEIOS
            // --------------------------------------------------
            state.passeios.forEach { doc ->

                val userId = state.userId
                val fornecedorId = doc.data["fornecedorId"]?.toString()
                val podeEditar = state.userId != null && fornecedorId == state.userId

                PasseioEstadoCard(
                    doc = doc,
                    podeEditar = fornecedorId == userId,
                    onEstadoChange = { novoEstado ->
                        viewModel.atualizarEstadoPasseio(doc.id, novoEstado)
                    }
                )
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasseioEstadoCard(
    doc: Document<Map<String, Any>>,
    podeEditar: Boolean,
    onEstadoChange: (String) -> Unit
) {
    val descricao = doc.data["descricao"]?.toString() ?: "Passeio"
    val cao = doc.data["Cao"]?.toString() ?: "-"
    val localidade = doc.data["Localidade"]?.toString() ?: "-"
    val horaRaw = doc.data["HoraInicio"]?.toString() ?: "-"
    val preco = doc.data["preco"]?.toString() ?: "-"
    val estado = doc.data["estado"]?.toString() ?: "pendente"
    val fotoCao = doc.data["fotoCao"]?.toString()

    val hora = formatHora(horaRaw)

    val estadoColor = when (estado) {
        "aceite" -> Color(0xFF2E7D32)
        "andamento" -> Color(0xFF0277BD)
        "concluido" -> Color(0xFF616161)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // --------------------------------------------------
            // CABEÇALHO (FOTO + TÍTULO)
            // --------------------------------------------------
            Row(verticalAlignment = Alignment.CenterVertically) {

                // 🐕 FOTO DO CÃO
                if (!fotoCao.isNullOrBlank()) {
                    AsyncImage(
                        model = fotoCao,
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🐕", fontSize = 24.sp)
                    }
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text = descricao,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = cao,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(Modifier.height(14.dp))
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))

            // --------------------------------------------------
            // INFORMAÇÕES (LOCALIDADE NÃO CORTA)
            // --------------------------------------------------
            InfoRow(
                icon = "📍",
                label = "Localidade",
                value = localidade,
                maxLines = 2
            )

            InfoRow("⏰", "Hora", hora)
            InfoRow("💶", "Preço", "€ $preco")

            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(12.dp))

            // --------------------------------------------------
            // ESTADO
            // --------------------------------------------------
            if (podeEditar) {

                var expanded by remember { mutableStateOf(false) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = estado.replaceFirstChar { it.uppercase() },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Estado do Passeio") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = estadoColor,
                            focusedLabelColor = estadoColor
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf("aceite", "andamento", "concluido").forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(it.replaceFirstChar { c -> c.uppercase() })
                                },
                                onClick = {
                                    onEstadoChange(it)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

            } else {
                Surface(
                    color = estadoColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = estado.replaceFirstChar { it.uppercase() },
                        color = estadoColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                    )
                }
            }
        }
    }
}
@Composable
fun InfoRow(
    icon: String,
    label: String,
    value: String,
    maxLines: Int = Int.MAX_VALUE
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = icon,
            fontSize = 18.sp,
            modifier = Modifier.padding(top = 2.dp)
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = "$label:",
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(95.dp)
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = value,
            color = Color.DarkGray,
            maxLines = maxLines,
            modifier = Modifier.weight(1f)
        )
    }
}
fun formatHora(hora: String): String {
    return try {
        if (hora.length >= 5) hora.substring(0, 5) else hora
    } catch (e: Exception) {
        hora
    }
}
