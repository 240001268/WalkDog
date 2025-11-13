package com.example.walkdog.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PasseiosScreen(onBackClick: () -> Unit = {}) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Passeios", color = Color.White, fontSize = 20.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6A1B9A)),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                actions = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatusChip("Agendado", Color(0xFF90CAF9))
                StatusChip("Em Andamento", Color(0xFFFFF59D))
                StatusChip("Cancelado", Color(0xFFFF8A80))
            }

            PasseioCard(
                titulo = "Parque Central",
                estado = "Concluído",
                tempo = "45 min",
                distancia = "3,2 km",
                preco = "25,00 €",
                data = "15/10/2025",
                color = Color(0xFFC8E6C9)
            )

            PasseioCard(
                titulo = "Agendado",
                estado = "Em Andamento",
                tempo = "45 min",
                distancia = "3,2 km",
                preco = "25,00 €",
                data = "",
                color = Color(0xFFFFF59D)
            )
        }
    }
}

@Composable
fun StatusChip(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(50.dp),
        color = color,
        tonalElevation = 2.dp
    ) {
        Text(text, modifier = Modifier.padding(12.dp), fontSize = 14.sp)
    }
}

@Composable
fun PasseioCard(
    titulo: String,
    estado: String,
    tempo: String,
    distancia: String,
    preco: String,
    data: String,
    color: Color
) {
    Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(titulo, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(Modifier.weight(1f))
                StatusChip(estado, color)
            }

            Spacer(Modifier.height(12.dp))

            Text("⏱ $tempo")
            Text("📍 $distancia")
            Text("💶 $preco")

            if (data.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(data, fontWeight = FontWeight.Light, color = Color.Gray)
            }
        }
    }
}
