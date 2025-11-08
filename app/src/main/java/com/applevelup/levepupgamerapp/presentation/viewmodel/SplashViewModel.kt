package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.repository.SessionRepositoryImpl
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
    private val sessionRepo: SessionRepositoryImpl = SessionRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState

    init {
        viewModelScope.launch {
            delay(2000) // animación / logo
            val dest = if (sessionRepo.isLoggedIn()) SplashDestination.HOME else SplashDestination.LOGIN
            _uiState.update { it.copy(destination = dest) }
        }
    }

    fun onNavigated() {
        _uiState.update { it.copy(destination = SplashDestination.NONE) }
    }
}
