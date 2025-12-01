package com.applevelup.levepupgamerapp.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.LevelUpDependencyContainer
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResource
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.usecase.levelup.FetchUserProfileUseCase
import com.applevelup.levepupgamerapp.domain.usecase.levelup.UpdateLevelUpProfileUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EditProfileUiState(
    val fullName: String = "",
    val email: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null
)

sealed interface EditProfileEvent {
    data object ProfileUpdated : EditProfileEvent
}

class EditProfileViewModel : ViewModel() {

    private val levelUpRepository = LevelUpDependencyContainer.userRepository
    private val fetchProfileUseCase = FetchUserProfileUseCase(levelUpRepository)
    private val updateProfileUseCase = UpdateLevelUpProfileUseCase(levelUpRepository)

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState

    private val _events = MutableSharedFlow<EditProfileEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<EditProfileEvent> = _events.asSharedFlow()

    init {
        observeProfile()
    }

    fun onFullNameChange(value: String) {
        _uiState.update {
            it.copy(fullName = value, fullNameError = null, errorMessage = null)
        }
    }

    fun onEmailChange(value: String) {
        _uiState.update {
            it.copy(email = value, emailError = null, errorMessage = null)
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(newPassword = value, passwordError = null, errorMessage = null)
        }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update {
            it.copy(confirmPassword = value, passwordError = null, errorMessage = null)
        }
    }

    fun saveProfile() {
        val state = _uiState.value
        if (!validate(state)) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            when (val result = updateProfileUseCase(
                name = state.fullName.trim(),
                email = state.email.trim(),
                newPassword = state.newPassword.takeIf { it.isNotBlank() }
            )) {
                is LevelUpResult.Success -> {
                    _uiState.update {
                        it.copy(
                            newPassword = "",
                            confirmPassword = "",
                            isSaving = false,
                            passwordError = null
                        )
                    }
                    _events.emit(EditProfileEvent.ProfileUpdated)
                }

                is LevelUpResult.Failure -> _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = result.throwable.message
                            ?: "No se pudo actualizar el perfil. Intenta nuevamente."
                    )
                }
            }
        }
    }

    private fun observeProfile() {
        viewModelScope.launch {
            fetchProfileUseCase(forceRefresh = true).collectLatest { resource ->
                when (resource) {
                    LevelUpResource.Loading -> _uiState.update { it.copy(isLoading = true, errorMessage = null) }

                    is LevelUpResource.Error -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = resource.throwable.message
                                ?: "No se pudo cargar la información del perfil."
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
                                    errorMessage = null
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

    private fun validate(state: EditProfileUiState): Boolean {
        val nameError = if (state.fullName.isBlank()) "Ingresa tu nombre" else null
        val emailError = when {
            state.email.isBlank() -> "Ingresa tu correo"
            !Patterns.EMAIL_ADDRESS.matcher(state.email).matches() -> "Correo inválido"
            else -> null
        }

        val passwordError = when {
            state.newPassword.isBlank() && state.confirmPassword.isBlank() -> null
            state.newPassword.length < 6 -> "La contraseña debe tener al menos 6 caracteres"
            state.newPassword != state.confirmPassword -> "Las contraseñas no coinciden"
            else -> null
        }

        val hasErrors = listOf(nameError, emailError, passwordError).any { it != null }

        if (hasErrors) {
            _uiState.update {
                it.copy(
                    fullNameError = nameError,
                    emailError = emailError,
                    passwordError = passwordError
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    fullNameError = null,
                    emailError = null,
                    passwordError = null
                )
            }
        }

        return !hasErrors
    }
}
