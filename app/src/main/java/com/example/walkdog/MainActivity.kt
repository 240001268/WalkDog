package com.example.walkdog

import android.os.Bundle
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
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
                val viewModel: LoginViewModel = viewModel(
                    factory = LoginViewModelFactory(AppwriteService)
                )

                Surface(color = MaterialTheme.colorScheme.background) {

                    NavHost(
                        navController = navController,
                        startDestination = "splash"
                    ) {

                        // ------------------------------------------------
                        // SPLASH SCREEN
                        // ------------------------------------------------
                        composable("splash") {
                            SplashScreen {
                                navController.navigate("login") {
                                    popUpTo("splash") { inclusive = true }
                                }
                            }
                        }

                        // ------------------------------------------------
                        // LOGIN
                        // ------------------------------------------------
                        composable("login") {
                            LoginPage(
                                onEntrarCliente = {
                                    navController.navigate("perfil_cliente")
                                },
                                onEntrarFornecedor = {
                                    // Encoded supplier route
                                    val nome = Uri.encode("Carlos Andrade")
                                    val localidade = Uri.encode("Lisboa")
                                    val rating = Uri.encode("4.9")
                                    navController.navigate("perfil_fornecedor/$nome/$localidade/$rating")
                                },
                                onRegistarCliente = { navController.navigate("formulario_cliente") },
                                onRegistarFornecedor = { navController.navigate("formulario_fornecedor") }
                            )
                        }

                        // ------------------------------------------------
                        // PERFIL CLIENTE
                        // ------------------------------------------------
                        composable("perfil_cliente") {
                            PerfilClienteScreen(
                                onRegistarCao = { navController.navigate("formulario_cao") },
                                onBuscarFornecedor = { navController.navigate("buscar_fornecedores") },
                                onMarcarPasseio = { navController.navigate("marcar_passeio") },

                                onCaoClick = { route ->
                                    navController.navigate(route)
                                },

                                onHistoricoClick = {
                                    navController.navigate("historico_passeios")
                                },

                                onBackClick = {
                                    viewModel.logout(navController)
                                }
                            )
                        }

                        // ------------------------------------------------
                        // HISTÓRICO DE PASSEIOS
                        // ------------------------------------------------
                        composable("historico_passeios") {
                            HistoricoPasseiosScreen(
                                onBackClick = { navController.navigateUp() }
                            )
                        }

                        // ------------------------------------------------
                        // PERFIL FORNECEDOR
                        // ------------------------------------------------
                        composable(
                            route = "perfil_fornecedor/{nome}/{localidade}/{rating}",
                            arguments = listOf(
                                navArgument("nome") { type = NavType.StringType },
                                navArgument("localidade") { type = NavType.StringType },
                                navArgument("rating") { type = NavType.StringType }
                            )
                        ) { entry ->

                            PerfilFornecedorScreen(
                                nomeFornecedor = entry.arguments!!.getString("nome")!!,
                                localidadeFornecedor = entry.arguments!!.getString("localidade")!!,
                                ratingFornecedor = entry.arguments!!.getString("rating")!!,

                                onBackClick = { navController.navigateUp() },

                                onScheduleClick = { tipo, minutos, preco ->
                                    navController.navigate("marcar_passeio/$tipo/$minutos/$preco")
                                }
                            )
                        }

                        // ------------------------------------------------
                        // MARCAR PASSEIO COM PARAMETROS
                        // ------------------------------------------------
                        composable(
                            route = "perfil_fornecedor/{nome}/{localidade}/{rating}",
                            arguments = listOf(
                                navArgument("nome") { type = NavType.StringType },
                                navArgument("localidade") { type = NavType.StringType },
                                navArgument("rating") { type = NavType.FloatType }
                            )
                        ) { entry ->

                            PerfilFornecedorScreen(
                                nomeFornecedor = entry.arguments!!.getString("nome")!!,
                                localidadeFornecedor = entry.arguments!!.getString("localidade")!!,
                                ratingFornecedor = entry.arguments!!.getFloat("rating").toString(),
                                onBackClick = { navController.navigateUp() },
                                onScheduleClick = { tipo, minutos, preco ->
                                    navController.navigate("marcar_passeio/$tipo/$minutos/$preco")
                                }
                            )
                        }

                        // ------------------------------------------------
                        // MARCAR PASSEIO MANUAL
                        // ------------------------------------------------
                        composable("marcar_passeio") {
                            MarcarPasseioScreen(
                                tipoInicial = "",
                                minutosIniciais = 0,
                                onBackClick = { navController.navigateUp() }
                            )
                        }

                        // ------------------------------------------------
                        // FORMULÁRIOS
                        // ------------------------------------------------
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

                        // ------------------------------------------------
                        // BUSCAR FORNECEDORES
                        // ------------------------------------------------
                        composable("buscar_fornecedores") {
                            BuscarFornecedoresScreen(
                                onBackClick = { navController.navigateUp() },
                                onVerPerfil = { fornecedor ->
                                    val nome = Uri.encode(fornecedor.nome)
                                    val localidade = Uri.encode(fornecedor.localidade)
                                    val rating = Uri.encode(fornecedor.rating.toString())
                                    navController.navigate("perfil_fornecedor/$nome/$localidade/$rating")
                                }
                            )
                        }

                        // ------------------------------------------------
                        // PERFIL DO CÃO (Todos os campos)
                        // ------------------------------------------------
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
                                navArgument("localidadeDono") { type = NavType.StringType },
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
