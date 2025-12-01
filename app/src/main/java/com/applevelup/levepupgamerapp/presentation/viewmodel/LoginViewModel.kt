package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.data.sync.LevelUpLegacyUserSyncer
import com.applevelup.levepupgamerapp.domain.usecase.ValidateUserLoginUseCase
import com.applevelup.levepupgamerapp.domain.sync.LegacyUserSyncer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false
)

class LoginViewModel(
    private val legacyUserSyncer: LegacyUserSyncer = LevelUpLegacyUserSyncer
) : ViewModel() {

    private val validateLoginUseCase = ValidateUserLoginUseCase(
        LevelUpDependencyContainer.authRepository,
        legacyUserSyncer
    )

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.update { it.copy(password = newPassword) }
    }

    fun onRememberMeChange(checked: Boolean) {
        _uiState.update { state ->
            val cleanedEmail = if (!checked && !state.isLoginSuccessful) "" else state.email
            state.copy(rememberMe = checked, email = cleanedEmail)
        }
    }

    fun login() {
        val email = _uiState.value.email
        val password = _uiState.value.password
        val remember = _uiState.value.rememberMe

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            when (val result = validateLoginUseCase(email, password, remember)) {
                is ValidateUserLoginUseCase.Result.Success -> {
                    _uiState.update { it.copy(isLoading = false, password = "", isLoginSuccessful = true) }
                }

                is ValidateUserLoginUseCase.Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }
}
