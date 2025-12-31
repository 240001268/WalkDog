package com.example.walkdog.screens

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
import com.example.walkdog.model.Cao
import com.example.walkdog.viewmodel.PerfilClienteViewModel
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.walkdog.utils.buildFotoClienteUrl
import com.example.walkdog.utils.buildFotoCaoUrl
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// ------------------------------------------------------------
// MINI MENU CLIENTE
// ------------------------------------------------------------
@Composable
fun MiniMenuCliente(
    onRegistarCao: () -> Unit,
    onBuscarFornecedor: () -> Unit,
    onMarcarPasseio: () -> Unit,
    onHistoricoClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MenuButton("Registar Cão", onRegistarCao)
        MenuButton("Buscar Fornecedor", onBuscarFornecedor)
        MenuButton("Marcar Passeio", onMarcarPasseio)
        MenuButton("Histórico de Passeios", onHistoricoClick)
    }
}

@Composable
fun MenuButton(text: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(Color(0xFFF1E6FF)),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 17.sp,
                color = Color(0xFF6A1B9A),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// ------------------------------------------------------------
// CARD DO CÃO
// ------------------------------------------------------------
@Composable
fun CaoCard(
    cao: Cao,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // 🐶 FOTO DO CÃO
            if (!cao.fotoUrl.isNullOrBlank()) {
                AsyncImage(
                    model = cao.fotoUrl,
                    contentDescription = "Foto do cão",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF8E67FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        cao.nome.firstOrNull()?.uppercase() ?: "",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(cao.nome, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(cao.raca, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

// ------------------------------------------------------------
// PERFIL CLIENTE — PRINCIPAL
// ------------------------------------------------------------
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilClienteScreen(
    userId: String,
    onRegistarCao: () -> Unit,
    onBuscarFornecedor: () -> Unit,
    onMarcarPasseio: () -> Unit,
    onCaoClick: (String) -> Unit, // ✅ agora recebe o ID do cão
    onHistoricoClick: () -> Unit,
    onBackClick: () -> Unit,
    onEditarCliente: (String) -> Unit
) {
    val viewModel: PerfilClienteViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getClienteData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil do Cliente", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {

            if (state.loading) CircularProgressIndicator()

            state.error?.let {
                Text("Erro: $it", color = Color.Red)
            }

            // PERFIL CLIENTE
            state.cliente?.let { cliente ->
                val fotoUrl = buildFotoClienteUrl(cliente.data["fotoId"]?.toString())
                val nome = cliente.data["nome"]?.toString() ?: ""
                val localidade = cliente.data["localidade"]?.toString() ?: ""

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            cliente.data["userId"]?.toString()?.let(onEditarCliente)
                        },
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (fotoUrl != null) {
                            AsyncImage(
                                model = fotoUrl,
                                contentDescription = "Foto do cliente",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF8E67FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    nome.firstOrNull()?.toString() ?: "",
                                    color = Color.White,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(Modifier.width(16.dp))

                        Column {
                            Text(nome, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                            Text(localidade, fontSize = 15.sp, color = Color.Gray)
                        }
                    }
                }
            }

            MiniMenuCliente(
                onRegistarCao,
                onBuscarFornecedor,
                onMarcarPasseio,
                onHistoricoClick
            )

            Text("Cães Registados", fontWeight = FontWeight.Bold, fontSize = 20.sp)

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                state.caes.forEach { doc ->

                    val cao = Cao(
                        nome = doc.data["nome"]?.toString() ?: "",
                        raca = doc.data["raca"]?.toString() ?: "",
                        porte = doc.data["porte"]?.toString() ?: "",
                        peso = doc.data["peso"]?.toString() ?: "",
                        localidade = doc.data["localidade"]?.toString() ?: "",
                        fotoUrl = buildFotoCaoUrl(doc.data["fotoId"]?.toString()),
                        nomeDono = "",
                        emailDono = "",
                        telefoneDono = "",
                        localidadeDono = ""
                    )

                    CaoCard(
                        cao = cao,
                        onClick = {
                            onCaoClick(doc.id) // ✅ ID REAL DO CÃO
                        }
                    )
                }
            }
        }
    }
}

// ------------------------------------------------------------
// HELPER – ROTA DO PERFIL DO CÃO
// ------------------------------------------------------------
fun buildPerfilCaoRoute(cao: Cao): String {
    fun enc(v: String) = URLEncoder.encode(v, StandardCharsets.UTF_8.toString())

    return "perfil_cao/" +
            "${enc(cao.nome)}/" +
            "${enc(cao.raca)}/" +
            "${enc(cao.porte)}/" +
            "${enc(cao.peso)}/" +
            "${enc(cao.localidade)}/" +
            "${enc(cao.fotoUrl ?: "")}"
}
