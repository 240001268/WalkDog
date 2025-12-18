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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarcarPasseioScreen(
    tipoInicial: String = "",
    minutosIniciais: Int = 0,
    onBackClick: () -> Unit = {},
    dogs: List<String> = listOf("Rex", "Bobby", "Luna")
) {

    // -------------------- ESTADOS CORRIGIDOS --------------------

    var selectedDog by remember { mutableStateOf("") }
    var localidade by remember { mutableStateOf("") }
    var horaInicio by remember { mutableStateOf("") }

    // duração (Rápido / Longo)
    var duracao by remember { mutableStateOf(tipoInicial.ifEmpty { "Rápido" }) }

    // tempo selecionado (30,60,90,120)
    var tempoSelecionado by remember {
        mutableStateOf(if (minutosIniciais > 0) "minutosIniciais min" else "")
    }

    // tipo de passeio (Individual / Grupo)
    var tipoPasseio by remember { mutableStateOf("Individual") }

    // -------------------- TABELA DE PREÇOS --------------------

    val opcoesPrecoRapido = mapOf(
        "30 min" to "12€",
        "60 min" to "20€"
    )

    val opcoesPrecoLongo = mapOf(
        "90 min" to "30€",
        "120 min" to "45€"
    )

    val tempoOptions =
        if (duracao == "Rápido") opcoesPrecoRapido else opcoesPrecoLongo

    val precoFinal = tempoOptions[tempoSelecionado] ?: "--"

    // -------------------- UI --------------------

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

            // ------------------ SELECIONAR CÃO ------------------
            var expandedDog by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expandedDog,
                onExpandedChange = { expandedDog = !expandedDog }
            ) {
                OutlinedTextField(
                    value = selectedDog,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Selecionar Cão") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedDog) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                )
                ExposedDropdownMenu(
                    expanded = expandedDog,
                    onDismissRequest = { expandedDog = false }
                ) {
                    dogs.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                selectedDog = it
                                expandedDog = false
                            }
                        )
                    }
                }
            }

            // ------------------ LOCALIDADE ------------------
            OutlinedTextField(
                value = localidade,
                onValueChange = { localidade = it },
                label = { Text("Localidade") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // ------------------ HORA ------------------
            OutlinedTextField(
                value = horaInicio,
                onValueChange = { horaInicio = it },
                label = { Text("Hora Início (ex: 17:00)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            // ------------------ DURAÇÃO ------------------
            var expandedDuracao by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expandedDuracao,
                onExpandedChange = { expandedDuracao = !expandedDuracao }
            ) {
                OutlinedTextField(
                    value = duracao,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Duração do Passeio") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedDuracao) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedDuracao,
                    onDismissRequest = { expandedDuracao = false }
                ) {
                    listOf("Rápido", "Longo").forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                duracao = it
                                tempoSelecionado = ""
                                expandedDuracao = false
                            }
                        )
                    }
                }
            }

            // ------------------ TEMPO ------------------
            var expandedTempo by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expandedTempo,
                onExpandedChange = { expandedTempo = !expandedTempo }
            ) {
                OutlinedTextField(
                    value = tempoSelecionado,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tempo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedTempo) },
                    modifier = Modifier
                       .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedTempo,
                    onDismissRequest = { expandedTempo = false }
                ) {
                    tempoOptions.keys.forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                tempoSelecionado = it
                                expandedTempo = false
                            }
                        )
                    }
                }
            }

            // ------------------ TIPO DE PASSEIO ------------------
            var expandedTipo by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expandedTipo,
                onExpandedChange = { expandedTipo = !expandedTipo }
            ) {
                OutlinedTextField(
                    value = tipoPasseio,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de Passeio") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedTipo) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(
                    expanded = expandedTipo,
                    onDismissRequest = { expandedTipo = false }
                ) {
                    listOf("Individual", "Grupo").forEach {
                        DropdownMenuItem(
                            text = { Text(it) },
                            onClick = {
                                tipoPasseio = it
                                expandedTipo = false
                            }
                        )
                    }
                }
            }

            // ------------------ PREÇO + CONFIRMAR ------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column {
                    Text("Preço do Passeio", color = Color.Gray, fontSize = 14.sp)
                    Text(
                        precoFinal,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6A1B9A)
                    )
                }

                Button(
                    onClick = {
                        // TODO: Gravar no Appwrite
                    },
                    enabled = precoFinal != "--",
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6A1B9A)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Confirmar", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMarcarPasseioScreen() {
    MarcarPasseioScreen()
}
