package com.example.walkdog.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilFornecedorScreen(
    onBackClick: () -> Unit = {},
    onContactClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {}
) {
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
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Share, contentDescription = "Partilhar", tint = Color.White)
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorito", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6A1B9A))
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // FOTO DO PERFIL
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8E8E8)),
                contentAlignment = Alignment.Center
            ) {
                // Placeholder circular
                Icon(
                    painter = painterResource(android.R.drawable.sym_def_app_icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }

            // NOME + FUNÇÃO
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Nome do Fornecedor", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Fornecedor Profissional",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            // AVALIAÇÕES
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F3FF))
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Rating",
                            tint = Color(0xFF6A1B9A),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("4.8", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    }

                    Text("45 avaliações", fontSize = 16.sp, color = Color.Gray)
                }
            }

            // INFORMAÇÕES DE CONTACTO
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Informações de Contacto", fontWeight = FontWeight.Bold, fontSize = 18.sp)

                    InfoRow(icon = android.R.drawable.ic_menu_call, text = "+351 987 654 321")
                    InfoRow(icon = android.R.drawable.ic_dialog_email, text = "email@fornecedor.com")
                    InfoRow(icon = android.R.drawable.ic_menu_mylocation, text = "Lisboa")
                    InfoRow(icon = android.R.drawable.ic_menu_compass, text = "Zona Central")
                }
            }

            // DETALHES DO SERVIÇO
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Detalhes do Serviço", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    Row {
                        Icon(painterResource(android.R.drawable.star_big_on), contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Icon(painterResource(android.R.drawable.star_big_on), contentDescription = null)
                    }
                }
            }

            Spacer(Modifier.height(10.dp))

            // BOTÕES
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onContactClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Contactar")
                }
                Button(
                    onClick = onScheduleClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A))
                ) {
                    Icon(painterResource(android.R.drawable.ic_menu_my_calendar), contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Agendar", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun InfoRow(icon: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = Color(0xFF6A1B9A),
            modifier = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text(text, fontSize = 16.sp)
    }
}
