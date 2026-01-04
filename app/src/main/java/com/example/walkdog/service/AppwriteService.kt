package com.example.walkdog.service

import android.content.Context
import io.appwrite.Client
import io.appwrite.services.Account
import io.appwrite.services.Databases
import io.appwrite.services.Storage

object AppwriteService {

    const val ENDPOINT = "https://appwrite.hugetower.cloud/v1"
    const val PROJECT_ID = "691e407c0036fe1c7f17"

    private var _client: Client? = null
    val client: Client
        get() = _client ?: throw IllegalStateException("AppwriteService not initialized")

    private var _account: Account? = null
    val account: Account
        get() = _account ?: throw IllegalStateException("Account not initialized")

    private var _databases: Databases? = null
    val databases: Databases
        get() = _databases ?: throw IllegalStateException("Databases not initialized")

    private var _storage: Storage? = null
    val storage: Storage
        get() = _storage ?: throw IllegalStateException("Storage not initialized")

    fun init(context: Context) {
        if (_client != null) return

        _client = Client(context.applicationContext)
            .setEndpoint(ENDPOINT)
            .setProject(PROJECT_ID)

        _account = Account(client)
        _databases = Databases(client)
        _storage = Storage(client)
    }
}
