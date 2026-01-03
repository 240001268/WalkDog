package com.example.walkdog.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.walkdog.service.AppwriteService
import io.appwrite.ID
import io.appwrite.exceptions.AppwriteException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val route: String? = null,
    val userId: String? = null
)

private const val DB_ID = "69236f45003447bc5844"
private const val COLLECTION_CLIENTES = "69236f5200282814eb3c"
private const val COLLECTION_FORNECEDORES = "69236f93001828d82b6f"

class LoginViewModel(
    private val appwrite: AppwriteService
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state

    fun login(email: String, password: String) {
        login(email, password, "cliente")
    }

    fun login(email: String, password: String, tipo: String) {
        viewModelScope.launch {
            try {
                _state.value = LoginUiState(loading = true)

                appwrite.account.createEmailPasswordSession(
                    email = email,
                    password = password
                )

                val user = appwrite.account.get()
                val userId = user.id

                val existeCliente = try {
                    appwrite.databases.getDocument(
                        databaseId = DB_ID,
                        collectionId = COLLECTION_CLIENTES,
                        documentId = userId
                    )
                    true
                } catch (e: Exception) {
                    false
                }

                val existeFornecedor = try {
                    appwrite.databases.getDocument(
                        databaseId = DB_ID,
                        collectionId = COLLECTION_FORNECEDORES,
                        documentId = userId
                    )
                    true
                } catch (e: Exception) {
                    false
                }

                when (tipo) {
                    "cliente" -> {
                        if (!existeCliente) {
                            appwrite.account.deleteSession("current")
                            _state.value = LoginUiState(
                                error = "Esta conta não é de cliente."
                            )
                            return@launch
                        }
                    }

                    "fornecedor" -> {
                        if (!existeFornecedor) {
                            appwrite.account.deleteSession("current")
                            _state.value = LoginUiState(
                                error = "Esta conta não é de fornecedor."
                            )
                            return@launch
                        }
                    }
                }

                _state.value = LoginUiState(
                    success = true,
                    route = tipo,
                    userId = userId
                )

            } catch (e: AppwriteException) {
                Log.e("LOGIN", "Erro Appwrite", e)
                _state.value = LoginUiState(
                    error = e.message ?: "Erro de autenticação"
                )
            } catch (e: Exception) {
                Log.e("LOGIN", "Erro inesperado", e)
                _state.value = LoginUiState(
                    error = "Erro inesperado ao fazer login"
                )
            }
        }
    }

    fun logout(navController: NavController) {
        viewModelScope.launch {
            try {
                appwrite.account.deleteSession("current")
                navController.navigate("login") {
                    popUpTo(0)
                }
            } catch (e: Exception) {
                Log.e("LOGOUT", e.message ?: "Erro ao fazer logout")
            }
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                _state.value = LoginUiState(loading = true)

                appwrite.account.create(
                    userId = ID.unique(),
                    email = email,
                    password = password,
                    name = name
                )

                appwrite.account.createEmailPasswordSession(
                    email = email,
                    password = password
                )

                _state.value = LoginUiState(success = true)

            } catch (e: AppwriteException) {
                _state.value = LoginUiState(
                    error = e.message ?: "Erro ao registar"
                )
            } catch (e: Exception) {
                _state.value = LoginUiState(
                    error = "Erro inesperado"
                )
            }
        }
    }

    fun setError(message: String) {
        _state.value = LoginUiState(error = message)
    }
}
