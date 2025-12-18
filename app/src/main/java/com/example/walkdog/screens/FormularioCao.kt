package com.example.walkdog.screens

import CustomField
import InfoCard
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioCaoScreen(onBackClick: () -> Unit = {}) {

    var nomeCao by remember { mutableStateOf("") }
    var raca by remember { mutableStateOf("") }
    var porte by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var localidadeCao by remember { mutableStateOf("") }

    var nomeDono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefone by remember { mutableStateOf("") }
    var localidadeDono by remember { mutableStateOf("") }

    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        fotoUri = uri
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Adicionar Cão",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
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
    ) { pad ->

        LazyColumn(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .clickable { launcher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (fotoUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(fotoUri),
                            contentDescription = "Foto do cão",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text("Adicionar Foto", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }

            item {
                InfoCard(title = "Informações do Cão") {
                    CustomField("Nome", nomeCao) { nomeCao = it }
                    CustomField("Raça", raca) { raca = it }
                    CustomField("Porte", porte) { porte = it }
                    CustomField("Peso (kg)", peso, keyboard = KeyboardType.Number) { peso = it }
                    CustomField("Localidade", localidadeCao) { localidadeCao = it }
                }
            }

            item {
                InfoCard(title = "Informações do Dono") {
                    CustomField("Nome", nomeDono) { nomeDono = it }
                    CustomField("Email", email, keyboard = KeyboardType.Email) { email = it }
                    CustomField("Telefone", telefone, keyboard = KeyboardType.Phone) { telefone = it }
                    CustomField("Localidade", localidadeDono) { localidadeDono = it }
                }
            }

            item {
                Button(
                    onClick = { /* TODO salvar no Appwrite */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A))
                ) {
                    Text("Adicionar Cão", fontSize = 18.sp, color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFormularioCaoScreen() {
    FormularioCaoScreen()
}
