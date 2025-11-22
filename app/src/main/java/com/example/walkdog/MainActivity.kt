package com.example.walkdog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.formulariocliente.FormularioClienteScreen
import com.example.formulariofornecedor.FormularioFornecedorScreen
import com.example.walkdog.Screens.LoginPage
import com.example.walkdog.Screens.PerfilClienteScreen
import com.example.walkdog.Screens.PerfilFornecedorScreen
import com.example.walkdog.ui.theme.WalkDogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            WalkDogTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "splash"
                ) {

                    // SPLASH
                    composable("splash") {
                        SplashScreen {
                            navController.navigate("login") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    }

                    // LOGIN
                    composable("login") {
                        LoginPage(navController)
                    }


                    // BOTÃO ENTRAR CLIENTE
                    Button(
                        onClick = { navController.navigate("perfil-cliente") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Entrar como Cliente")
                    }
                    
                    // BOTÃO ENTRAR FORNECEDOR
                    Button(
                        onClick = { navController.navigate("perfil_fornecedor") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Entrar como Fornecedor")
                    }
                    
                    // BOTÃO REGISTAR CLIENTE
                    OutlinedButton(
                        onClick = { navController.navigate("formulario_cliente") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Registar Cliente")
                    }
                    
                    // BOTÃO REGISTAR FORNECEDOR
                    OutlinedButton(
                        onClick = { navController.navigate("formulario_fornecedor") },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Registar Fornecedor")
                    }

                }
            }
        }
    }
}

