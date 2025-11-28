package com.example.walkdog.Screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.automirrored.filled.ArrowBack


// ------------------------------------------------------------
// MODELO DE DADOS — AGORA FORNECEDOR
// ------------------------------------------------------------
data class Fornecedor(
    val nome: String,
    val localidade: String,
    val rating: String,
    val avatarColor: Color
)


// ------------------------------------------------------------
// TELA PRINCIPAL — Buscar Fornecedores
// ------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscarFornecedoresScreen(
    onBackClick: () -> Unit = {},
    onVerPerfil: (Fornecedor) -> Unit = {}  // ← Navegação correta
) {

    val fornecedores = listOf(
        Fornecedor("João Silva", "Lisboa", "4.8", Color(0xFF8E67FF)),
        Fornecedor("Maria Santos", "Porto", "4.7", Color(0xFFFF8A65)),
        Fornecedor("Pedro Costa", "Braga", "4.9", Color(0xFF64B5F6))
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Buscar Fornecedores",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6A1B9A))
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // 🔍 Campo de busca
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Buscar por nome ou localidade...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text("Filtros", fontWeight = FontWeight.Medium, fontSize = 16.sp)

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilterChip(text = "Zona", selected = false)
                FilterChip(text = "Avaliação", selected = false)
                FilterChip(text = "Serviços", selected = false)
            }

            Spacer(Modifier.height(16.dp))

            Text("${fornecedores.size} fornecedor(es) encontrado(s)", fontWeight = FontWeight.Light)

            Spacer(Modifier.height(16.dp))

            fornecedores.forEach { fornecedor ->
                FornecedorCard(
                    fornecedor = fornecedor,
                    onClick = { onVerPerfil(fornecedor) } // ← Navegação correta
                )
            }
        }
    }
}


// ------------------------------------------------------------
// COMPONENTES
// ------------------------------------------------------------
@Composable
fun FilterChip(text: String, selected: Boolean) {
    Surface(
        shape = RoundedCornerShape(30.dp),
        color = if (selected) Color(0xFF6A1B9A) else Color(0xFFEDE7F6),
        tonalElevation = 2.dp
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (selected) Color.White else Color.Black,
            fontSize = 14.sp
        )
    }
}

@Composable
fun FornecedorCard(
    fornecedor: Fornecedor,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Avatar circular
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(fornecedor.avatarColor)
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(fornecedor.nome, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text("⭐ ${fornecedor.rating}", fontSize = 14.sp)
                Text(fornecedor.localidade, fontSize = 14.sp, color = Color.Gray)
            }

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Ver Perfil Completo", color = Color.White, fontSize = 13.sp)
            }
        }
    }
}


// ------------------------------------------------------------
// PREVIEW APRIMORADO
// ------------------------------------------------------------
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewBuscarFornecedoresScreen() {
    BuscarFornecedoresScreen(
        onBackClick = {},
        onVerPerfil = {}
    )
}

