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


                    // PERFIL CLIENTE
                    composable("perfil_cliente") {
                        PerfilClienteScreen(
                            onBackClick = { navController.popBackStack() },
                            onSaveClick = { navController.navigate("login") }
                        )
                    }

                    // PERFIL FORNECEDOR
                    composable("perfil_fornecedor") {
                        PerfilFornecedorScreen(
                            onBackClick = { navController.popBackStack() },
                            /*onContactClick = { /* envio de mensagem */ },
                            onScheduleClick = { /* abrir calendário/agendar */ }*/
                            onSaveClick = { navController.navigate("login") }
                        )
                    }

                    //----------------------------------

                   // FORMULÁRIO CLIENTE
                    composable("formulario_cliente") {
                        FormularioClienteScreen(
                            onBackClick = { navController.popBackStack() },
                            onSaveClick = { navController.navigate("login") }
                        )
                    }

                    // FORMULÁRIO FORNECEDOR
                    composable("formulario_fornecedor") {
                        FormularioFornecedorScreen(
                            onBackClick = { navController.popBackStack() },
                            /*onContactClick = { /* envio de mensagem */ },
                            onScheduleClick = { /* abrir calendário/agendar */ }*/
                            onSaveClick = { navController.navigate("login") }
                        )
                    }

                    // HOME (opcional)
                    composable("login") {
                        LoginPage(navController)
                    }

                }
            }
        }
    }
}

