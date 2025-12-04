package com.example.walkdog.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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


data class Fornecedor(
    val nome: String,
    val localidade: String,
    val rating: Float,
    val avatarColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscarFornecedoresScreen(
    onBackClick: () -> Unit = {},
    onVerPerfil: (Fornecedor) -> Unit = {}
) {

    val fornecedores = listOf(
        Fornecedor("João Silva", "Lisboa", 4.8f, Color(0xFF8E67FF)),
        Fornecedor("Maria Santos", "Porto", 4.7f, Color(0xFFFF8A65)),
        Fornecedor("Pedro Costa", "Braga", 4.9f, Color(0xFF64B5F6))
    )

    var search by remember { mutableStateOf("") }

    val filtrados = fornecedores.filter {
        it.nome.contains(search, ignoreCase = true) ||
                it.localidade.contains(search, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Buscar Fornecedores", color = Color.White, fontSize = 20.sp)
                },
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

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                placeholder = { Text("Buscar por nome ou localidade...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                "${filtrados.size} fornecedor(es) encontrado(s)",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(Modifier.height(16.dp))

            filtrados.forEach { fornecedor ->
                FornecedorCard(
                    fornecedor = fornecedor,
                    onClick = { onVerPerfil(fornecedor) }
                )
            }
        }
    }
}

@Composable
fun FornecedorCard(
    fornecedor: Fornecedor,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(fornecedor.avatarColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    fornecedor.nome.first().uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(fornecedor.nome, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("⭐ ${fornecedor.rating}", fontSize = 14.sp)
                Text(fornecedor.localidade, fontSize = 14.sp, color = Color.Gray)
            }

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ver Perfil", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBuscar() {
    BuscarFornecedoresScreen()
}
