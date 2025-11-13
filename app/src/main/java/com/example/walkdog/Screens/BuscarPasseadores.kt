package com.example.walkdog.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun BuscarPasseadoresScreen(onBackClick: () -> Unit = {}) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Buscar Passeadores",
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

            // 🔍 Campo de busca
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Buscar por nome ou localidade...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(16.dp))

            // ⚙️ Título dos filtros
            Text("Filtros", fontWeight = FontWeight.Medium, fontSize = 16.sp)

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FilterChip(text = "Zona", selected = false)
                FilterChip(text = "Raça Especializada", selected = false)
                FilterChip(text = "Porte", selected = false)
            }

            Spacer(Modifier.height(16.dp))

            Text("3 passeador(es) encontrado(s)", fontWeight = FontWeight.Light)

            Spacer(Modifier.height(16.dp))

            PasseadorCard(
                nome = "João Silva",
                localidade = "Lisboa",
                rating = "4.8",
                avatarColor = Color(0xFF8E67FF)
            )
            PasseadorCard(
                nome = "Maria Santos",
                localidade = "Porto",
                rating = "4.8",
                avatarColor = Color(0xFFFF8A65)
            )
            PasseadorCard(
                nome = "Pedro Costa",
                localidade = "Braga",
                rating = "4.8",
                avatarColor = Color(0xFF64B5F6)
            )
        }
    }
}

@Composable
fun FilterChip(text: String, selected: Boolean) {
    Surface(
        shape = RoundedCornerShape(30.dp),
        color = if (selected) Color(0xFF6A1B9A) else Color(0xFFEDE7F6),
        tonalElevation = 2.dp
    ) {
        Text(
            text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (selected) Color.White else Color.Black,
            fontSize = 14.sp
        )
    }
}

@Composable
fun PasseadorCard(nome: String, localidade: String, rating: String, avatarColor: Color) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Avatar circular com cor personalizada
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(avatarColor)
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(nome, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text("⭐ $rating", fontSize = 14.sp)
                Text(localidade, fontSize = 14.sp, color = Color.Gray)
            }

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Ver Perfil Completo", color = Color.White, fontSize = 13.sp)
            }
        }
    }
}
