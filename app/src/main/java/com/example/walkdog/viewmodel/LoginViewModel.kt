package com.example.walkdog.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.walkdog.service.AppwriteService
import io.appwrite.exceptions.AppwriteException
import io.appwrite.ID                      // ✅ importar ID
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

class LoginViewModel(
    private val appwrite: AppwriteService
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state

    fun login(email: String, password: String, redirectRoute: String) {
        viewModelScope.launch {
            try {
                _state.value = LoginUiState(loading = true)

                val account = try {
                    appwrite.account
                } catch (e: Exception) {
                    throw IllegalStateException("AppwriteService não inicializado.")
                }

                _state.value = LoginUiState(success = true)

                var xd = account.createEmailPasswordSession(email, password)

                _state.value = LoginUiState(route = redirectRoute, userId = xd.userId)

            } catch (e: AppwriteException) {
                e.printStackTrace()
                _state.value = LoginUiState(error = "Erro Appwrite: ${e.message}")
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = LoginUiState(error = e.message ?: "Erro desconhecido")
            }
        }
    }

    fun logout(navController: NavController) {
        viewModelScope.launch {
            try {
                appwrite.account.deleteSession("current")
                navController.navigate("login")
            } catch (ex: Exception) {
                ex.message?.let { Log.e("Teste", it) }
            }
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                _state.value = LoginUiState(loading = true)

                val account = try {
                    appwrite.account
                } catch (e: Exception) {
                    throw IllegalStateException("AppwriteService não inicializado.")
                }

                // ✅ usar ID.unique() em vez de "unique()"
                account.create(
                    userId = ID.unique(),
                    email = email,
                    password = password,
                    name = name
                )

                _state.value = LoginUiState(success = true)

            } catch (e: AppwriteException) {
                e.printStackTrace()
                _state.value = LoginUiState(error = "Erro Appwrite: ${e.message}")
            } catch (e: Exception) {
                e.printStackTrace()
                _state.value = LoginUiState(error = e.message ?: "Erro desconhecido")
            }
        }
    }
}
