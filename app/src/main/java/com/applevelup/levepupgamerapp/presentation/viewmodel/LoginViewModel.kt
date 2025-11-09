package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.repository.SessionRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.UserRepositoryImpl
import com.applevelup.levepupgamerapp.domain.usecase.ObserveSessionUseCase
import com.applevelup.levepupgamerapp.domain.usecase.ValidateUserLoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
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
    private val sessionRepository: SessionRepositoryImpl = SessionRepositoryImpl(),
    private val userRepository: UserRepositoryImpl = UserRepositoryImpl()
) : ViewModel() {

    private val validateLoginUseCase = ValidateUserLoginUseCase(userRepository, sessionRepository)
    private val observeSessionUseCase = ObserveSessionUseCase(sessionRepository)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    init {
        viewModelScope.launch {
            observeSessionUseCase().collect { session ->
                _uiState.update { state ->
                    val email = when {
                        session.rememberMe && state.email.isBlank() -> session.email.orEmpty()
                        !session.rememberMe && !session.isLoggedIn -> ""
                        else -> state.email
                    }

                    state.copy(
                        email = email,
                        rememberMe = session.rememberMe,
                        isLoginSuccessful = session.isLoggedIn,
                        errorMessage = if (session.isLoggedIn) null else state.errorMessage
                    )
                }
            }
        }
    }

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
