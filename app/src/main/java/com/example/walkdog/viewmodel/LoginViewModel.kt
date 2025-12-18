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

class LoginViewModel(
    private val appwrite: AppwriteService
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state

    // LOGIN CLIENTE (default)
    fun login(email: String, password: String) {
        login(email, password, "cliente")
    }

    // LOGIN COM TIPO (cliente / fornecedor)
    fun login(email: String, password: String, tipo: String) {
        viewModelScope.launch {
            try {
                _state.value = LoginUiState(loading = true)

                val account = appwrite.account
                account.createEmailPasswordSession(email, password)

                val user = account.get()

                _state.value = LoginUiState(
                    success = true,
                    route = tipo,
                    userId = user.id
                )

            } catch (e: AppwriteException) {
                _state.value = LoginUiState(error = "Erro Appwrite: ${e.message}")
            } catch (e: Exception) {
                _state.value = LoginUiState(error = e.message ?: "Erro desconhecido")
            }
        }
    }

    fun setError(message: String) {
        _state.value = LoginUiState(error = message)
    }

    fun logout(navController: NavController) {
        viewModelScope.launch {
            try {
                appwrite.account.deleteSession("current")
                navController.navigate("login")
            } catch (ex: Exception) {
                ex.message?.let { Log.e("Logout", it) }
            }
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            try {
                _state.value = LoginUiState(loading = true)

                val account = appwrite.account
                account.create(
                    userId = ID.unique(),
                    email = email,
                    password = password,
                    name = name
                )

                _state.value = LoginUiState(success = true)

            } catch (e: AppwriteException) {
                _state.value = LoginUiState(error = "Erro Appwrite: ${e.message}")
            } catch (e: Exception) {
                _state.value = LoginUiState(error = e.message ?: "Erro desconhecido")
            }
        }
    }
}
