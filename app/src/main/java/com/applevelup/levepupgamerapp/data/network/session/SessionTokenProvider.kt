package com.applevelup.levepupgamerapp.data.network.session

import android.util.Base64
import com.applevelup.levepupgamerapp.data.prefs.SessionPreferencesDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.concurrent.atomic.AtomicReference

class SessionTokenProvider(
    private val sessionPreferencesDataSource: SessionPreferencesDataSource,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
) : TokenProvider {

    private val tokenState = MutableStateFlow<String?>(null)
    private val cachedToken = AtomicReference<String?>(null)
    
    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole.asStateFlow()

    init {
        scope.launch {
            sessionPreferencesDataSource.tokenFlow.collect { token ->
                cachedToken.set(token)
                tokenState.emit(token)
                _userRole.emit(extractRoleFromToken(token))
            }
        }
    }
    
    /**
     * Extrae los roles del payload del JWT.
     * El JWT tiene formato: header.payload.signature
     * El claim 'roles' es un array: ["ROLE_CLIENTE"] o ["ROLE_ADMINISTRADOR", "ROLE_SUPERADMIN"]
     */
    private fun extractRoleFromToken(token: String?): String? {
        if (token.isNullOrBlank()) return null
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null
            
            val payload = parts[1]
            val decodedBytes = Base64.decode(payload, Base64.URL_SAFE or Base64.NO_WRAP)
            val json = JSONObject(String(decodedBytes, Charsets.UTF_8))
            
            android.util.Log.d("SessionTokenProvider", "JWT Payload: $json")
            
            // El claim 'roles' es un array de strings
            val rolesArray = json.optJSONArray("roles")
            val roles = mutableListOf<String>()
            if (rolesArray != null) {
                for (i in 0 until rolesArray.length()) {
                    roles.add(rolesArray.getString(i))
                }
            }
            
            android.util.Log.d("SessionTokenProvider", "JWT Roles: $roles")
            
            // Retornar roles separados por coma para procesarlos después
            roles.joinToString(",").takeIf { it.isNotBlank() }
        } catch (e: Exception) {
            android.util.Log.e("SessionTokenProvider", "Error extracting roles", e)
            null
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
