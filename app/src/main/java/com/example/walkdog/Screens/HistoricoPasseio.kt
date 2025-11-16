package com.example.walkdog.Screens

import androidx.compose.foundation.Image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.res.painterResource
import com.example.walkdog.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricoPasseiosScreen(onBackClick: () -> Unit = {}) {

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
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Voltar",
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Título informativo
            Text(
                "3 passeio(s) concluído(s)",
                fontSize = 15.sp,
                color = Color.DarkGray
            )

            // Lista de passeios
            PasseioHistoricoItem(
                nome = "João Silva",
                data = "12 Fev 2025",
                duracao = "30 min",
                avatarColor = Color(0xFF8E67FF)
            )
            PasseioHistoricoItem(
                nome = "Maria Santos",
                data = "07 Fev 2025",
                duracao = "45 min",
                avatarColor = Color(0xFFFF8A65)
            )
            PasseioHistoricoItem(
                nome = "Pedro Costa",
                data = "03 Fev 2025",
                duracao = "1h 10min",
                avatarColor = Color(0xFF64B5F6)
            )
        }
    }
}

@Composable
fun PasseioHistoricoItem(
    nome: String,
    data: String,
    duracao: String,
    avatarColor: Color
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Avatar
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(avatarColor),
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
                Text(nome, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(data, fontSize = 14.sp, color = Color.Gray)
            }

            // Duração
            Text(
                duracao,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6A1B9A)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHistorico() {
    HistoricoPasseiosScreen()
}
