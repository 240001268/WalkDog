package com.example.walkdog.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.walkdog.R

// ---------------------------------------------------------
// DATA MODEL DO CÃO
// ---------------------------------------------------------
data class Cao(
    val nome: String,
    val raca: String,
    val avatarColor: Color
)

// ---------------------------------------------------------
// TELA PRINCIPAL
// ---------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomePage(navController: NavHostController = rememberNavController()) {

    // Lista dinâmica de cães
    var caes by remember {
        mutableStateOf(
            listOf(
                Cao("Max", "Labrador Retriever", Color(0xFFFF8A65)),
                Cao("Luna", "Golden Retriever", Color(0xFF64B5F6)),
                Cao("Rex", "Fila — Grande", Color(0xFF8E67FF))
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Bem-vindo",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6A1B9A))
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // PERFIL DO CLIENTE
            PerfilCard(
                nome = "Nome do Cliente",
                descricao = "Localidade",
                avatarColor = Color(0xFF8E67FF)
            )

            // TÍTULO SEÇÃO
            Text(
                text = "Cães Registrados",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            // LISTA DINÂMICA DE CÃES
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(caes) { cao ->
                    PerfilCardComAcoes(
                        cao = cao,
                        onEditar = { println("Editar ${cao.nome}") },
                        onRemover = {
                            caes = caes.filter { it != cao } // remove o cão da lista
                        }
                    )
                }
            }


        }
    }
}

// ---------------------------------------------------------
// CARD DO CLIENTE OU CÃO (simples)
// ---------------------------------------------------------
@Composable
fun PerfilCard(nome: String, descricao: String, avatarColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(avatarColor),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = nome,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
                Text(
                    text = descricao,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// ---------------------------------------------------------
// CARD DO CÃO COM BOTÕES DE AÇÃO
// ---------------------------------------------------------
@Composable
fun PerfilCardComAcoes(cao: Cao, onEditar: () -> Unit, onRemover: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(cao.avatarColor),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(cao.nome, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(cao.raca, fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.width(8.dp))

            Row {
                Button(
                    onClick = onEditar,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Editar", color = Color.White, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onRemover,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Remover", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWelcomePageDinâmica() {
    WelcomePage()
}