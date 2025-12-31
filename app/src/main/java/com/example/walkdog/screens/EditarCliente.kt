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
import com.example.walkdog.viewmodel.EditarClienteViewModel
import com.example.walkdog.utils.buildFotoClienteUrl


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarClienteScreen(
    userId: String,
    onBackClick: () -> Unit,
    viewModel: EditarClienteViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadCliente()
        }

    LaunchedEffect(state.success) {
        if (state.success) {
            Toast.makeText(context, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
            onBackClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Perfil do Cliente") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->

        when {
            state.loading -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            state.error != null -> Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) { Text("Erro: ${state.error}", color = Color.Red) }

            state.data != null -> {
                val cliente = state.data!!

                var nome by remember { mutableStateOf(cliente["nome"]?.toString() ?: "") }
                var morada by remember { mutableStateOf(cliente["morada"]?.toString() ?: "") }
                var codPostal by remember { mutableStateOf(cliente["codpostal"]?.toString() ?: "") }
                var localidade by remember { mutableStateOf(cliente["localidade"]?.toString() ?: "") }
                var nif by remember { mutableStateOf(cliente["nif"]?.toString() ?: "") }
                var email by remember { mutableStateOf(cliente["email"]?.toString() ?: "") }
                var iban by remember { mutableStateOf(cliente["iban"]?.toString() ?: "") }

                var fotoUri by remember { mutableStateOf<Uri?>(null) }
                val fotoId = cliente["fotoId"]?.toString()
                val fotoAtualUrl = buildFotoClienteUrl(fotoId)

                val imagemParaMostrar = fotoUri ?: fotoAtualUrl

                val imagePicker = rememberLauncherForActivityResult(
                    ActivityResultContracts.GetContent()
                ) { uri -> fotoUri = uri }

                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {

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
                                contentDescription = "Foto do cliente",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    item { OutlinedTextField(nome, { nome = it }, label = { Text("Nome") }, modifier = Modifier.fillMaxWidth()) }
                    item { OutlinedTextField(morada, { morada = it }, label = { Text("Morada") }, modifier = Modifier.fillMaxWidth()) }
                    item { OutlinedTextField(codPostal, { codPostal = it }, label = { Text("Código Postal") }, modifier = Modifier.fillMaxWidth()) }
                    item { OutlinedTextField(localidade, { localidade = it }, label = { Text("Localidade") }, modifier = Modifier.fillMaxWidth()) }
                    item { OutlinedTextField(nif, { nif = it }, label = { Text("NIF") }, modifier = Modifier.fillMaxWidth()) }
                    item { OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth()) }
                    item { OutlinedTextField(iban, { iban = it }, label = { Text("IBAN") }, modifier = Modifier.fillMaxWidth()) }

                    item {
                        Button(
                            onClick = {
                                viewModel.salvarAlteracoes(
                                    context = context,
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

