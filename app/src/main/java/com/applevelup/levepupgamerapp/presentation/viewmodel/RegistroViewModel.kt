package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.repository.AddressRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.PaymentRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.SessionRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.UserRepositoryImpl
import com.applevelup.levepupgamerapp.domain.usecase.RegisterUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class RegistroUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val birthDate: String = "",
    val address: String = "",
    val termsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val isRegisterSuccessful: Boolean = false,
    val errors: FormErrors = FormErrors(),
    val generalError: String? = null,
    val qualifiesForDuocDiscount: Boolean = false
)

data class FormErrors(
    val usernameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val birthDateError: String? = null,
    val addressError: String? = null,
    val termsError: String? = null
)

class RegistroViewModel(
    private val registerUserUseCase: RegisterUserUseCase = RegisterUserUseCase(
        UserRepositoryImpl(),
        SessionRepositoryImpl()
    ),
    private val addressRepository: AddressRepositoryImpl = AddressRepositoryImpl(),
    private val paymentRepository: PaymentRepositoryImpl = PaymentRepositoryImpl()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState

    fun onUsernameChange(value: String) {
        _uiState.update { it.copy(username = value) }
    }

    fun onEmailChange(value: String) {
        _uiState.update {
            it.copy(
                email = value,
                qualifiesForDuocDiscount = isDuocEmail(value)
            )
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value) }
    }

    fun onBirthDateChange(value: String) {
        _uiState.update { it.copy(birthDate = value) }
    }

    fun onAddressChange(value: String) {
        _uiState.update { it.copy(address = value) }
    }

    fun onTermsChange(value: Boolean) {
        _uiState.update { it.copy(termsAccepted = value) }
    }

    fun register() {
        val state = _uiState.value
        val errors = validateForm(state)

        if (errors != FormErrors()) {
            _uiState.update { it.copy(errors = errors, generalError = null) }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errors = FormErrors(),
                    generalError = null
                )
            }

            val result = registerUserUseCase(
                fullName = state.username.trim(),
                email = state.email.trim(),
                password = state.password,
                birthDate = state.birthDate.trim(),
                address = state.address.trim()
            )

            when (result) {
                is RegisterUserUseCase.Result.Success -> {
                    if (state.address.isNotBlank()) {
                        addressRepository.setPrimaryAddress(state.address.trim())
                    } else {
                        addressRepository.clearAll()
                    }
                    paymentRepository.reset()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRegisterSuccessful = true,
                            generalError = null
                        )
                    }
                }

                is RegisterUserUseCase.Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            generalError = result.message
                        )
                    }
                }
            }
        }
    }

    private fun validateForm(state: RegistroUiState): FormErrors {
        var usernameError: String? = null
        var emailError: String? = null
        var passwordError: String? = null
        var confirmPasswordError: String? = null
        var birthDateError: String? = null
        var addressError: String? = null
        var termsError: String? = null

        if (state.username.isBlank()) usernameError = "El nombre es obligatorio"
        if (state.email.isBlank() || !state.email.contains("@")) emailError = "Correo inválido"
        if (state.password.length < 6) passwordError = "Contraseña muy corta"
        if (state.password != state.confirmPassword) confirmPasswordError = "Las contraseñas no coinciden"
        if (state.birthDate.isBlank()) {
            birthDateError = "La fecha de nacimiento es obligatoria"
        } else {
            val parsedBirthDate = parseBirthDate(state.birthDate)
            if (parsedBirthDate == null) {
                birthDateError = "Formato inválido. Usa DD/MM/AAAA"
            } else {
                val age = calculateAge(parsedBirthDate)
                if (age < MIN_AGE || age > MAX_AGE) {
                    birthDateError = "La edad debe estar entre $MIN_AGE y $MAX_AGE años"
                }
            }
        }
        if (state.address.isBlank()) addressError = "La dirección es obligatoria"
        if (!state.termsAccepted) termsError = "Debes aceptar los términos"

        return FormErrors(
            usernameError = usernameError,
            emailError = emailError,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError,
            birthDateError = birthDateError,
            addressError = addressError,
            termsError = termsError
        )
    }

    private fun parseBirthDate(raw: String): Calendar? {
        if (raw.isBlank()) return null
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            isLenient = false
        }
        return try {
            val parsed = formatter.parse(raw) ?: return null
            Calendar.getInstance().apply { time = parsed }
        } catch (_: ParseException) {
            null
        }
    }

    private fun calculateAge(birthDate: Calendar): Int {
        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age -= 1
        }
        return age
    }

    private fun isDuocEmail(rawEmail: String): Boolean {
        val normalized = rawEmail.trim().lowercase()
        if (!normalized.contains("@")) return false
        val domain = normalized.substringAfter("@")
        return domain.contains("duoc")
    }

    companion object {
        private const val MIN_AGE = 18
        private const val MAX_AGE = 120
    }
}
