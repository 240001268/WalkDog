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
import com.example.walkdog.componentes.PasseioFornecedorCard
import com.example.walkdog.utils.buildFotoFornecedorUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilFornecedorScreen(
    fornecedorId: String?,
    onLogoutClick: () -> Unit,
    onBackClick: () -> Unit, // 👈 NOVO
    onEditPerfil: (String) -> Unit,
    onEscolherPasseiosClick: () -> Unit,
    onVerPasseiosClick: () -> Unit,
    onScheduleClick: (String) -> Unit

) {
    val viewModel: PerfilFornecedorViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    val isPerfilProprio = fornecedorId == null

    // 🔙 BackHandler só no perfil próprio
    if (isPerfilProprio) {
        BackHandler { onLogoutClick() }
    }

    // 🔄 Carregar dados
    LaunchedEffect(fornecedorId) {
        if (fornecedorId != null) {
            viewModel.getFornecedorData(fornecedorId)
        } else {
            viewModel.getFornecedorDataDoUserLogado()
        }
    }

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
                    IconButton(
                        onClick = {
                            if (isPerfilProprio) {
                                onLogoutClick()
                            } else {
                                onBackClick()
                            }
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            if (state.loading) CircularProgressIndicator()
            state.error?.let { Text("Erro: $it", color = Color.Red) }

            // ---------------- FORNECEDOR ----------------
            state.fornecedor?.let { fornecedor ->

                val fotoId = fornecedor.data["fotoId"]?.toString()?.takeIf { it.isNotBlank() }
                val fotoUrl = fotoId?.let { buildFotoFornecedorUrl(it) }

                val nome = fornecedor.data["nome"]?.toString() ?: ""
                val localidade = fornecedor.data["localidade"]?.toString() ?: ""

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = isPerfilProprio) {
                            onEditPerfil(fornecedor.id)
                        },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (fotoUrl != null) {
                            AsyncImage(
                                model = fotoUrl,
                                contentDescription = null,
                                modifier = Modifier.size(70.dp).clip(CircleShape),
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

                        Spacer(Modifier.width(16.dp))

                        Column {
                            Text(nome, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                            Text(localidade, fontSize = 14.sp, color = Color.Gray)
                        }
                    }
                }
            }

            // ---------------- BOTÕES SÓ PERFIL PRÓPRIO ----------------
            if (isPerfilProprio) {

                Button(
                    onClick = onEscolherPasseiosClick,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A))
                ) {
                    Text("Escolher Passeios", color = Color.White)
                }

                Button(
                    onClick = onVerPasseiosClick,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A))
                ) {
                    Text("Ver meus Passeios", color = Color.White)
                }
            }

            // ---------------- PASSEIOS ----------------
            Text("Passeios Disponíveis", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            state.passeios.forEach { passeio ->
                PasseioFornecedorCard(
                    descricao = passeio.descricao,
                    duracaoStr = passeio.duracao,
                    precoStr = passeio.preco,
                    enabled = isPerfilProprio,
                    onAgendar = {
                        if (isPerfilProprio) {
                            onScheduleClick(passeio.tipoId)
                        }
                    }
                )
            }
        }
    }
}
