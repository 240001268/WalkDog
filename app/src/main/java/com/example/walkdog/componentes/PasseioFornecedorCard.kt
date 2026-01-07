package com.example.walkdog.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PasseioFornecedorCard(
    descricao: String,
    duracaoStr: String,
    precoStr: String,
    enabled: Boolean,
    onAgendar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(Modifier.padding(16.dp)) {

            Text(descricao, fontWeight = FontWeight.Bold)
            Text("Duração: $duracaoStr")
            Text("Preço: $precoStr")

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onAgendar,
                enabled = enabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6A1B9A) // 👈 MESMA COR DO "VER MEUS PASSEIOS"
                )
            ) {
                Text(
                    "Agendar Pendentes",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun PasseioPendenteCard(
    descricao: String,
    localidade: String,
    hora: String,
    preco: String,
    onAceitar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Text(descricao, fontWeight = FontWeight.Bold)

            Text("📍 $localidade")
            Text("⏰ $hora")
            Text("💶 € $preco")

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onAceitar,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                Text("Aceitar Passeio", color = Color.White)
            }
        }
    }
}