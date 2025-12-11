package com.example.walkdog.Screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.walkdog.viewmodel.FormularioFornecedorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioFornecedorScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {

    val context = LocalContext.current
    val viewModel: FormularioFornecedorViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    // FOTO
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> profileImageUri = uri }

    // CAMPOS
    var nome by remember { mutableStateOf("") }
    var morada by remember { mutableStateOf("") }
    var codPostal by remember { mutableStateOf("") }
    var localidade by remember { mutableStateOf("") }
    var nif by remember { mutableStateOf("") }
    var iban by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // ERROS
    var nomeErro by remember { mutableStateOf(false) }
    var moradaErro by remember { mutableStateOf(false) }
    var codPostalErro by remember { mutableStateOf(false) }
    var localidadeErro by remember { mutableStateOf(false) }
    var nifErro by remember { mutableStateOf(false) }
    var ibanErro by remember { mutableStateOf(false) }
    var emailErro by remember { mutableStateOf(false) }
    var passwordErro by remember { mutableStateOf(false) }

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
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
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

                InputField(
                    value = email,
                    onValueChange = { email = it; emailErro = false },
                    label = "Email",
                    error = emailErro,
                    keyboardType = KeyboardType.Email
                )

                InputField(
                    value = password,
                    onValueChange = { password = it; passwordErro = false },
                    label = "Password",
                    error = passwordErro,
                    keyboardType = KeyboardType.Password,
                    isPassword = true
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

            // BOTÃO SALVAR
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {

                    nomeErro = nome.isBlank()
                    moradaErro = morada.isBlank()
                    codPostalErro = codPostal.isBlank()
                    localidadeErro = localidade.isBlank()
                    nifErro = nif.isBlank()
                    ibanErro = iban.isBlank()
                    emailErro = email.isBlank()
                    passwordErro = password.isBlank()

                    val valido =
                        !nomeErro && !moradaErro && !codPostalErro &&
                                !localidadeErro && !nifErro && !ibanErro &&
                                !emailErro && !passwordErro

                    if (valido) {
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
                            fotoUri = profileImageUri
                        )

                        onSaveClick()
                    }
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


@Preview(showBackground = true)
@Composable
fun PreviewFormularioFornecedorScreen() {
    FormularioFornecedorScreen()
}