package com.example.walkdog.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.walkdog.componentes.TopBarcomponent
import com.example.walkdog.componentes.footercomponent

@Preview
@Composable
fun loginpage(): Unit {
    Column {
        TopBarcomponent()
        footercomponent()
    }
}