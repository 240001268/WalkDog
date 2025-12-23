package com.example.walkdog

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.walkdog.model.Cao
import com.example.walkdog.screens.*
import com.example.walkdog.service.AppwriteService
import com.example.walkdog.ui.theme.WalkDogTheme
import com.example.walkdog.viewmodel.LoginViewModel
import com.example.walkdog.viewmodel.LoginViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppwriteService.init(applicationContext)
        enableEdgeToEdge()

        setContent {
            WalkDogTheme {

                val navController = rememberNavController()
                val loginVM: LoginViewModel = viewModel(
                    factory = LoginViewModelFactory(AppwriteService)
                )

                Surface(color = MaterialTheme.colorScheme.background) {

                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {

                        // -----------------------
                        // SPLASH
                        // -----------------------
                        composable("splash") {
                            SplashScreen {
                                navController.navigate("login") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        }

                        // -----------------------
                        // LOGIN
                        // -----------------------
                        composable("login") {
                            LoginPage(
                                onEntrarCliente = { userId ->
                                    navController.navigate("perfil_cliente/$userId")
                                },
                                onEntrarFornecedor = {
                                    // fornecedor usa user autenticado
                                    navController.navigate("perfil_fornecedor")
                                },
                                onRegistarCliente = {
                                    navController.navigate("formulario_cliente")
                                },
                                onRegistarFornecedor = {
                                    navController.navigate("formulario_fornecedor")
                                }
                            )
                        }

                        // -----------------------
                        // PERFIL CLIENTE
                        // -----------------------
                        composable("perfil_cliente/{clienteId}") { backStackEntry ->
                            val clienteId =
                                backStackEntry.arguments!!.getString("clienteId")!!

                            PerfilClienteScreen(
                                userId = clienteId,
                                onRegistarCao = {
                                    navController.navigate("formulario_cao")
                                },
                                onBuscarFornecedor = {
                                    navController.navigate("buscar_fornecedores")
                                },
                                onMarcarPasseio = {
                                    navController.navigate("marcar_passeio")
                                },
                                onCaoClick = { cao ->
                                    navController.navigate(buildPerfilCaoRoute(cao))
                                },
                                onHistoricoClick = {
                                    navController.navigate("historico_passeios")
                                },
                                onBackClick = {
                                    loginVM.logout(navController)
                                },
                                onEditarCliente = { userId ->
                                    navController.navigate("editar_cliente/$userId")
                                }
                            )
                        }

                        // -----------------------
                        // HISTÓRICO
                        // -----------------------
                        composable("historico_passeios") {
                            HistoricoPasseiosScreen(
                                onBackClick = { navController.navigateUp() }
                            )
                        }

                        // -----------------------
                        // PERFIL FORNECEDOR
                        // -----------------------
                        composable("perfil_fornecedor") {
                            PerfilFornecedorScreen(
                                onLogoutClick = {
                                    loginVM.logout(navController)
                                },
                                onEditPerfil = { id ->
                                    navController.navigate("editar_fornecedor/$id")
                                },
                                onEscolherPasseiosClick = {
                                    navController.navigate("escolher_passeios")
                                },
                                onScheduleClick = { tipo, minutos, preco ->
                                    navController.navigate(
                                        "marcar_passeio/$tipo/$minutos/$preco"
                                    )
                                }
                            )
                        }

                        // -----------------------
                        // EDITAR FORNECEDOR
                        // -----------------------
                        composable(
                            route = "editar_fornecedor/{userId}",
                            arguments = listOf(
                                navArgument("userId") {
                                    type = NavType.StringType
                                }
                            )
                        ) { entry ->
                            val id = entry.arguments!!.getString("userId")!!
                            EditarFornecedorScreen(
                                userId = id,
                                onBackClick = { navController.navigateUp() }
                            )
                        }

                        // -----------------------
                        // EDITAR CLIENTE
                        // -----------------------
                        composable(
                            route = "editar_cliente/{userId}",
                            arguments = listOf(
                                navArgument("userId") {
                                    type = NavType.StringType
                                }
                            )
                        ) { entry ->
                            val id = entry.arguments!!.getString("userId")!!
                            EditarClienteScreen(
                                userId = id,
                                onBackClick = { navController.navigateUp() }
                            )
                        }

                        // -----------------------
                        // ESCOLHER PASSEIOS
                        // -----------------------
                        composable("escolher_passeios") {
                            EscolherPasseiosScreen(
                                onBackClick = { navController.navigateUp() }
                            )
                        }

                        // -----------------------
                        // MARCAR PASSEIO
                        // -----------------------
                        composable("marcar_passeio") {
                            MarcarPasseioScreen(
                                onBackClick = { navController.navigateUp() }
                            )
                        }

                        // -----------------------
                        // FORMULÁRIOS
                        // -----------------------
                        composable("formulario_cliente") {
                            FormularioClienteScreen(
                                onBackClick = { navController.navigateUp() },
                                onSaveClick = {
                                    navController.navigate("login")
                                }
                            )
                        }

                        composable("formulario_fornecedor") {
                            FormularioFornecedorScreen(
                                onBackClick = { navController.navigateUp() },
                                onSaveClick = {
                                    navController.navigate("login")
                                }
                            )
                        }

                        composable("formulario_cao") {
                            FormularioCaoScreen(
                                onBackClick = { navController.navigateUp() }
                            )
                        }

                        // -----------------------
                        // BUSCAR FORNECEDORES
                        // -----------------------
                        composable("buscar_fornecedores") {
                            BuscarFornecedoresScreen(
                                onBackClick = { navController.navigateUp() },
                                onVerPerfil = {
                                    navController.navigate("perfil_fornecedor")
                                }
                            )
                        }

                        // -----------------------
                        // PERFIL CÃO (LEGADO)
                        // -----------------------
                        composable(
                            route =
                                "perfil_cao/{nome}/{raca}/{porte}/{peso}/{localidade}/{fotoUrl}/{nomeDono}/{emailDono}/{telefoneDono}/{localidadeDono}",
                            arguments = listOf(
                                navArgument("nome") { type = NavType.StringType },
                                navArgument("raca") { type = NavType.StringType },
                                navArgument("porte") { type = NavType.StringType },
                                navArgument("peso") { type = NavType.StringType },
                                navArgument("localidade") { type = NavType.StringType },
                                navArgument("fotoUrl") { type = NavType.StringType },
                                navArgument("nomeDono") { type = NavType.StringType },
                                navArgument("emailDono") { type = NavType.StringType },
                                navArgument("telefoneDono") { type = NavType.StringType },
                                navArgument("localidadeDono") {
                                    type = NavType.StringType
                                }
                            )
                        ) { entry ->

                            val cao = Cao(
                                nome = Uri.decode(
                                    entry.arguments!!.getString("nome")!!
                                ),
                                raca = Uri.decode(
                                    entry.arguments!!.getString("raca")!!
                                ),
                                porte = Uri.decode(
                                    entry.arguments!!.getString("porte")!!
                                ),
                                peso = Uri.decode(
                                    entry.arguments!!.getString("peso")!!
                                ),
                                localidade = Uri.decode(
                                    entry.arguments!!.getString("localidade")!!
                                ),
                                fotoUrl = Uri.decode(
                                    entry.arguments!!.getString("fotoUrl")!!
                                ).ifEmpty { null },
                                nomeDono = Uri.decode(
                                    entry.arguments!!.getString("nomeDono")!!
                                ),
                                emailDono = Uri.decode(
                                    entry.arguments!!.getString("emailDono")!!
                                ),
                                telefoneDono = Uri.decode(
                                    entry.arguments!!.getString("telefoneDono")!!
                                ),
                                localidadeDono = Uri.decode(
                                    entry.arguments!!.getString("localidadeDono")!!
                                )
                            )

                            PerfilCaoScreen(
                                cao = cao,
                                onBackClick = { navController.navigateUp() }
                            )
                        }
                    }
                }
            }
        }
    }
}
