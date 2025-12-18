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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.walkdog.viewmodel.FormularioFornecedorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioFornecedorScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    viewModel: FormularioFornecedorViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    // Campos do formulário
    var nome by remember { mutableStateOf("") }
    var morada by remember { mutableStateOf("") }
    var codPostal by remember { mutableStateOf("") }
    var localidade by remember { mutableStateOf("") }
    var nif by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var iban by remember { mutableStateOf("") }

    // Foto
    var fotoUri by remember { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> fotoUri = uri }

    // Sucesso → Navega
    LaunchedEffect(state.success) {
        if (state.success) {
            Toast.makeText(context, "Fornecedor registado com sucesso!", Toast.LENGTH_SHORT).show()
            onSaveClick()
        }
    }

    // Erro → Toast
    LaunchedEffect(state.error) {
        state.error?.let {
            Toast.makeText(context, "Erro: $it", Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registar Fornecedor") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { padding ->

        // ⭐⭐⭐ AQUI ESTÁ O SCROLL ⭐⭐⭐
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {

            // ------------------ FOTO ------------------
            item {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                        .clickable { imagePicker.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (fotoUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(fotoUri),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text("Foto", color = Color.DarkGray)
                    }
                }
            }

            // ------------------ CAMPOS ------------------
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
                    label = { Text("E-mail") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Senha") },
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

            // ------------------ BOTÃO ------------------
            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {

                        if (nome.isBlank() || email.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Preencha os campos obrigatórios.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        viewModel.salvarFornecedor(
                            context = context,
                            nome = nome,
                            morada = morada,
                            codPostal = codPostal,
                            localidade = localidade,
                            nif = nif,
                            email = email,
                            password = password,
                            iban = iban,
                            fotoUri = fotoUri
                        )
                    }
                ) {
                    if (state.loading)
                        CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                    else
                        Text("Registar Fornecedor")
                }
            }
        }
    }
}
