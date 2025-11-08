package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistroUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val termsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val isRegisterSuccessful: Boolean = false,
    val errors: FormErrors = FormErrors()
)

data class FormErrors(
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val termsError: String? = null
)

class RegistroViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value) }
    }

    fun onTermsChange(value: Boolean) {
        _uiState.update { it.copy(termsAccepted = value) }
    }

    fun register() {
        val state = _uiState.value
        val errors = validateForm(state)

        if (errors != FormErrors()) {
            _uiState.update { it.copy(errors = errors) }
            return
        }

        // Simulación de registro
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errors = FormErrors()) }
            delay(1200)
            _uiState.update { it.copy(isLoading = false, isRegisterSuccessful = true) }
        }
    }

    private fun validateForm(state: RegistroUiState): FormErrors {
        var usernameError: String? = null
        var emailError: String? = null
        var passwordError: String? = null
        var confirmPasswordError: String? = null
        var termsError: String? = null

        if (state.username.isBlank()) usernameError = "El nombre es obligatorio"
        if (state.email.isBlank() || !state.email.contains("@")) emailError = "Correo inválido"
        if (state.password.length < 6) passwordError = "Contraseña muy corta"
        if (state.password != state.confirmPassword) confirmPasswordError = "Las contraseñas no coinciden"
        if (!state.termsAccepted) termsError = "Debes aceptar los términos"

        return FormErrors(usernameError, emailError, passwordError, confirmPasswordError, termsError)
    }
}
