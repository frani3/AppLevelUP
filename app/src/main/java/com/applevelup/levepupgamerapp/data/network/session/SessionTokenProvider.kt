package com.applevelup.levepupgamerapp.data.network.session

import com.applevelup.levepupgamerapp.data.prefs.SessionPreferencesDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicReference

class SessionTokenProvider(
    private val sessionPreferencesDataSource: SessionPreferencesDataSource,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) : TokenProvider {

    private val tokenState = MutableStateFlow<String?>(null)
    private val cachedToken = AtomicReference<String?>(null)

    init {
        scope.launch {
            sessionPreferencesDataSource.tokenFlow.collect { token ->
                cachedToken.set(token)
                tokenState.emit(token)
            }
        }
    }

    override fun getToken(): String? = cachedToken.get()

    override val tokenFlow = tokenState.asStateFlow()

    override suspend fun persistToken(token: String?) {
        if (token.isNullOrBlank()) {
            sessionPreferencesDataSource.clearToken()
        } else {
            sessionPreferencesDataSource.saveToken(token)
        }
    }
}
