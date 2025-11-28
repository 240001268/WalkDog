package com.example.walkdog.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ------------------------------------------------------------
// MINI MENU
// ------------------------------------------------------------
@Composable
fun MiniMenuCliente(
    onRegistarCao: () -> Unit,
    onBuscarFornecedor: () -> Unit,
    onOutro: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MenuButton("Registar Cão", onClick = onRegistarCao)
        MenuButton("Buscar Fornecedor", onClick = onBuscarFornecedor)
        MenuButton("Outro", onClick = onOutro)
    }
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(Color(0xFFF1E6FF)),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text,
                fontSize = 17.sp,
                color = Color(0xFF6A1B9A),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ------------------------------------------------------------
// CARTÃO SUPERIOR DO CLIENTE (NOVO)
// ------------------------------------------------------------
@Composable
fun PerfilCardCliente(nome: String, descricao: String, avatarColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar do cliente
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(avatarColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Foto do Cliente",
                    tint = Color.White,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(nome, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Text(descricao, fontSize = 15.sp, color = Color.Gray)
            }
        }
    }
}

// ------------------------------------------------------------
// PERFIL CLIENTE — PRINCIPAL (APRIMORADO)
// ------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilClienteScreen(
    onRegistarCao: () -> Unit,
    onBuscarFornecedor: () -> Unit,
    onOutro: () -> Unit,
    onBackClick: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Perfil do Cliente",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF6A1B9A)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {

            // PERFIL APRIMORADO DO CLIENTE
            PerfilCardCliente(
                nome = "João Silva",
                descricao = "Cliente desde 2024 | Lisboa",
                avatarColor = Color(0xFF8E67FF)
            )

            // MINI MENU
            MiniMenuCliente(
                onRegistarCao = onRegistarCao,
                onBuscarFornecedor = onBuscarFornecedor,
                onOutro = onOutro
            )

            // TÍTULO
            Text("Cães Registados", fontWeight = FontWeight.Bold, fontSize = 20.sp)

            // LISTA DE CÃES — ESTILO DO FORNECEDOR
            CaoCard("Rex", "Pastor Alemão", Color(0xFF8E67FF))
            CaoCard("Bolt", "Labrador", Color(0xFFFF8A65))
        }
    }
}

// ------------------------------------------------------------
// CARD DO CÃO — BASEADO NO LAYOUT DO FORNECEDOR
// ------------------------------------------------------------
@Composable
fun CaoCard(nome: String, raca: String, avatarColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(avatarColor)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(nome, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(raca, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

// ------------------------------------------------------------
// PREVIEW
// ------------------------------------------------------------
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPerfilClienteScreen() {
    PerfilClienteScreen(
        onRegistarCao = {},
        onBuscarFornecedor = {},
        onOutro = {},
        onBackClick = {}
    )
}
