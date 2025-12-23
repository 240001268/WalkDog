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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.walkdog.viewmodel.FormularioCaoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioCaoScreen(onBackClick: () -> Unit = {}) {

    // 🔹 ViewModel + State
    val viewModel: FormularioCaoViewModel = viewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // 🔹 Campos do cão (UI)
    var nomeCao by remember { mutableStateOf("") }
    var raca by remember { mutableStateOf("") }
    var porte by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var localidadeCao by remember { mutableStateOf("") }

    // 🔹 Foto
    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    // 🔹 Carregar dono automaticamente
    LaunchedEffect(Unit) {
        viewModel.loadDono()
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        fotoUri = uri
    }

    // 🔹 Se sucesso → voltar
    LaunchedEffect(state.success) {
        if (state.success) onBackClick()
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

            // -----------------------
            // FOTO DO CÃO
            // -----------------------
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

            // -----------------------
            // INFO DO CÃO
            // -----------------------
            item {
                InfoCard(title = "Informações do Cão") {
                    CustomField("Nome", nomeCao) { nomeCao = it }
                    CustomField("Raça", raca) { raca = it }
                    CustomField("Porte", porte) { porte = it }
                    CustomField("Peso (kg)", peso, keyboard = KeyboardType.Number) { peso = it }
                    CustomField("Localidade", localidadeCao) { localidadeCao = it }
                }
            }

            // -----------------------
            // INFO DO DONO (STATE)
            // -----------------------
            item {
                InfoCard(title = "Informações do Dono") {

                    Text("Nome", fontWeight = FontWeight.Bold)
                    Text(state.nomeDono)
                    Spacer(Modifier.height(8.dp))

                    Text("Email", fontWeight = FontWeight.Bold)
                    Text(state.emailDono)
                    Spacer(Modifier.height(8.dp))

                    Text("Telefone", fontWeight = FontWeight.Bold)
                    Text(state.telefoneDono)
                    Spacer(Modifier.height(8.dp))

                    Text("Localidade", fontWeight = FontWeight.Bold)
                    Text(state.localidadeDono)
                }
            }

            // -----------------------
            // BOTÃO SALVAR
            // -----------------------
            item {
                Button(
                    onClick = {
                        if (
                            nomeCao.isBlank() ||
                            raca.isBlank() ||
                            porte.isBlank() ||
                            peso.isBlank() ||
                            localidadeCao.isBlank()
                        ) {
                            return@Button
                        }

                        viewModel.registarCao(
                            context = context,
                            nome = nomeCao,
                            raca = raca,
                            porte = porte,
                            peso = peso,
                            localidadeCao = localidadeCao,
                            fotoUri = fotoUri
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    enabled = !state.loading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A))
                ) {
                    if (state.loading) {
                        CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text("Adicionar Cão", fontSize = 18.sp, color = Color.White)
                    }
                }
            }

            // -----------------------
            // ERRO
            // -----------------------
            state.error?.let {
                item {
                    Text(it, color = Color.Red)
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
