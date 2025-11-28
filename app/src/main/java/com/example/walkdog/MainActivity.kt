package com.example.walkdog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.formulariocao.FormularioCaoScreen
import com.example.formulariocliente.FormularioClienteScreen
import com.example.formulariofornecedor.FormularioFornecedorScreen
import com.example.walkdog.Screens.*
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

                    /* ------------------- SPLASH SCREEN ------------------- */
                    composable("splash") {
                        SplashScreen {
                            navController.navigate("login") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    }

                    /* --------------------- LOGIN ------------------------ */
                    composable("login") {
                        LoginPage(
                            onEntrarCliente = { navController.navigate("perfil_cliente") },
                            onEntrarFornecedor = { navController.navigate("perfil_fornecedor") },
                            onRegistarCliente = { navController.navigate("formulario_cliente") },
                            onRegistarFornecedor = { navController.navigate("formulario_fornecedor") }
                        )
                    }

                    /* ------------------ PERFIL CLIENTE ------------------- */
                    composable("perfil_cliente") {
                        PerfilClienteScreen(
                            onRegistarCao = { navController.navigate("formulario_cao") },
                            onBuscarFornecedor = { navController.navigate("buscar_fornecedores") },
                            onOutro = { navController.navigate("outro") },
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    /* ----------------- PERFIL FORNECEDOR ---------------- */
                    composable("perfil_fornecedor") {
                        PerfilFornecedorScreen(
                            onBackClick = { navController.popBackStack() },
                            onSaveClick = { navController.navigate("login") }
                        )
                    }

                    /* ------------ FORMULÁRIO CLIENTE ------------------- */
                    composable("formulario_cliente") {
                        FormularioClienteScreen(
                            onBackClick = { navController.popBackStack() },
                            onSaveClick = { navController.navigate("login") }
                        )
                    }

                    /* ------------ FORMULÁRIO FORNECEDOR ---------------- */
                    composable("formulario_fornecedor") {
                        FormularioFornecedorScreen(
                            onBackClick = { navController.popBackStack() },
                            onSaveClick = { navController.navigate("login") }
                        )
                    }

                    /* ---------------- FORMULÁRIO CÃO ------------------ */
                    composable("formulario_cao") {
                        FormularioCaoScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    /* ---------------- BUSCAR FORNECEDORES -------------- */
                    composable("buscar_fornecedores") {
                        BuscarFornecedoresScreen(
                            onBackClick = { navController.popBackStack() },
                            onVerPerfil = {}
                        )
                    }

                    /* -------------------- OUTRO SCREEN ------------------ */
                    composable("outro") {
                        OutroScreen(
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
