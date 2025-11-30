package com.applevelup.levepupgamerapp.data.network.session

import kotlinx.coroutines.flow.Flow

/**
 * Abstraction that exposes the latest JWT token for requests and lets callers persist new tokens.
 */
interface TokenProvider {
    fun getToken(): String?
    val tokenFlow: Flow<String?>
    suspend fun persistToken(token: String?)
}
