package com.applevelup.levepupgamerapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.data.network.session.TokenProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SplashDestination { LOGIN, HOME, NONE }

data class SplashUiState(
    val destination: SplashDestination = SplashDestination.NONE
)

class SplashViewModel(
    private val tokenProvider: TokenProvider = LevelUpDependencyContainer.sessionTokenProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState

    init {
        viewModelScope.launch {
            delay(2000) // animación / logo
            val hasToken = tokenProvider.getToken() != null
            Log.d("SplashViewModel", "hasToken = $hasToken")
            val dest = if (hasToken) SplashDestination.HOME else SplashDestination.LOGIN
            _uiState.update { it.copy(destination = dest) }
        }
    }

    fun onNavigated() {
        _uiState.update { it.copy(destination = SplashDestination.NONE) }
    }
}
