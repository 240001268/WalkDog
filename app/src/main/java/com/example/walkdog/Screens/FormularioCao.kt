package com.example.formulariocao

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
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioCaoScreen(onBackClick: () -> Unit = {}) {

    // Estados
    var nomeCao by remember { mutableStateOf("") }
    var raca by remember { mutableStateOf("") }
    var porte by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var morada by remember { mutableStateOf("") }
    var codigoPostal by remember { mutableStateOf("") }
    var localidadeCao by remember { mutableStateOf("") }

    var nomeDono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var localidadeDono by remember { mutableStateOf("") }

    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileImageUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil do Cão", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF6A1B9A))
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                // Foto do Cão
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .clickable { launcher.launch("image/*") }
                        .align(Alignment.CenterHorizontally),
                    contentAlignment = Alignment.Center
                ) {
                    if (profileImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(profileImageUri),
                            contentDescription = "Foto do Cão",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text("Adicionar Foto", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            }

            item {
                InfoCard(title = "Informações do Cão") {
                    CustomField("Nome", nomeCao) { nomeCao = it }
                    CustomField("Raça", raca) { raca = it }
                    CustomField("Porte (Pequeno/Médio/Grande)", porte) { porte = it }
                    CustomField(
                        label = "Peso (kg)",
                        value = peso,
                        keyboard = KeyboardType.Number
                    ) { peso = it }
                    CustomField("Morada", morada) { morada = it }
                    CustomField("Código Postal", codigoPostal) { codigoPostal = it }
                    CustomField("Localidade", localidadeCao) { localidadeCao = it }
                }
            }

            item {
                InfoCard(title = "Informações do Dono") {
                    CustomField("Nome", nomeDono) { nomeDono = it }
                    CustomField(
                        label = "Email",
                        value = email,
                        keyboard = KeyboardType.Email
                    ) { email = it }
                    CustomField(
                        label = "Telefone",
                        value = telefone,
                        keyboard = KeyboardType.Phone
                    ) { telefone = it }
                    CustomField("Localidade", localidadeDono) { localidadeDono = it }
                }
            }

            item {
                Button(
                    onClick = { /* ação de adicionar cão */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A))
                ) {
                    Text("Adicionar Cão", color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }
}
