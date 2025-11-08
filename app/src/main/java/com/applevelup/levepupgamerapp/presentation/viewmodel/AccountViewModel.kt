package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.repository.UserRepositoryImpl
import com.applevelup.levepupgamerapp.domain.usecase.UpdateAccountUseCase
import com.applevelup.levepupgamerapp.domain.usecase.ValidateAccountFormUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AccountUiState(
    val fullName: String = "",
    val email: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val errors: Map<String, String?> = emptyMap(),
    val successMessage: String? = null
)

class AccountViewModel(
    private val repo: UserRepositoryImpl = UserRepositoryImpl()
) : ViewModel() {

    private val updateUseCase = UpdateAccountUseCase(repo)
    private val validateUseCase = ValidateAccountFormUseCase()

    private val _uiState = MutableStateFlow(AccountUiState(fullName = "FranI3", email = "frani3@email.com"))
    val uiState: StateFlow<AccountUiState> = _uiState

    fun onFullNameChange(value: String) = _uiState.update { it.copy(fullName = value) }
    fun onEmailChange(value: String) = _uiState.update { it.copy(email = value) }
    fun onCurrentPasswordChange(value: String) = _uiState.update { it.copy(currentPassword = value) }
    fun onNewPasswordChange(value: String) = _uiState.update { it.copy(newPassword = value) }
    fun onConfirmPasswordChange(value: String) = _uiState.update { it.copy(confirmPassword = value) }

    fun saveChanges() {
        val s = _uiState.value
        val errs = validateUseCase(s.fullName, s.email, s.currentPassword, s.newPassword, s.confirmPassword)
        val map = mapOf(
            "name" to errs.fullNameError,
            "email" to errs.emailError,
            "password" to errs.passwordError,
            "confirm" to errs.confirmError
        )

        if (map.values.all { it == null }) {
            viewModelScope.launch {
                updateUseCase(s.fullName, s.email, if (s.newPassword.isNotBlank()) s.newPassword else null)
                _uiState.update { it.copy(successMessage = "Cambios guardados correctamente") }
            }
        } else {
            _uiState.update { it.copy(errors = map, successMessage = null) }
        }
    }
}
