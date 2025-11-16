package com.example.walkdog.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.walkdog.R

// ---------------------------------------------------------
// DATA MODEL
// ---------------------------------------------------------
data class Passeio(
    val id: Int,
    val nomePasseador: String,
    val avatarColor: Color,
    val data: String,      // formato “12 Fev 2025”
    val grupoMes: String,  // exemplo: “Fevereiro 2025”
    val duracao: String
)


// ---------------------------------------------------------
// TELA PRINCIPAL
// ---------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricoPasseiosScreen(onBackClick: () -> Unit = {}, onDetalhes: (Int) -> Unit = {}) {

    val passeiosAgrupados = samplePasseios.groupBy { it.grupoMes }

    var filtroSelecionado by remember { mutableStateOf("Todos") }

    val filtros = listOf("Todos", "Este mês", "Por Passeador")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Histórico de Passeios",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
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
        ) {

            // ---------------------------------------------------------
            // FILTROS (CHIPS)
            // ---------------------------------------------------------
            Text("Filtros", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                filtros.forEach { f ->
                    FilterChipHistorico(
                        text = f,
                        selected = filtroSelecionado == f,
                        onClick = { filtroSelecionado = f }
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ---------------------------------------------------------
            // LISTA COM AGRUPAMENTO
            // ---------------------------------------------------------
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                passeiosAgrupados.forEach { (mes, listaPasseios) ->

                    item {
                        Text(
                            mes,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF6A1B9A)
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    items(listaPasseios) { passeio ->
                        PasseioHistoricoCard(
                            passeio = passeio,
                            onDetalhes = { onDetalhes(passeio.id) }
                        )
                    }
                }
            }
        }
    }
}


// ---------------------------------------------------------
// CHIP DE FILTRO CUSTOMIZADO
// ---------------------------------------------------------
@Composable
fun FilterChipHistorico(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(30.dp),
        color = if (selected) Color(0xFF6A1B9A) else Color(0xFFEDE7F6),
        modifier = Modifier,
        onClick = onClick
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (selected) Color.White else Color.Black,
            fontSize = 14.sp
        )
    }
}


// ---------------------------------------------------------
// CARD DO PASSEIO
// ---------------------------------------------------------
@Composable
fun PasseioHistoricoCard(passeio: Passeio, onDetalhes: () -> Unit) {

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Avatar colorido
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(passeio.avatarColor),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(passeio.nomePasseador, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(passeio.data, fontSize = 14.sp, color = Color.Gray)
            }

            Column(horizontalAlignment = Alignment.End) {

                // Ícone de concluído
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = "Concluído",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(Modifier.height(8.dp))

                // Botão de detalhes
                Button(
                    onClick = onDetalhes,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text("Detalhes", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}


// ---------------------------------------------------------
// DADOS DE EXEMPLO
// ---------------------------------------------------------
val samplePasseios = listOf(
    Passeio(
        id = 1,
        nomePasseador = "João Silva",
        avatarColor = Color(0xFF8E67FF),
        data = "12 Fev 2025",
        grupoMes = "Fevereiro 2025",
        duracao = "30 min"
    ),
    Passeio(
        id = 2,
        nomePasseador = "Maria Santos",
        avatarColor = Color(0xFFFF8A65),
        data = "07 Fev 2025",
        grupoMes = "Fevereiro 2025",
        duracao = "45 min"
    ),
    Passeio(
        id = 3,
        nomePasseador = "Pedro Costa",
        avatarColor = Color(0xFF64B5F6),
        data = "23 Jan 2025",
        duracao = "1h 10min",
        grupoMes = "Janeiro 2025"
    )
)

@Preview(showBackground = true)
@Composable
fun PreviewHistorico2() {
    HistoricoPasseiosScreen()
}