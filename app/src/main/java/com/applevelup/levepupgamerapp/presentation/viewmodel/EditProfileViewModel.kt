package com.applevelup.levepupgamerapp.presentation.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.repository.UserRepositoryImpl
import com.applevelup.levepupgamerapp.domain.usecase.GetUserProfileUseCase
import com.applevelup.levepupgamerapp.domain.usecase.UpdateUserProfileUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

class EditProfileViewModel(
    repository: UserRepositoryImpl = UserRepositoryImpl()
) : ViewModel() {

    private val getProfileUseCase = GetUserProfileUseCase(repository)
    private val updateProfileUseCase = UpdateUserProfileUseCase(repository)

    private val _uiState = MutableStateFlow(EditProfileUiState())
    val uiState: StateFlow<EditProfileUiState> = _uiState

    private val _events = MutableSharedFlow<EditProfileEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<EditProfileEvent> = _events.asSharedFlow()

    init {
        viewModelScope.launch {
            loadProfile()
        }
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
            try {
                updateProfileUseCase(
                    fullName = state.fullName.trim(),
                    email = state.email.trim(),
                    newPassword = state.newPassword.takeIf { it.isNotBlank() }
                )
                _uiState.update {
                    it.copy(
                        newPassword = "",
                        confirmPassword = "",
                        isSaving = false,
                        passwordError = null
                    )
                }
                _events.emit(EditProfileEvent.ProfileUpdated)
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = "No se pudo actualizar el perfil. Intenta nuevamente."
                    )
                }
            }
        }
    }

    private suspend fun loadProfile() {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        try {
            val profile = getProfileUseCase.getUserProfile()
            if (profile != null) {
                _uiState.update {
                    it.copy(
                        fullName = profile.name,
                        email = profile.email,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "No se pudo cargar la información del perfil."
                    )
                }
            }
        } catch (exception: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    errorMessage = "Ocurrió un error al cargar tu perfil."
                )
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
