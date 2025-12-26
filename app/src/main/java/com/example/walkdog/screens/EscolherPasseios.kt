package com.example.walkdog.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.walkdog.viewmodel.EscolherPasseiosViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EscolherPasseiosScreen(
    onBackClick: () -> Unit,
    viewModel: EscolherPasseiosViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // Carregar dados ao entrar
    LaunchedEffect(Unit) {
        viewModel.loadPasseios()
    }

    // Sucesso ao salvar
    LaunchedEffect(state.success) {
        if (state.success) {
            Toast.makeText(context, "Passeios atualizados!", Toast.LENGTH_SHORT).show()
            onBackClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escolher Passeios") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            if (state.loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
                return@Column
            }

            if (state.error != null) {
                Text("Erro: ${state.error}", color = MaterialTheme.colorScheme.error)
                return@Column
            }

            // Lista de passeios
            state.todosPasseios.forEach { doc ->

                val passeioId = doc.id
                val descricao = doc.data["descricao"]?.toString() ?: "Sem descrição"
                val preco = doc.data["preco"]?.toString() ?: ""
                val duracao = doc.data["duracao"]?.toString() ?: ""

                val checked = state.selecionados.contains(passeioId)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .toggleable(
                            value = checked,
                            onValueChange = { viewModel.togglePasseio(passeioId) }
                        ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(descricao, style = MaterialTheme.typography.titleMedium)
                            Text("Duração: $duracao")
                            Text("Preço: $preco")
                        }

                        Checkbox(
                            checked = checked,
                            onCheckedChange = {
                                viewModel.togglePasseio(passeioId)
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { viewModel.salvar() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Seleção")
            }
        }
    }
}
