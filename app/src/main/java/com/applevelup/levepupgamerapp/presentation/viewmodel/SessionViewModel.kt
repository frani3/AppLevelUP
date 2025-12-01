package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.data.network.session.TokenProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val tokenProvider: TokenProvider = LevelUpDependencyContainer.sessionTokenProvider
) : ViewModel() {

    private val _sessionState = MutableStateFlow(LevelUpSessionState())
    val sessionState: StateFlow<LevelUpSessionState> = _sessionState

    init {
        viewModelScope.launch {
            tokenProvider.tokenFlow.collect { token ->
                _sessionState.value = LevelUpSessionState(isLoggedIn = token != null)
            }
        }
    }
}
