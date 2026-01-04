package com.example.walkdog.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.walkdog.viewmodel.MarcarPasseioViewModel
import androidx.compose.material3.MenuAnchorType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarcarPasseioScreen(
    onBackClick: () -> Unit
) {
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

    val roxoFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
        cursorColor = MaterialTheme.colorScheme.primary
    )

    var localidade by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Marcar Passeio") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // -----------------------
            // CÃO
            // -----------------------
            item {
                FormSection(title = "Cão") {
                    DropdownSelector(
                        label = "Selecionar cão",
                        options = state.caes.map { it.data["nome"]?.toString() ?: "Cão" },
                        onSelected = { index ->
                            viewModel.selecionarCao(state.caes[index].id)

                        }
                    )
                }
            }

            // -----------------------
            // PASSEIO
            // -----------------------
            item {
                FormSection(title = "Passeio") {

                    DropdownSelector(
                        label = "Tipo de passeio",
                        options = state.passeios.map {
                            it.data["descricao"]?.toString() ?: "Passeio"
                        },
                        onSelected = { index ->
                            viewModel.selecionarPasseio(state.passeios[index])
                        }
                    )

                    Spacer(Modifier.height(12.dp))

                    // ✅ VALOR DO PASSEIO (NO CONTEXTO CERTO)
                    OutlinedTextField(
                        value = state.precoFinal?.let { "$it €" } ?: "--",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Valor do passeio") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // -----------------------
            // FORNECEDOR
            // -----------------------
            item {
                FormSection(title = "Fornecedor (opcional)") {
                    DropdownSelector(
                        label = "Fornecedor",
                        options = listOf("Sem fornecedor") +
                                state.fornecedores.map {
                                    it.data["nome"]?.toString() ?: "Fornecedor"
                                },
                        onSelected = { index ->
                            if (index == 0) {
                                viewModel.selecionarFornecedor(null)
                            } else {
                                viewModel.selecionarFornecedor(
                                    state.fornecedores[index - 1].id
                                )
                            }
                        }
                    )
                }
            }

            // -----------------------
            // LOCAL / HORA
            // -----------------------
            item {
                FormSection(title = "Detalhes") {
                    OutlinedTextField(
                        value = localidade,
                        onValueChange = { localidade = it },
                        label = { Text("Localidade") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = roxoFieldColors
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = horaInicio,
                        onValueChange = { horaInicio = it },
                        label = { Text("Hora de início") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = roxoFieldColors
                    )
                }
            }

            // -----------------------
            // CONFIRMAR
            // -----------------------
            item {
                Button(
                    onClick = {
                        viewModel.confirmarPasseio(localidade, horaInicio)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.loading &&
                            state.caoSelecionadoId != null &&
                            state.tipoPasseioSelecionado != null
                ) {
                    if (state.loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Confirmar pedido")
                    }
                }
            }

            // -----------------------
            // ERRO
            // -----------------------
            state.error?.let {
                item {
                    Text(it, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
@Composable
fun FormSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(title, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownSelector(
    label: String,
    options: List<String>,
    onSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedLabel by remember { mutableStateOf("") }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedLabel,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        selectedLabel = option
                        onSelected(index)
                        expanded = false
                    }
                )
            }
        }
    }
}
