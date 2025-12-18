package com.example.walkdog.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.walkdog.viewmodel.BuscarFornecedoresViewModel
import com.example.walkdog.viewmodel.FornecedorItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuscarFornecedoresScreen(
    onBackClick: () -> Unit = {},
    onVerPerfil: (String) -> Unit = {}   // agora só envia o ID real
) {
    val viewModel: BuscarFornecedoresViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    var search by remember { mutableStateOf("") }
    var ratingMin by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.loadFornecedores()
    }

    val filtrados = state.fornecedores.filter {
        (it.nome.contains(search, ignoreCase = true) ||
                it.localidade.contains(search, ignoreCase = true)) &&
                it.rating >= ratingMin
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar Fornecedores", color = Color.White, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(Color(0xFF6A1B9A))
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            if (state.loading) {
                CircularProgressIndicator()
            }

            state.error?.let {
                Text("Erro: $it", color = Color.Red)
            }

            OutlinedTextField(
                value = search,
                onValueChange = { search = it },
                placeholder = { Text("Buscar por nome ou localidade...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text("Filtrar por rating mínimo", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 1..5) {
                    Icon(
                        imageVector = if (ratingMin >= i) Icons.Filled.Star else Icons.Outlined.Star,
                        contentDescription = null,
                        tint = if (ratingMin >= i) Color(0xFFFFC107) else Color.Gray,
                        modifier = Modifier
                            .size(36.dp)
                            .padding(horizontal = 4.dp)
                            .clickable { ratingMin = i }
                    )
                }
            }

            Spacer(Modifier.height(6.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Limpar filtro",
                    color = Color(0xFF6A1B9A),
                    modifier = Modifier
                        .clickable {
                            ratingMin = 0
                            search = ""
                        }
                        .padding(vertical = 4.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            Text(
                "${filtrados.size} fornecedor(es) encontrado(s)",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(Modifier.height(16.dp))

            filtrados.forEach { fornecedor ->
                FornecedorCard(
                    fornecedor = fornecedor,
                    onClick = { onVerPerfil(fornecedor.id) }   // envia ID real
                )
            }
        }
    }
}

@Composable
fun FornecedorCard(
    fornecedor: FornecedorItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF8E67FF)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    fornecedor.nome.first().uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(fornecedor.nome, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("⭐ ${fornecedor.rating}", fontSize = 14.sp)
                Text(fornecedor.localidade, fontSize = 14.sp, color = Color.Gray)
            }

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ver Perfil", color = Color.White)
            }
        }
    }
}
