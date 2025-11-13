package com.example.formulariofornecedor

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FormularioFornecedorScreen(onBackClick: () -> Unit = {}) {

    // Estado da foto de perfil
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para selecionar imagem da galeria
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileImageUri = uri
    }

    // Estados de campos pessoais
    var nome by remember { mutableStateOf("") }
    var morada by remember { mutableStateOf("") }
    var codPostal by remember { mutableStateOf("") }
    var localidade by remember { mutableStateOf("") }
    var nif by remember { mutableStateOf("") }

    // Erros de campos pessoais
    var nomeErro by remember { mutableStateOf(false) }
    var moradaErro by remember { mutableStateOf(false) }
    var codPostalErro by remember { mutableStateOf(false) }
    var localidadeErro by remember { mutableStateOf(false) }
    var nifErro by remember { mutableStateOf(false) }

    // Campo de pagamento (apenas IBAN)
    var iban by remember { mutableStateOf("") }
    var ibanErro by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil do Fornecedor", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(containerColor = Color(0xFF6A1B9A))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🖼️ FOTO DE PERFIL
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(alpha = 0.3f))
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (profileImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(profileImageUri),
                        contentDescription = "Foto de Perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = "Adicionar Foto",
                        tint = Color(0xFF6A1B9A),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            // 🧾 SEÇÃO DE INFORMAÇÕES PESSOAIS
            PersonalCardField("Nome", nome, { nome = it; nomeErro = false }, nomeErro, "Digite o nome completo")
            PersonalCardField("Morada", morada, { morada = it; moradaErro = false }, moradaErro, "Rua, nº, andar...")
            PersonalCardField("Código Postal", codPostal, { codPostal = it; codPostalErro = false }, codPostalErro, "Ex: 1000-200")
            PersonalCardField("Localidade", localidade, { localidade = it; localidadeErro = false }, localidadeErro, "Ex: Lisboa")
            PersonalCardField("NIF", nif, { nif = it; nifErro = false }, nifErro, "Número de Identificação Fiscal", KeyboardType.Number)

            // 💳 SEÇÃO DE PAGAMENTO
            Text(
                "Método de Pagamento",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(start = 4.dp, top = 12.dp, bottom = 4.dp)
                    .align(Alignment.Start)
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = iban,
                        onValueChange = { iban = it; ibanErro = false },
                        label = { Text("IBAN") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = ibanErro
                    )
                    if (ibanErro) Text("IBAN inválido", color = Color.Red, fontSize = 12.sp)
                }
            }

            // 🟣 BOTÃO SALVAR
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    // Validação geral
                    nomeErro = nome.isBlank()
                    moradaErro = morada.isBlank()
                    codPostalErro = codPostal.isBlank()
                    localidadeErro = localidade.isBlank()
                    nifErro = nif.isBlank()
                    ibanErro = iban.isBlank()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A))
            ) {
                Text("Salvar", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun PersonalCardField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: Boolean,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(placeholder) },
                modifier = Modifier.fillMaxWidth(),
                isError = error,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
            )
            if (error) Text("Campo obrigatório", color = Color.Red, fontSize = 12.sp)
        }
    }
}
