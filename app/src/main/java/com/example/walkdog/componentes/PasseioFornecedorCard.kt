package com.example.walkdog.componentes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PasseioFornecedorCard(
    descricao: String,
    duracaoStr: String,
    precoStr: String,
    onAgendar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text("Descrição: $descricao", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(6.dp))

            Text("Duração: $duracaoStr", fontSize = 15.sp)
            Text("Preço: $precoStr", fontSize = 15.sp, fontWeight = FontWeight.Medium)

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onAgendar,
                colors = ButtonDefaults.buttonColors(Color(0xFF6A1B9A)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agendar Passeio", color = Color.White)
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