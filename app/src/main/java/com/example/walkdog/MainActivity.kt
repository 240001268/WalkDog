package com.example.walkdog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.walkdog.Screens.*
import com.example.walkdog.service.AppwriteService
import com.example.walkdog.ui.theme.WalkDogTheme
import com.example.walkdog.viewmodel.LoginViewModel
import com.example.walkdog.viewmodel.LoginViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Inicializar uma única vez, com applicationContext (evita leaks/instabilidade)
        AppwriteService.init(applicationContext)

        enableEdgeToEdge()

        setContent {
            WalkDogTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val viewModel: LoginViewModel = viewModel(
                        factory = LoginViewModelFactory(AppwriteService)
                    )

                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {
                        composable("splash") {
                            SplashScreen {
                                navController.navigate("login") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        }
                        composable("login") {
                            LoginPage(
                                onEntrarCliente = { navController.navigate("perfil_cliente") },
                                onEntrarFornecedor = { navController.navigate("perfil_fornecedor") },
                                onRegistarCliente = { navController.navigate("formulario_cliente") },
                                onRegistarFornecedor = { navController.navigate("formulario_fornecedor") }
                            )
                        }
                        composable("perfil_cliente") {
                            PerfilClienteScreen(
                                onRegistarCao = { navController.navigate("formulario_cao") },
                                onBuscarFornecedor = { navController.navigate("buscar_fornecedores") },
                                onOutro = { navController.navigate("outro") },
                                onBackClick = { viewModel.logout(navController) }
                            )
                        }
                        composable("perfil_fornecedor") {
                            PerfilFornecedorScreen(
                                onBackClick = { viewModel.logout(navController) },
                                onSaveClick = { navController.navigate("login") }
                            )
                        }
                        composable("formulario_cliente") {
                            FormularioClienteScreen(
                                onBackClick = { navController.popBackStack() },
                                onSaveClick = { navController.navigate("login") }
                            )
                        }
                        composable("formulario_fornecedor") {
                            FormularioFornecedorScreen(
                                onBackClick = { navController.popBackStack() },
                                onSaveClick = { navController.navigate("login") }
                            )
                        }
                        composable("formulario_cao") {
                            FormularioCaoScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                        composable("buscar_fornecedores") {
                            BuscarFornecedoresScreen(
                                onBackClick = { navController.popBackStack() },
                                onVerPerfil = {}
                            )
                        }
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
}
