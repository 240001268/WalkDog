package com.example.walkdog.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.walkdog.R
import org.w3c.dom.Text

@Preview
@Composable

fun Marcador(valor: String="27", label: String="Total", image:Int=R.drawable.ic_launcher_foreground): Unit {
    Row {
        Image(painter = painterResource(image), modifier = Modifier.size(30.dp), contentDescription = "")

        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.height(20.dp)) {
            Text (valor, color = Color.White, fontSize = 10.sp )
            Text (label , color = Color.White, fontSize = 5.sp)
        }
    }

}
@Preview
@Composable
fun teste(): Unit {
    Column {

        Marcador("12", "Total", R.drawable.ic_launcher_background)
        Marcador("8", "Concluidos",R.drawable.ic_launcher_foreground)
        Marcador("24,5 Km", "Distância")
        Marcador("180 min", "Duração")
    }
}