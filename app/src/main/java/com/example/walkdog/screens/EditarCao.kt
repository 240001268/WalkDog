package com.example.walkdog.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.walkdog.utils.buildFotoCaoUrl
import com.example.walkdog.viewmodel.EditarCaoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarCaoScreen(
    caoId: String,
    onBackClick: () -> Unit,
    viewModel: EditarCaoViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(caoId) {
        viewModel.loadCao(caoId)
    }

    LaunchedEffect(state.success) {
        if (state.success) onBackClick()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Cão") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->

        when {
            state.loading -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            state.error != null -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Erro: ${state.error}", color = Color.Red)
            }

            state.data != null -> {
                val cao = state.data!!

                var nome by remember { mutableStateOf(cao["nome"]?.toString() ?: "") }
                var raca by remember { mutableStateOf(cao["raca"]?.toString() ?: "") }
                var porte by remember { mutableStateOf(cao["porte"]?.toString() ?: "") }
                var peso by remember { mutableStateOf(cao["peso"]?.toString() ?: "") }
                var localidade by remember { mutableStateOf(cao["localidade"]?.toString() ?: "") }

                val fotoId = cao["fotoId"]?.toString()
                val fotoUrl = buildFotoCaoUrl(fotoId)

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // 🐶 FOTO DO CÃO
                    if (fotoUrl != null) {
                        AsyncImage(
                            model = fotoUrl,
                            contentDescription = "Foto do cão",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🐶", style = MaterialTheme.typography.headlineLarge)
                        }
                    }

                    // 🔹 CAMPOS CENTRADOS
                    OutlinedTextField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = { Text("Nome") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 420.dp)
                    )

                    OutlinedTextField(
                        value = raca,
                        onValueChange = { raca = it },
                        label = { Text("Raça") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 420.dp)
                    )

                    OutlinedTextField(
                        value = porte,
                        onValueChange = { porte = it },
                        label = { Text("Porte") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 420.dp)
                    )

                    OutlinedTextField(
                        value = peso,
                        onValueChange = { peso = it },
                        label = { Text("Peso") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 420.dp)
                    )

                    OutlinedTextField(
                        value = localidade,
                        onValueChange = { localidade = it },
                        label = { Text("Localidade") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 420.dp)
                    )

                    Button(
                        onClick = {
                            viewModel.salvarAlteracoes(
                                caoId = caoId,
                                nome = nome,
                                raca = raca,
                                porte = porte,
                                peso = peso,
                                localidade = localidade
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .widthIn(max = 420.dp)
                    ) {
                        Text("Guardar Alterações")
                    }
                }
            }
        }
    }
}
