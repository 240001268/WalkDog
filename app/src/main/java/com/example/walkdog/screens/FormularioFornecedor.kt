@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.walkdog.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.Text
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
import com.example.walkdog.viewmodel.FormularioFornecedorViewModel
import com.example.walkdog.ui.components.InputField
import com.example.walkdog.ui.components.CardSection

/* ---------------- INPUT FIELD ---------------- */

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
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
            else
                VisualTransformation.None,
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible)
                            Icons.Default.Visibility
                        else
                            Icons.Default.VisibilityOff,
                        contentDescription = "Mostrar password"
                    )
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}

/* ---------------- SCREEN ---------------- */

@Composable
fun FormularioFornecedorScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    viewModel: FormularioFornecedorViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    var nome by remember { mutableStateOf("") }
    var morada by remember { mutableStateOf("") }
    var codPostal by remember { mutableStateOf("") }
    var localidade by remember { mutableStateOf("") }
    var nif by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var iban by remember { mutableStateOf("") }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            fotoUri = it
        }
    }

    var erroDialog by remember { mutableStateOf<List<String>?>(null) }

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

    LaunchedEffect(state.success) {
        if (state.success) {

            Toast.makeText(
                context,
                "Fornecedor registado com sucesso!",
                Toast.LENGTH_SHORT
            ).show()

            // LIMPAR FORMULÁRIO
            nome = ""
            morada = ""
            codPostal = ""
            localidade = ""
            nif = ""
            email = ""
            password = ""
            iban = ""
            fotoUri = null
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registar Fornecedor", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
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

        if (state.loading) {
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
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                            .clickable { imagePicker.launch(arrayOf("image/*")) },
                        contentAlignment = Alignment.Center
                    ) {
                        if (fotoUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(fotoUri),
                                contentDescription = "Foto do fornecedor",
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

                /* -------- DADOS -------- */
                CardSection("Informações do Fornecedor") {

                    InputField(
                        value = nome,
                        onValueChange = { nome = it },
                        label = "Nome"
                    )

                    InputField(
                        value = morada,
                        onValueChange = { morada = it },
                        label = "Morada"
                    )

                    InputField(
                        value = codPostal,
                        onValueChange = { codPostal = it },
                        label = "Código Postal"
                    )

                    InputField(
                        value = localidade,
                        onValueChange = { localidade = it },
                        label = "Localidade"
                    )

                    InputField(
                        value = nif,
                        onValueChange = { nif = it },
                        label = "NIF",
                        keyboardType = KeyboardType.Number
                    )

                    InputField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email",
                        keyboardType = KeyboardType.Email
                    )

                    InputField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        keyboardType = KeyboardType.Password,
                        isPassword = true
                    )
                }

                /* -------- PAGAMENTO -------- */
                CardSection("Dados Bancários") {
                    InputField(
                        value = iban,
                        onValueChange = { iban = it },
                        label = "IBAN"
                    )
                }

                Button(
                    onClick = {

                        val erros = mutableListOf<String>()

                        if (nome.isBlank()) erros.add("Nome")
                        if (email.isBlank()) erros.add("Email")
                        if (password.isBlank()) erros.add("Password")



                        if (erros.isNotEmpty()) {
                            erroDialog = erros
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
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6A1B9A)
                    )
                ) {
                    Text(
                        text = "Registar Fornecedor",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}
