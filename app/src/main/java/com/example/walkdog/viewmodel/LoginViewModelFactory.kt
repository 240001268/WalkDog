package com.example.walkdog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.walkdog.service.AppwriteService

class LoginViewModelFactory(
    private val appwrite: AppwriteService
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(appwrite) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
