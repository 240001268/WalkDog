package com.example.walkdog.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.walkdog.viewmodel.PerfilFornecedorViewModel
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.walkdog.utils.buildFotoFornecedorUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilFornecedorScreen(
    fornecedorId: String?,
    onLogoutClick: () -> Unit,
    onEditPerfil: (String) -> Unit,
    onEscolherPasseiosClick: () -> Unit,
    onVerPasseiosClick: () -> Unit,
    onScheduleClick: (String) -> Unit
) {
    val viewModel: PerfilFornecedorViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    // 🔙 Back = Logout
    BackHandler { onLogoutClick() }

    // 🔄 Carregar dados do fornecedor
    LaunchedEffect(fornecedorId) {
        if (fornecedorId != null) {
            // 🔓 Perfil público
            viewModel.getFornecedorData(fornecedorId)
        } else {
            // 🔐 Perfil próprio
            viewModel.getFornecedorDataDoUserLogado()
        }
    }

    // 🔄 Carregar passeios quando o fornecedor existir
    LaunchedEffect(state.fornecedor) {
        if (state.fornecedor != null) {
            viewModel.getPasseiosFornecedor()
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Perfil do Fornecedor",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onLogoutClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Logout",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    Color(0xFF6A1B9A)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ⏳ Loading
            if (state.loading) {
                CircularProgressIndicator()
            }

            // ❌ Erro
            state.error?.let {
                Text("Erro: $it", color = Color.Red)
            }

            // --------------------------------------------------------------
            // CARD DO FORNECEDOR
            // --------------------------------------------------------------
            state.fornecedor?.let { fornecedor ->

                val fotoId = fornecedor.data["fotoId"]?.toString()
                val fotoUrl = buildFotoFornecedorUrl(fotoId)

                val nome = fornecedor.data["nome"]?.toString() ?: ""
                val localidade = fornecedor.data["localidade"]?.toString() ?: ""

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onEditPerfil(fornecedor.id) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (fotoUrl != null) {
                            AsyncImage(
                                model = fotoUrl,
                                contentDescription = "Foto do fornecedor",
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF8E67FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    nome.firstOrNull()?.uppercase() ?: "",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(nome, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Text(localidade, fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                }
            }

            // --------------------------------------------------------------
            // BOTÃO → ESCOLHER PASSEIOS
            // --------------------------------------------------------------
            Button(
                onClick = onEscolherPasseiosClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    "Escolher Passeios",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color.White
                )
            }

            // --------------------------------------------------------------
            // BOTÃO → VER MEUS PASSEIOS
            // --------------------------------------------------------------
            Button(
                onClick = { onVerPasseiosClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    "Ver meus Passeios",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Color.White
                )
            }

            // --------------------------------------------------------------
            // LISTA DOS PASSEIOS
            // --------------------------------------------------------------
            Text(
                "Passeios Disponíveis",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            state.passeios.forEach { passeio ->

                val descricao = passeio.data["descricao"]?.toString() ?: "Sem descrição"
                val duracao = passeio.data["duracao"]?.toString()?.toIntOrNull() ?: 0
                val preco = passeio.data["preco"]?.toString()?.toIntOrNull() ?: 0

                PasseioFornecedorCard(
                    descricao = descricao,
                    duracaoStr = duracao.toString(),
                    precoStr = preco.toString(),
                    onAgendar = {
                        onScheduleClick(descricao)
                    }
                )
            }
        }
    }
}

@Composable
fun PasseioFornecedorCard(
    descricao: String,
    duracaoStr: String,
    precoStr: String,
    onAgendar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Descrição: $descricao",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text("Duração: $duracaoStr min", fontSize = 15.sp)
            Text("Preço: €$precoStr", fontSize = 15.sp)

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onAgendar,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Agendar Passeio", color = Color.White)
            }
        }
    }
}
