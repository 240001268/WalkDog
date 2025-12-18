package com.example.walkdog.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.walkdog.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasseiosDisponiveisScreen(onBackClick: () -> Unit = {}) {

    val passeios = listOf(
        PasseioDisponivel(
            cachorroNome = "Rex",
            raca = "Fila — Grande",
            zona = "Centro",
            horario = "17:00 — 17:30",
            preco = "€12.50",
            foto = R.drawable.dog_rex
        ),
        PasseioDisponivel(
            cachorroNome = "Luna",
            raca = "Labrador — Médio",
            zona = "Bairro Alto",
            horario = "16:30 — 17:00",
            preco = "€10.00",
            foto = R.drawable.dog_rex
        ),
        PasseioDisponivel(
            cachorroNome = "Bobby",
            raca = "Beagle — Pequeno",
            zona = "Alvalade",
            horario = "18:00 — 18:45",
            preco = "€14.00",
            foto = R.drawable.dog_rex
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Passeios Disponíveis",
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

            passeios.forEach { passeio ->
                CardPasseioDisponivel(passeio)
            }
        }
    }
}

@Composable
fun CardPasseioDisponivel(passeio: PasseioDisponivel) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            // Linha principal
            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = painterResource(id = passeio.foto),
                    contentDescription = "Foto do cão",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        passeio.cachorroNome,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        passeio.raca,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        passeio.zona,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF6A1B9A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Horário + Preço
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Horário", color = Color.Gray, fontSize = 13.sp)
                    Text(
                        passeio.horario,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Preço", color = Color.Gray, fontSize = 13.sp)
                    Text(
                        passeio.preco,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6A1B9A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botão Aceitar
            Button(
                onClick = { /* TODO: Acionar aceite do passeio */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Aceitar Passeio", color = Color.White)
            }
        }
    }
}

data class PasseioDisponivel(
    val cachorroNome: String,
    val raca: String,
    val zona: String,
    val horario: String,
    val preco: String,
    val foto: Int
)

@Preview(showBackground = true)
@Composable
fun PreviewPasseiosDisponiveisScreen() {
    PasseiosDisponiveisScreen()
}
