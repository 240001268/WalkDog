package com.example.perfilcao

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilCaoScreen(onBackClick: () -> Unit = {}) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rex", color = Color.White) },
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
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Apagar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF7B42F6)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(20.dp))

            // 🐶 Avatar
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF7B42F6)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.dog_icon),
                    contentDescription = "Dog Avatar",
                    modifier = Modifier.size(90.dp)
                )
            }

            Spacer(Modifier.height(20.dp))

            // 📊 Estatísticas
            StatsGrid(
                stats = listOf(
                    StatItem(Icons.Default.DirectionsWalk, "12", "Total"),
                    StatItem(Icons.Default.CheckCircle, "8", "Concluídos"),
                    StatItem(Icons.Default.Place, "24,5 km", "Distância"),
                    StatItem(Icons.Default.AccessTime, "180 min", "Duração")
                )
            )

            Spacer(Modifier.height(16.dp))

            // 🐕 Informações do Cão
            InfoSection(
                title = "Informações do Cão",
                icon = Icons.Default.Pets,
                content = {
                    InfoRow("Nome:", "Rex")
                    InfoRow("Raça:", "Labrador")
                    InfoRow("Morada:", "")
                    InfoRow("Localidade:", "")
                }
            )

            Spacer(Modifier.height(8.dp))

            // 👤 Informações do Tutor
            InfoSection(
                title = "Informações do Tutor",
                icon = Icons.Default.Person,
                content = {},
                trailing = {
                    FloatingActionButton(
                        onClick = {},
                        containerColor = Color(0xFF7B42F6),
                        shape = CircleShape,
                        modifier = Modifier.size(50.dp)
                    ) {
                        Icon(Icons.Default.Add, "Adicionar", tint = Color.White)
                    }
                }
            )
        }
    }
}
