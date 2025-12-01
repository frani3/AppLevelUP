package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.data.network.session.SessionTokenProvider
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpUserRepository
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
    private val tokenProvider: SessionTokenProvider = LevelUpDependencyContainer.sessionTokenProvider,
    private val userRepository: LevelUpUserRepository = LevelUpDependencyContainer.userRepository
) : ViewModel() {

    private val _sessionState = MutableStateFlow(LevelUpSessionState())
    val sessionState: StateFlow<LevelUpSessionState> = _sessionState

    init {
        viewModelScope.launch {
            // Combinar token, roles del JWT y perfil del usuario
            combine(
                tokenProvider.tokenFlow,
                tokenProvider.userRole,
                userRepository.observeProfile()
            ) { token, jwtRoles, profile ->
                val isLoggedIn = token != null
                
                // Parsear roles del JWT (viene como "ROLE_ADMIN,ROLE_SUPERADMIN")
                val rolesList = jwtRoles?.split(",")?.map { it.trim().uppercase() } ?: emptyList()
                
                // Determinar si es superadmin: por JWT o por systemAccount del perfil
                val isSuperAdmin = rolesList.any { it.contains("SUPERADMIN") } || 
                    profile?.isSuperAdmin == true
                
                // Determinar si es admin: ROLE_ADMINISTRADOR, ROLE_VENDEDOR, o perfil Administrador/Vendedor
                val isAdmin = isSuperAdmin ||
                    rolesList.any { it.contains("ADMINISTRADOR") || it.contains("VENDEDOR") } ||
                    profile?.perfil?.equals("Administrador", ignoreCase = true) == true ||
                    profile?.perfil?.equals("Vendedor", ignoreCase = true) == true
                
                LevelUpSessionState(
                    isLoggedIn = isLoggedIn,
                    profileRole = if (isAdmin) "Administrador" else profile?.perfil,
                    isSuperAdmin = isSuperAdmin
                )
            }.collect { state ->
                _sessionState.value = state
            }
        }
    }
}
