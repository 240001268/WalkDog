package com.example.walkdog.Screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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

data class PasseioOpcao(
    val titulo: String,
    val duracao: Int,
    val preco: Int,
    val tipo: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilFornecedorScreen(
    nomeFornecedor: String,
    localidadeFornecedor: String,
    ratingFornecedor: String,
    onBackClick: () -> Unit = {},
    onScheduleClick: (String, Int, Int) -> Unit
) {
    // Garante que o botão físico de voltar funciona
    BackHandler { onBackClick() }

    val passeios = listOf(
        PasseioOpcao("Passeio Rápido", 30, 12, "Rápido"),
        PasseioOpcao("Passeio Rápido", 60, 20, "Rápido"),
        PasseioOpcao("Passeio Longo", 90, 30, "Longo"),
        PasseioOpcao("Passeio Longo", 120, 45, "Longo")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Perfil do Fornecedor",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
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
                colors = TopAppBarDefaults.topAppBarColors(
                    Color(0xFF6A1B9A)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF8E67FF))
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(nomeFornecedor, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(localidadeFornecedor, fontSize = 14.sp, color = Color.Gray)
                        Text("⭐ $ratingFornecedor", fontSize = 14.sp)
                    }
                }
            }

            Text("Passeios Disponíveis", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            passeios.forEach { passeio ->
                PasseioFornecedorCard(
                    passeio = passeio,
                    onAgendar = { onScheduleClick(passeio.tipo, passeio.duracao, passeio.preco) }
                )
            }
        }
    }
}

@Composable
fun PasseioFornecedorCard(
    passeio: PasseioOpcao,
    onAgendar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {
                Text(passeio.titulo, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("${passeio.duracao} min", fontSize = 14.sp, color = Color.DarkGray)
                Text("${passeio.preco}€", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Button(
                onClick = onAgendar,
                colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Agendar", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPerfilFornecedor() {
    PerfilFornecedorScreen(
        nomeFornecedor = "Carlos Andrade",
        localidadeFornecedor = "Lisboa",
        ratingFornecedor = "4.9",
        onScheduleClick = { _, _, _ -> }
    )
}
