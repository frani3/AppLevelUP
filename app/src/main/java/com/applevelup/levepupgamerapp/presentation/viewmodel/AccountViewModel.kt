package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResource
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.usecase.ValidateAccountFormUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.FetchUserProfileUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.UpdateLevelUpProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AccountUiState(
    val fullName: String = "",
    val email: String = "",
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val errors: Map<String, String?> = emptyMap(),
    val successMessage: String? = null,
    val generalError: String? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false
)

class AccountViewModel : ViewModel() {

    private val levelUpRepository = LevelUpDependencyContainer.userRepository
    private val fetchProfileUseCase = FetchUserProfileUseCase(levelUpRepository)
    private val updateProfileUseCase = UpdateLevelUpProfileUseCase(levelUpRepository)
    private val validateUseCase = ValidateAccountFormUseCase()

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState

    init {
        observeProfile()
    }

    private fun observeProfile() {
        viewModelScope.launch {
            fetchProfileUseCase(forceRefresh = true).collectLatest { resource ->
                when (resource) {
                    LevelUpResource.Loading -> _uiState.update {
                        it.copy(isLoading = true, generalError = null)
                    }

                    is LevelUpResource.Error -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            generalError = resource.throwable.message ?: "No pudimos cargar tu perfil"
                        )
                    }

                    is LevelUpResource.Success -> {
                        val profile = resource.data
                        if (profile != null) {
                            _uiState.update {
                                it.copy(
                                    fullName = profile.name,
                                    email = profile.email,
                                    isLoading = false,
                                    generalError = null
                                )
                            }
                        } else {
                            _uiState.update { it.copy(isLoading = false) }
                        }
                    }
                }
            }
        }
    }

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
            submitChanges(
                fullName = s.fullName.trim(),
                email = s.email.trim(),
                newPassword = s.newPassword.takeIf { it.isNotBlank() }
            )
        } else {
            _uiState.update { it.copy(errors = map, successMessage = null) }
        }
    }

    private fun submitChanges(fullName: String, email: String, newPassword: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, successMessage = null, generalError = null) }
            when (val result = updateProfileUseCase(fullName, email, newPassword)) {
                is LevelUpResult.Success -> _uiState.update {
                    it.copy(
                        successMessage = "Cambios guardados correctamente",
                        newPassword = "",
                        confirmPassword = "",
                        currentPassword = "",
                        isSaving = false,
                        generalError = null
                    )
                }

                is LevelUpResult.Failure -> _uiState.update {
                    it.copy(
                        isSaving = false,
                        generalError = result.throwable.message
                            ?: "No pudimos guardar tus cambios"
                    )
                }
            }
        }
    }
}
