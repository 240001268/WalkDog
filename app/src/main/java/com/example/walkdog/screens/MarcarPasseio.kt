package com.example.walkdog.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.walkdog.viewmodel.MarcarPasseioViewModel
import androidx.compose.material3.MenuAnchorType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarcarPasseioScreen(
    onBackClick: () -> Unit = {}
) {
    // --------------------------------
    // VIEWMODEL
    // --------------------------------
    val viewModel: MarcarPasseioViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCaesCliente()
        viewModel.loadPasseios()
        viewModel.loadFornecedores()
    }

    LaunchedEffect(state.success) {
        if (state.success) onBackClick()
    }

    // --------------------------------
    // CAMPOS LOCAIS
    // --------------------------------
    var localidade by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }
    var caoNome by remember { mutableStateOf("") }

    // --------------------------------
    // UI
    // --------------------------------
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Marcar Passeio",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6A1B9A)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // --------------------------------------------------
            // SELECIONAR CÃO
            // --------------------------------------------------
            var expandedDog by remember { mutableStateOf(false) }
            var selectedDogLabel by remember { mutableStateOf("") }

            ExposedDropdownMenuBox(
                expanded = expandedDog,
                onExpandedChange = { expandedDog = !expandedDog }
            ) {
                OutlinedTextField(
                    value = selectedDogLabel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Selecionar Cão") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expandedDog)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = expandedDog,
                    onDismissRequest = { expandedDog = false }
                ) {
                    state.caes.forEach { doc ->
                        val nome = doc.data["nome"]?.toString() ?: ""

                        DropdownMenuItem(
                            text = { Text(nome) },
                            onClick = {
                                selectedDogLabel = nome
                                caoNome = nome
                                viewModel.selecionarCao(doc.id)
                                expandedDog = false
                            }
                        )
                    }
                }
            }

            // --------------------------------------------------
            // LOCALIDADE
            // --------------------------------------------------
            OutlinedTextField(
                value = localidade,
                onValueChange = { localidade = it },
                label = { Text("Localidade") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // --------------------------------------------------
            // HORA INÍCIO
            // --------------------------------------------------
            OutlinedTextField(
                value = horaInicio,
                onValueChange = { horaInicio = it },
                label = { Text("Hora Início (ex: 17:00)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // --------------------------------------------------
            // SELECIONAR PASSEIO
            // --------------------------------------------------
            var expandedPasseio by remember { mutableStateOf(false) }
            var passeioLabel by remember { mutableStateOf("") }

            ExposedDropdownMenuBox(
                expanded = expandedPasseio,
                onExpandedChange = { expandedPasseio = !expandedPasseio }
            ) {
                OutlinedTextField(
                    value = passeioLabel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Passeio") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expandedPasseio)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = expandedPasseio,
                    onDismissRequest = { expandedPasseio = false }
                ) {
                    state.passeios.forEach { doc ->
                        val descricao = doc.data["descricao"]?.toString() ?: ""
                        val duracao = doc.data["duracao"]?.toString() ?: ""
                        val preco = doc.data["preco"]?.toString() ?: ""

                        DropdownMenuItem(
                            text = { Text("$descricao - $duracao (€$preco)") },
                            onClick = {
                                passeioLabel = "$descricao - $duracao"
                                viewModel.selecionarPasseio(doc)
                                expandedPasseio = false
                            }
                        )
                    }
                }
            }

            // --------------------------------------------------
            // SELECIONAR FORNECEDOR
            // --------------------------------------------------
            var expandedFornecedor by remember { mutableStateOf(false) }
            var fornecedorLabel by remember { mutableStateOf("") }

            ExposedDropdownMenuBox(
                expanded = expandedFornecedor,
                onExpandedChange = { expandedFornecedor = !expandedFornecedor }
            ) {
                OutlinedTextField(
                    value = fornecedorLabel,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fornecedor") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expandedFornecedor)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = expandedFornecedor,
                    onDismissRequest = { expandedFornecedor = false }
                ) {
                    state.fornecedores.forEach { doc ->
                        val nome = doc.data["nome"]?.toString() ?: ""
                        val localidadeFornecedor =
                            doc.data["localidade"]?.toString() ?: ""

                        DropdownMenuItem(
                            text = { Text("$nome - $localidadeFornecedor") },
                            onClick = {
                                fornecedorLabel = "$nome - $localidadeFornecedor"
                                viewModel.selecionarFornecedor(doc.id)
                                expandedFornecedor = false
                            }
                        )
                    }
                }
            }

            // --------------------------------------------------
            // PREÇO + CONFIRMAR
            // --------------------------------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Text("Preço do Passeio", color = Color.Gray)
                    Text(
                        if (state.precoFinal.isBlank()) "--" else state.precoFinal,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6A1B9A)
                    )
                }

                Button(
                    onClick = {
                        viewModel.confirmarPasseio(
                            caoNome = caoNome,
                            localidade = localidade,
                            horaInicio = horaInicio
                        )
                    },
                    enabled =
                        state.precoFinal.isNotBlank() &&
                                state.fornecedorSelecionadoId != null &&
                                !state.loading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6A1B9A)
                    )
                ) {
                    Text("Confirmar", color = Color.White)
                }
            }

            // --------------------------------------------------
            // ERRO
            // --------------------------------------------------
            state.error?.let {
                Text(it, color = Color.Red)
            }
        }
    }
}
