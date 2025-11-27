package com.example.walkdog.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.walkdog.R



// DATA MODEL DO SERVIÇO OU CATEGORIA ATENDIDA

data class ServicoFornecedor(
    val titulo: String,
    val descricao: String,
    val avatarColor: Color
)

// TELA PRINCIPAL - PERFIL DO FORNECEDOR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilFornecedorScreen(
    onBackClick: () -> Unit = {},
    onContactClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    onSaveClick: () -> Unit
) {

    // Lista dinâmica de serviços oferecidos
    var servicos by remember {
        mutableStateOf(
            listOf(
                ServicoFornecedor("Passeio Simples", "30 minutos de passeio", Color(0xFFFF8A65)),
                ServicoFornecedor("Passeio Premium", "1 hora de passeio + exercícios", Color(0xFF64B5F6)),
                ServicoFornecedor("Adestramento Básico", "Sessão de 45 minutos", Color(0xFF8E67FF))
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil do Fornecedor", color = Color.White) },
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // PERFIL DO FORNECEDOR
            PerfilCard(
                nome = "Nome do Fornecedor",
                descricao = "Profissional | Lisboa",
                avatarColor = Color(0xFF8E67FF)
            )

            // TÍTULO SEÇÃO
            Text(
                text = "Serviços Oferecidos",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            // LISTA DINÂMICA DE SERVIÇOS
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(servicos) { servico ->
                    PerfilCardComAcoesFornecedor(
                        servico = servico,
                        onEditar = { println("Editar ${servico.titulo}") },
                        onRemover = {
                            servicos = servicos.filter { it != servico } // remove da lista
                        }
                    )
                }
            }
        }
    }
}

// CARD SIMPLES — REUTILIZADO (mesma estrutura do cliente)

@Composable
fun PerfilCardFornecedor(
    nome: String,
    descricao: String,
    avatarColor: Color
) {
    PerfilCardFornecedor(nome, descricao, avatarColor)
}

// CARD DO SERVIÇO COM AÇÕES

@Composable
fun PerfilCardComAcoesFornecedor(
    servico: ServicoFornecedor,
    onEditar: () -> Unit,
    onRemover: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            // ÍCONE DO SERVIÇO
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(servico.avatarColor),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Avatar Serviço",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // TEXTOS
            Column(modifier = Modifier.weight(1f)) {
                Text(servico.titulo, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(servico.descricao, fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.width(8.dp))

            // BOTÕES DE AÇÃO
            Row {
                Button(
                    onClick = onEditar,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Editar", color = Color.White, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onRemover,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Remover", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

// PREVIEW

