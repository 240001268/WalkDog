@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.walkdog.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.walkdog.viewmodel.FormularioClienteViewModel

/* ---------------- INPUT FIELD ---------------- */

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation =
            if (isPassword && !passwordVisible)
                PasswordVisualTransformation()
            else VisualTransformation.None,
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector =
                            if (passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                        contentDescription = null
                    )
                }
            }
        },
        modifier = modifier.fillMaxWidth()
    )
}

/* ---------------- CARD SECTION ---------------- */

@Composable
fun CardSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF6A1B9A)
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

/* ---------------- SCREEN ---------------- */

@Composable
fun FormularioClienteScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    viewModel: FormularioClienteViewModel = viewModel()
) {
    val uiState by viewModel.state.collectAsState()
    val context = LocalContext.current

    var nome by remember { mutableStateOf("") }
    var morada by remember { mutableStateOf("") }
    var codPostal by remember { mutableStateOf("") }
    var localidade by remember { mutableStateOf("") }
    var nif by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var numeroCartao by remember { mutableStateOf("") }
    var validade by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var iban by remember { mutableStateOf("") }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri -> fotoUri = uri }

    var erroDialog by remember { mutableStateOf<List<String>?>(null) }

    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            onSaveClick()
            viewModel.resetState()
        }
    }

    erroDialog?.let { campos ->
        AlertDialog(
            onDismissRequest = { erroDialog = null },
            title = { Text("Campos obrigatórios") },
            text = {
                Text(
                    "Preenche os seguintes campos:\n\n" +
                            campos.joinToString("\n") { "• $it" }
                )
            },
            confirmButton = {
                Button(onClick = { erroDialog = null }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registar Cliente", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6A1B9A)
                )
            )
        }
    ) { padding ->

        if (uiState.loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                /* -------- FOTO -------- */
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
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

                    Column {
                        Text("Foto de Perfil", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Toque para adicionar", color = Color.Gray, fontSize = 14.sp)
                    }
                }

                /* -------- INFORMAÇÕES -------- */
                CardSection("Informações do Cliente") {

                    InputField(value = nome, onValueChange = { nome = it }, label = "Nome")
                    InputField(value = morada, onValueChange = { morada = it }, label = "Morada")
                    InputField(value = codPostal, onValueChange = { codPostal = it }, label = "Código Postal")
                    InputField(value = localidade, onValueChange = { localidade = it }, label = "Localidade")
                    InputField(value = nif, onValueChange = { nif = it }, label = "NIF", keyboardType = KeyboardType.Number)
                    InputField(value = email, onValueChange = { email = it }, label = "Email", keyboardType = KeyboardType.Email)
                    InputField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        keyboardType = KeyboardType.Password,
                        isPassword = true
                    )
                }

                CardSection("Método de Pagamento") {

                    InputField(value = numeroCartao, onValueChange = { numeroCartao = it }, label = "Número do Cartão")

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        InputField(
                            value = validade,
                            onValueChange = { validade = it },
                            label = "MM/YY",
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.weight(1f)
                        )
                        InputField(
                            value = cvv,
                            onValueChange = { cvv = it },
                            label = "CVV",
                            keyboardType = KeyboardType.Number,
                            isPassword = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    InputField(value = iban, onValueChange = { iban = it }, label = "IBAN")
                }

                Button(
                    onClick = {
                        val erros = mutableListOf<String>()

                        if (nome.isBlank()) erros.add("Nome")
                        if (morada.isBlank()) erros.add("Morada")
                        if (email.isBlank()) erros.add("Email")
                        if (password.isBlank()) erros.add("Password")
                        if (iban.isBlank()) erros.add("IBAN")

                        if (erros.isNotEmpty()) {
                            erroDialog = erros
                            return@Button
                        }

                        viewModel.salvarCliente(
                            context = context,
                            nome = nome,
                            morada = morada,
                            codPostal = codPostal,
                            localidade = localidade,
                            NIF = nif,
                            email = email,
                            password = password,
                            numeroCartao = numeroCartao,
                            validade = validade,
                            cvv = cvv,
                            iban = iban,
                            fotoUri = fotoUri
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A))
                ) {
                    Text("Registar Cliente", color = Color.White, fontSize = 18.sp)
                }
            }
        }
    }
}
