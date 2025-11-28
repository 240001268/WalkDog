@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.formulariocliente

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter


// -------------------------------------------------------------
// COMPONENTES REUTILIZÁVEIS (IGUAIS AO FORMULÁRIO DO FORNECEDOR)
// -------------------------------------------------------------

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
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
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
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true
    )
    if (error) {
        Text("Campo obrigatório", color = Color.Red, fontSize = 12.sp)
    }
}


// -------------------------------------------------------------
// FORMULÁRIO COMPLETO DO CLIENTE
// -------------------------------------------------------------

@Composable
fun FormularioClienteScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {

    // CAMPOS DO CLIENTE (COM VALIDAÇÃO)
    var nome by remember { mutableStateOf("") }
    var morada by remember { mutableStateOf("") }
    var codPostal by remember { mutableStateOf("") }
    var localidade by remember { mutableStateOf("") }
    var nif by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // PAGAMENTO
    var numeroCartao by remember { mutableStateOf("") }
    var validade by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var iban by remember { mutableStateOf("") }

    // FOTO
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> profileImageUri = uri }

    // ERROS (IGUAL AO FORNECEDOR)
    var nomeErro by remember { mutableStateOf(false) }
    var moradaErro by remember { mutableStateOf(false) }
    var codPostalErro by remember { mutableStateOf(false) }
    var localidadeErro by remember { mutableStateOf(false) }
    var nifErro by remember { mutableStateOf(false) }
    var emailErro by remember { mutableStateOf(false) }
    var passwordErro by remember { mutableStateOf(false) }
    var ibanErro by remember { mutableStateOf(false) }


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
    ) { paddingValues ->


        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // -------------------------------------------------------------
            // FOTO (SEÇÃO A) — Foto no topo da tela
            // -------------------------------------------------------------
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                            contentDescription = "Foto do Cliente",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text("Adicionar Foto", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            }


            // -------------------------------------------------------------
            // INFORMAÇÕES DO CLIENTE (COPIADO E ADAPTADO DO FORNECEDOR)
            // -------------------------------------------------------------
            CardSection(title = "Informações do Cliente") {

                InputField(nome, { nome = it; nomeErro = false }, "Nome", nomeErro)

                InputField(morada, { morada = it; moradaErro = false }, "Morada", moradaErro)

                InputField(codPostal, { codPostal = it; codPostalErro = false }, "Código Postal", codPostalErro)

                InputField(localidade, { localidade = it; localidadeErro = false }, "Localidade", localidadeErro)

                InputField(
                    nif,
                    { nif = it; nifErro = false },
                    "NIF",
                    nifErro,
                    keyboardType = KeyboardType.Number
                )

                InputField(
                    email,
                    { email = it; emailErro = false },
                    "Email",
                    emailErro,
                    keyboardType = KeyboardType.Email
                )

                InputField(
                    password,
                    { password = it; passwordErro = false },
                    "Password",
                    passwordErro,
                    keyboardType = KeyboardType.Password,
                    isPassword = true
                )
            }


            // -------------------------------------------------------------
            // MÉTODO DE PAGAMENTO
            // -------------------------------------------------------------
            CardSection(title = "Método de Pagamento") {

                InputField(
                    numeroCartao,
                    { numeroCartao = it },
                    "Número do Cartão",
                    false,
                    keyboardType = KeyboardType.Number
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InputField(
                        validade,
                        { validade = it },
                        "MM/YY",
                        false,
                        keyboardType = KeyboardType.Number
                    )

                    InputField(
                        cvv,
                        { cvv = it },
                        "CVV",
                        false,
                        keyboardType = KeyboardType.Number,
                        isPassword = true
                    )
                }

                InputField(
                    iban,
                    { iban = it; ibanErro = false },
                    "IBAN",
                    ibanErro
                )
            }


            // -------------------------------------------------------------
            // BOTÃO SALVAR
            // -------------------------------------------------------------
            Button(
                onClick = {

                    // VALIDAÇÕES
                    nomeErro = nome.isBlank()
                    moradaErro = morada.isBlank()
                    codPostalErro = codPostal.isBlank()
                    localidadeErro = localidade.isBlank()
                    nifErro = nif.isBlank()
                    emailErro = email.isBlank()
                    passwordErro = password.isBlank()
                    ibanErro = iban.isBlank()

                    val valido = listOf(
                        nomeErro,
                        moradaErro,
                        codPostalErro,
                        localidadeErro,
                        nifErro,
                        emailErro,
                        passwordErro,
                        ibanErro
                    ).none { it }

                    if (valido) onSaveClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Salvar", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewFormularioClienteScreen() {
    FormularioClienteScreen()
}
