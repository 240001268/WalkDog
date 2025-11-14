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
@Composable
@Preview
fun FormularioFornecedorScreen(onBackClick: () -> Unit = {}) {

    // Foto
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> profileImageUri = uri }

    // Campos
    var nome by remember { mutableStateOf("") }
    var morada by remember { mutableStateOf("") }
    var codPostal by remember { mutableStateOf("") }
    var localidade by remember { mutableStateOf("") }
    var nif by remember { mutableStateOf("") }
    var iban by remember { mutableStateOf("") }

    // Erros
    var nomeErro by remember { mutableStateOf(false) }
    var moradaErro by remember { mutableStateOf(false) }
    var codPostalErro by remember { mutableStateOf(false) }
    var localidadeErro by remember { mutableStateOf(false) }
    var nifErro by remember { mutableStateOf(false) }
    var ibanErro by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil do Fornecedor", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6A1B9A))
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // FOTO DO FORNECEDOR
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (profileImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(profileImageUri),
                        contentDescription = "Foto",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text("Adicionar Foto", color = Color.Gray, fontSize = 14.sp)
                }
            }

            // INFORMAÇÕES DO FORNECEDOR
            CardSection(title = "Informações do Fornecedor") {

                InputField(
                    value = nome,
                    onValueChange = { nome = it; nomeErro = false },
                    label = "Nome",
                    error = nomeErro
                )

                InputField(
                    value = morada,
                    onValueChange = { morada = it; moradaErro = false },
                    label = "Morada",
                    error = moradaErro
                )

                InputField(
                    value = codPostal,
                    onValueChange = { codPostal = it; codPostalErro = false },
                    label = "Código Postal",
                    error = codPostalErro
                )

                InputField(
                    value = localidade,
                    onValueChange = { localidade = it; localidadeErro = false },
                    label = "Localidade",
                    error = localidadeErro
                )

                InputField(
                    value = nif,
                    onValueChange = { nif = it; nifErro = false },
                    label = "NIF",
                    error = nifErro,
                    keyboardType = KeyboardType.Number
                )
            }

            // MÉTODO DE PAGAMENTO
            CardSection(title = "Método de Pagamento") {
                InputField(
                    value = iban,
                    onValueChange = { iban = it; ibanErro = false },
                    label = "IBAN",
                    error = ibanErro
                )
            }

            // BOTÃO
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {
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
fun CardSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: Boolean,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        isError = error,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true
    )
    if (error) {
        Text("Campo obrigatório", color = Color.Red, fontSize = 12.sp)
    }
}
