package com.example.walkdog.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.walkdog.R // ✅ garante acesso aos recursos drawables corretos

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun MarcarPasseioScreen() {
    var zona by remember { mutableStateOf("Centro") }
    var horarioInicio by remember { mutableStateOf("17:00") }
    var horarioFim by remember { mutableStateOf("17:30") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Marcar Passeio",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF001F4D)
        )

        // --- Cão ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dog_rex),
                    contentDescription = "Foto do cão",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Rex", fontWeight = FontWeight.Bold)
                    Text("Fila — Grande", color = Color.Gray)
                }
            }
        }

        // --- Zona ---
        OutlinedTextField(
            value = zona,
            onValueChange = { zona = it },
            label = { Text("Zona") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            singleLine = true
        )

        // --- Horário ---
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = horarioInicio,
                onValueChange = { horarioInicio = it },
                label = { Text("Início") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )
            OutlinedTextField(
                value = horarioFim,
                onValueChange = { horarioFim = it },
                label = { Text("Fim") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )
        }

        // --- Fornecedor ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.carlos),
                    contentDescription = "Foto do fornecedor",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Carlos", fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Avaliação: ", color = Color.Gray)
                        Text("★ 4.9", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        // --- Preço + Botão ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Preço estimado", color = Color.Gray)
                Text(
                    "€12.50",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF001F4D)
                )
            }
            Button(
                onClick = { /* TODO: ação de confirmar */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF001F4D)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Confirmar & Iniciar")
            }
        }
    }
}