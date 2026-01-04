package com.example.walkdog.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.walkdog.ui.components.PasseioPendenteCard
import com.example.walkdog.viewmodel.PasseiosPendentesPorTipoViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasseiosPendentesPorTipoScreen(
    passeiotipoId: String,
    onBackClick: () -> Unit
) {
    val viewModel: PasseiosPendentesPorTipoViewModel = viewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(passeiotipoId) {
        viewModel.loadPasseiosPendentes(passeiotipoId)
    }

    LaunchedEffect(state.aceiteComSucesso) {
        if (state.aceiteComSucesso) {
            viewModel.resetAceiteFlag()
            onBackClick()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Passeios Pendentes", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            if (state.loading) CircularProgressIndicator()

            state.error?.let {
                Text(it, color = Color.Red)
            }

            if (!state.loading && state.passeios.isEmpty()) {
                Text("Não existem mais passeios pendentes deste tipo.")
            }

            state.passeios.forEach { passeio ->
                PasseioPendenteCard(
                    passeio = passeio,
                    onAceitar = {
                        viewModel.aceitarPasseio(passeio.id)
                    }
                )
            }
        }
    }
}