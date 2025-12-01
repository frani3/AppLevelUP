package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.data.network.session.SessionTokenProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * Estado de sesión simplificado basado en LevelUp token
 */
data class LevelUpSessionState(
    val isLoggedIn: Boolean = false,
    val profileRole: String? = null,
    val isSuperAdmin: Boolean = false
)

class SessionViewModel(
    private val tokenProvider: SessionTokenProvider = LevelUpDependencyContainer.sessionTokenProvider
) : ViewModel() {

    private val _sessionState = MutableStateFlow(LevelUpSessionState())
    val sessionState: StateFlow<LevelUpSessionState> = _sessionState

    init {
        viewModelScope.launch {
            // Combinar token y rol para actualizar el estado
            combine(
                tokenProvider.tokenFlow,
                tokenProvider.userRole
            ) { token, role ->
                val isLoggedIn = token != null
                val normalizedRole = role?.lowercase()
                val isSuperAdmin = normalizedRole == "superadmin" || normalizedRole == "super_admin"
                val isAdmin = isSuperAdmin || normalizedRole == "admin" || normalizedRole == "administrador"
                
                LevelUpSessionState(
                    isLoggedIn = isLoggedIn,
                    profileRole = if (isAdmin) "Administrador" else role,
                    isSuperAdmin = isSuperAdmin
                )
            }.collect { state ->
                _sessionState.value = state
            }
        }
    }
}
