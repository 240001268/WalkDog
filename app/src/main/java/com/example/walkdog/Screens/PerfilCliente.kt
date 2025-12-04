package com.example.walkdog.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.walkdog.model.Cao
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// ------------------------------------------------------------
// MINI MENU
// ------------------------------------------------------------
@Composable
fun MiniMenuCliente(
    onRegistarCao: () -> Unit,
    onBuscarFornecedor: () -> Unit,
    onMarcarPasseio: () -> Unit,
    onHistoricoClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MenuButton("Registar Cão", onClick = onRegistarCao)
        MenuButton("Buscar Fornecedor", onClick = onBuscarFornecedor)
        MenuButton("Marcar Passeio", onClick = onMarcarPasseio)
        MenuButton("Histórico de Passeios", onClick = onHistoricoClick)
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
// CARTÃO PERFIL CLIENTE
// ------------------------------------------------------------
@Composable
fun PerfilCardCliente(nome: String, descricao: String, avatarColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(avatarColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    nome.first().toString(),
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
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
// CARTÃO DO CÃO → CLICÁVEL PARA PERFIL
// ------------------------------------------------------------
@Composable
fun CaoCard(
    cao: Cao,
    onCaoClick: (Cao) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCaoClick(cao) },
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
                    .background(Color(0xFF8E67FF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    cao.nome.first().uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(cao.nome, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(cao.raca, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

// ------------------------------------------------------------
// PERFIL CLIENTE — PRINCIPAL
// ------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilClienteScreen(
    onRegistarCao: () -> Unit,
    onBuscarFornecedor: () -> Unit,
    onMarcarPasseio: () -> Unit,
    onCaoClick: (String) -> Unit,     // recebe a rota pronta para navegar
    onHistoricoClick: () -> Unit,
    onBackClick: () -> Unit
) {
    // MOCK dos cães do cliente
    val caes = listOf(
        Cao(
            nome = "Rex",
            raca = "Pastor Alemão",
            porte = "Grande",
            peso = "32",
            localidade = "Lisboa",
            nomeDono = "João Silva",
            emailDono = "joao@gmail.com",
            telefoneDono = "912345678",
            localidadeDono = "Lisboa"
        ),
        Cao(
            nome = "Bolt",
            raca = "Labrador",
            porte = "Médio",
            peso = "25",
            localidade = "Lisboa",
            nomeDono = "João Silva",
            emailDono = "joao@gmail.com",
            telefoneDono = "912345678",
            localidadeDono = "Lisboa"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil do Cliente", color = Color.White) },
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {

            // Perfil do cliente
            PerfilCardCliente(
                nome = "João Silva",
                descricao = "Cliente desde 2024 | Lisboa",
                avatarColor = Color(0xFF8E67FF)
            )

            // Mini menu + histórico
            MiniMenuCliente(
                onRegistarCao = onRegistarCao,
                onBuscarFornecedor = onBuscarFornecedor,
                onMarcarPasseio = onMarcarPasseio,
                onHistoricoClick = onHistoricoClick
            )

            // Secção de cães
            Text("Cães Registados", fontWeight = FontWeight.Bold, fontSize = 20.sp)

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                caes.forEach { cao ->
                    CaoCard(
                        cao = cao,
                        onCaoClick = { selectedCao ->
                            val route = buildPerfilCaoRoute(selectedCao)
                            onCaoClick(route) // envia a rota completa
                        }
                    )
                }
            }
        }
    }
}

// ------------------------------------------------------------
// HELPER – GERA ROTA COMPLETA COM TODOS OS CAMPOS
// ------------------------------------------------------------
fun buildPerfilCaoRoute(cao: Cao): String {

    fun enc(s: String) = URLEncoder.encode(s, StandardCharsets.UTF_8.toString())

    return "perfil_cao/" +
            "${enc(cao.nome)}/" +
            "${enc(cao.raca)}/" +
            "${enc(cao.porte)}/" +
            "${enc(cao.peso)}/" +
            "${enc(cao.localidade)}/" +
            "${enc(cao.fotoUrl ?: "")}/" +
            "${enc(cao.nomeDono)}/" +
            "${enc(cao.emailDono)}/" +
            "${enc(cao.telefoneDono)}/" +
            "${enc(cao.localidadeDono)}"
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPerfilClienteScreen() {
    PerfilClienteScreen(
        onRegistarCao = {},
        onBuscarFornecedor = {},
        onMarcarPasseio = {},
        onCaoClick = {},
        onHistoricoClick = {},
        onBackClick = {}
    )
}
