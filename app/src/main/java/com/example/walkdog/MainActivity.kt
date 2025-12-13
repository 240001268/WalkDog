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
import com.example.walkdog.Screens.*
import com.example.walkdog.model.Cao
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

                        // -------------------------------
                        // SPLASH
                        // -------------------------------
                        composable("splash") {
                            SplashScreen {
                                navController.navigate("login") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        }

                        // -------------------------------
                        // LOGIN
                        // -------------------------------
                        composable("login") {
                            LoginPage(
                                onEntrarCliente = {
                                    navController.navigate("perfil_cliente")
                                },
                                onEntrarFornecedor = { state ->
                                    val fornecedorId = Uri.encode(state.userId)
                                    navController.navigate("perfil_fornecedor/$fornecedorId")
                                },
                                onRegistarCliente = {
                                    navController.navigate("formulario_cliente")
                                },
                                onRegistarFornecedor = {
                                    navController.navigate("formulario_fornecedor")
                                }
                            )
                        }

                        // -------------------------------
                        // PERFIL CLIENTE
                        // -------------------------------
                        composable("perfil_cliente") {
                            PerfilClienteScreen(
                                onRegistarCao = { navController.navigate("formulario_cao") },
                                onBuscarFornecedor = { navController.navigate("buscar_fornecedores") },
                                onMarcarPasseio = { navController.navigate("marcar_passeio") },
                                onCaoClick = { route -> navController.navigate(route) },
                                onHistoricoClick = { navController.navigate("historico_passeios") },
                                onBackClick = { loginVM.logout(navController) }
                            )
                        }

                        // -------------------------------
                        // HISTÓRICO PASSEIOS
                        // -------------------------------
                        composable("historico_passeios") {
                            HistoricoPasseiosScreen(
                                onBackClick = { navController.navigateUp() }
                            )
                        }

                        // -------------------------------
                        // PERFIL FORNECEDOR
                        // -------------------------------
                        composable(
                            route = "perfil_fornecedor/{fornecedorId}",
                            arguments = listOf(navArgument("fornecedorId") {
                                type = NavType.StringType
                            })
                        ) { entry ->

                            val fornecedorId = entry.arguments!!.getString("fornecedorId")!!

                            PerfilFornecedorScreen(
                                userId = fornecedorId,
                                onLogoutClick = {
                                    loginVM.logout(navController)
                                },
                                onEditPerfil = { id ->
                                    navController.navigate("editar_fornecedor/$id")
                                },
                                onEscolherPasseiosClick = { id ->
                                    navController.navigate("escolher_passeios/$id")
                                },
                                onScheduleClick = { tipo, minutos, preco ->
                                    navController.navigate("marcar_passeio/$tipo/$minutos/$preco")
                                }
                            )
                        }

                        // -------------------------------
                        // EDITAR FORNECEDOR
                        // -------------------------------
                        composable(
                            route = "editar_fornecedor/{userId}",
                            arguments = listOf(navArgument("userId") {
                                type = NavType.StringType
                            })
                        ) { entry ->

                            val id = entry.arguments!!.getString("userId")!!

                            EditarFornecedorScreen(
                                userId = id,
                                onBackClick = { navController.navigateUp() }
                            )
                        }

                        // -------------------------------
                        // ESCOLHER PASSEIOS
                        // -------------------------------
                        composable(
                            route = "escolher_passeios/{userId}",
                            arguments = listOf(navArgument("userId") {
                                type = NavType.StringType
                            })
                        ) { entry ->

                            val fornecedorId = entry.arguments!!.getString("userId")!!

                            EscolherPasseiosScreen(
                                userId = fornecedorId,
                                onBackClick = { navController.navigateUp() }
                            )
                        }

                        // -------------------------------
                        // MARCAR PASSEIO
                        // -------------------------------
                        composable(
                            route = "marcar_passeio/{tipo}/{minutos}/{preco}",
                            arguments = listOf(
                                navArgument("tipo") { type = NavType.StringType },
                                navArgument("minutos") { type = NavType.IntType },
                                navArgument("preco") { type = NavType.IntType }
                            )
                        ) { entry ->

                            val tipo = entry.arguments!!.getString("tipo")!!
                            val minutos = entry.arguments!!.getInt("minutos")
                            val preco = entry.arguments!!.getInt("preco")

                            MarcarPasseioScreen(
                                tipoInicial = tipo,
                                minutosIniciais = minutos,
                                onBackClick = { navController.navigateUp() }
                            )
                        }

                        // Página sem parâmetros
                        composable("marcar_passeio") {
                            MarcarPasseioScreen(
                                tipoInicial = "",
                                minutosIniciais = 0,
                                onBackClick = { navController.navigateUp() }
                            )
                        }

                        // -------------------------------
                        // FORMULÁRIOS
                        // -------------------------------
                        composable("formulario_cliente") {
                            FormularioClienteScreen(
                                onBackClick = { navController.navigateUp() },
                                onSaveClick = { navController.navigate("login") }
                            )
                        }

                        composable("formulario_fornecedor") {
                            FormularioFornecedorScreen(
                                onBackClick = { navController.navigateUp() },
                                onSaveClick = { navController.navigate("login") }
                            )
                        }

                        composable("formulario_cao") {
                            FormularioCaoScreen(
                                onBackClick = { navController.navigateUp() }
                            )
                        }

                        // -------------------------------
                        // BUSCAR FORNECEDORES
                        // -------------------------------
                        composable("buscar_fornecedores") {
                            BuscarFornecedoresScreen(
                                onBackClick = { navController.navigateUp() },
                                onVerPerfil = { fornecedorId ->
                                    navController.navigate("perfil_fornecedor/$fornecedorId")
                                }
                            )
                        }

                        // -------------------------------
                        // PERFIL CÃO
                        // -------------------------------
                        composable(
                            route = "perfil_cao/{nome}/{raca}/{porte}/{peso}/{localidade}/{fotoUrl}/{nomeDono}/{emailDono}/{telefoneDono}/{localidadeDono}",
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
                                navArgument("localidadeDono") { type = NavType.StringType }
                            )
                        ) { entry ->

                            val cao = Cao(
                                nome = entry.arguments!!.getString("nome")!!,
                                raca = entry.arguments!!.getString("raca")!!,
                                porte = entry.arguments!!.getString("porte")!!,
                                peso = entry.arguments!!.getString("peso")!!,
                                localidade = entry.arguments!!.getString("localidade")!!,
                                fotoUrl = entry.arguments!!.getString("fotoUrl")!!.ifEmpty { null },
                                nomeDono = entry.arguments!!.getString("nomeDono")!!,
                                emailDono = entry.arguments!!.getString("emailDono")!!,
                                telefoneDono = entry.arguments!!.getString("telefoneDono")!!,
                                localidadeDono = entry.arguments!!.getString("localidadeDono")!!
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
