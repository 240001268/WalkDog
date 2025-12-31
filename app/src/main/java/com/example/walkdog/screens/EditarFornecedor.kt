package com.example.walkdog.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.walkdog.viewmodel.EditarFornecedorViewModel
import com.example.walkdog.utils.buildFotoFornecedorUrl



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarFornecedorScreen(
    userId: String,
    onBackClick: () -> Unit,
    viewModel: EditarFornecedorViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // Carregar dados ao entrar
    LaunchedEffect(userId) {
        viewModel.loadFornecedor(userId)
    }

    // Quando salvar com sucesso → voltar
    LaunchedEffect(state.success) {
        if (state.success) {
            Toast.makeText(context, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
            onBackClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil do Fornecedor") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { padding ->

        when {
            state.loading -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

            state.error != null -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Erro: ${state.error}", color = Color.Red)
            }

            state.data != null -> {
                val fornecedor = state.data!!

                // CAMPOS
                var nome by remember { mutableStateOf(fornecedor["nome"]?.toString() ?: "") }
                var morada by remember { mutableStateOf(fornecedor["morada"]?.toString() ?: "") }
                var codPostal by remember { mutableStateOf(fornecedor["codpostal"]?.toString() ?: "") }
                var localidade by remember { mutableStateOf(fornecedor["localidade"]?.toString() ?: "") }
                var nif by remember { mutableStateOf(fornecedor["nif"]?.toString() ?: "") }
                var email by remember { mutableStateOf(fornecedor["email"]?.toString() ?: "") }
                var iban by remember { mutableStateOf(fornecedor["iban"]?.toString() ?: "") }

                // FOTO
                var fotoUri by remember { mutableStateOf<Uri?>(null) }
                val fotoId = fornecedor["fotoId"]?.toString()
                val fotoAtualUrl = buildFotoFornecedorUrl(fotoId)
                val imagemParaMostrar = fotoUri ?: fotoAtualUrl

                val imagePicker = rememberLauncherForActivityResult(
                    ActivityResultContracts.GetContent()
                ) { uri -> fotoUri = uri }

                // ⭐⭐ SCROLL AQUI ⭐⭐
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {

                    // FOTO
                    item {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray)
                                .clickable { imagePicker.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(imagemParaMostrar),
                                contentDescription = "Foto do fornecedor",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    // CAMPOS DE TEXTO
                    item {
                        OutlinedTextField(
                            value = nome,
                            onValueChange = { nome = it },
                            label = { Text("Nome") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = morada,
                            onValueChange = { morada = it },
                            label = { Text("Morada") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = codPostal,
                            onValueChange = { codPostal = it },
                            label = { Text("Código Postal") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = localidade,
                            onValueChange = { localidade = it },
                            label = { Text("Localidade") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = nif,
                            onValueChange = { nif = it },
                            label = { Text("NIF") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = iban,
                            onValueChange = { iban = it },
                            label = { Text("IBAN") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // BOTÃO
                    item {
                        Button(
                            onClick = {
                                viewModel.salvarAlteracoes(
                                    context = context,
                                    userId = userId,
                                    nome = nome,
                                    morada = morada,
                                    codPostal = codPostal,
                                    localidade = localidade,
                                    nif = nif,
                                    email = email,
                                    iban = iban,
                                    fotoUri = fotoUri
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A))
                        ) {
                            Text("Guardar Alterações", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
