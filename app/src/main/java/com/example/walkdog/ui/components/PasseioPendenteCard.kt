package com.example.walkdog.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.walkdog.ui.model.PasseioPendenteUi

@Composable
fun PasseioPendenteCard(
    passeio: PasseioPendenteUi,
    onAceitar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // 🐶 Foto do cão + nomes
            Row(verticalAlignment = Alignment.CenterVertically) {

                if (passeio.fotoCaoUrl != null) {
                    AsyncImage(
                        model = passeio.fotoCaoUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFB39DDB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            passeio.nomeCao.firstOrNull()?.uppercase() ?: "",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(passeio.nomePasseio, fontWeight = FontWeight.Bold)
                    Text("Cão: ${passeio.nomeCao}", fontSize = 13.sp)
                }
            }

            HorizontalDivider()

            Text("📍 ${passeio.localidade}")
            Text("🕒 ${passeio.hora}")
            Text("💰 ${passeio.preco}")
            Text("👤 ${passeio.fornecedor}")
            Text("📌 ${passeio.estado}", color = Color(0xFF6A1B9A))

            Spacer(Modifier.height(8.dp))

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
