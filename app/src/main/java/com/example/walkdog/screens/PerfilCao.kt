package com.example.walkdog.screens

import InfoCard
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.walkdog.model.Cao
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilCaoScreen(
    cao: Cao,
    onBackClick: () -> Unit = {}
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        cao.nome,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
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
                    containerColor = Color(0xFF6A1B9A)
                )
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFEDE7F6)),
                    contentAlignment = Alignment.Center
                ) {
                    if (cao.fotoUrl != null) {
                        Image(
                            painter = rememberAsyncImagePainter(cao.fotoUrl),
                            contentDescription = "Foto do cão",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text("Sem Foto", color = Color.Gray)
                    }
                }
            }

            item {
                InfoCard(title = "Informações do Cão") {
                    Text("Raça: ${cao.raca}")
                    Text("Porte: ${cao.porte}")
                    Text("Peso: ${cao.peso} kg")
                    Text("Localidade: ${cao.localidade}")
                }
            }

            item {
                InfoCard(title = "Informações do Dono") {
                    Text("Nome: ${cao.nomeDono}")
                    Text("Email: ${cao.emailDono}")
                    Text("Telefone: ${cao.telefoneDono}")
                    Text("Localidade: ${cao.localidadeDono}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPerfilCaoScreen() {
    PerfilCaoScreen(
        cao = Cao(
            nome = "Rex",
            raca = "Labrador",
            porte = "Grande",
            peso = "32",
            localidade = "Lisboa",
            nomeDono = "João Silva",
            emailDono = "joao@gmail.com",
            telefoneDono = "910000000",
            localidadeDono = "Lisboa"
        )
    )
}
