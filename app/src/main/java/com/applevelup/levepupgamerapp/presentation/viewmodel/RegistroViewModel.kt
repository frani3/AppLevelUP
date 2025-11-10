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
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val birthDate: String = "",
    val address: String = "",
    val run: String = "",
    val region: String = "",
    val comuna: String = "",
    val referralCode: String = "",
    val termsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val isRegisterSuccessful: Boolean = false,
    val errors: FormErrors = FormErrors(),
    val generalError: String? = null,
    val qualifiesForDuocDiscount: Boolean = false
)

data class FormErrors(
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val birthDateError: String? = null,
    val addressError: String? = null,
    val runError: String? = null,
    val regionError: String? = null,
    val comunaError: String? = null,
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

    fun onFirstNameChange(value: String) {
        _uiState.update { it.copy(firstName = value) }
    }

    fun onLastNameChange(value: String) {
        _uiState.update { it.copy(lastName = value) }
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

    fun onRunChange(value: String) {
        _uiState.update { it.copy(run = value) }
    }

    fun onRegionChange(value: String) {
        _uiState.update {
            it.copy(
                region = value,
                comuna = if (it.comuna.isBlank() || it.region != value) "" else it.comuna
            )
        }
    }

    fun onComunaChange(value: String) {
        _uiState.update { it.copy(comuna = value) }
    }

    fun onReferralCodeChange(value: String) {
        _uiState.update { it.copy(referralCode = value) }
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
                firstName = state.firstName.trim(),
                lastName = state.lastName.trim(),
                run = state.run.trim(),
                email = state.email.trim(),
                password = state.password,
                birthDate = state.birthDate.trim(),
                region = state.region.trim(),
                comuna = state.comuna.trim(),
                address = state.address.trim(),
                referralCode = state.referralCode.trim().takeIf { it.isNotBlank() }
            )

            when (result) {
                is RegisterUserUseCase.Result.Success -> {
                    if (state.address.isNotBlank()) {
                        addressRepository.setPrimaryAddress(state.address.trim())
                    } else {
                        addressRepository.clearAll()
                    }
                    paymentRepository.clearPaymentMethods()
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
        var firstNameError: String? = null
        var lastNameError: String? = null
        var emailError: String? = null
        var passwordError: String? = null
        var confirmPasswordError: String? = null
        var birthDateError: String? = null
        var addressError: String? = null
        var runError: String? = null
        var regionError: String? = null
        var comunaError: String? = null
        var termsError: String? = null

        if (state.firstName.isBlank()) firstNameError = "El nombre es obligatorio"
        if (state.lastName.isBlank()) lastNameError = "Los apellidos son obligatorios"
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
        if (state.run.isBlank()) {
            runError = "El RUN es obligatorio"
        } else if (!isValidRun(state.run)) {
            runError = "RUN inválido"
        }
        if (state.region.isBlank()) regionError = "Selecciona una región"
        if (state.comuna.isBlank()) comunaError = "Selecciona una comuna"
        if (state.address.isBlank()) addressError = "La dirección es obligatoria"
        if (!state.termsAccepted) termsError = "Debes aceptar los términos"

        return FormErrors(
            firstNameError = firstNameError,
            lastNameError = lastNameError,
            emailError = emailError,
            passwordError = passwordError,
            confirmPasswordError = confirmPasswordError,
            birthDateError = birthDateError,
            addressError = addressError,
            runError = runError,
            regionError = regionError,
            comunaError = comunaError,
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

    private fun isValidRun(raw: String): Boolean {
        val sanitized = sanitizeRun(raw) ?: return false
        if (!sanitized.matches(Regex("^\\d{7,8}-[0-9K]$"))) return false
        val numberPart = sanitized.substringBefore('-')
        val expectedDigit = calculateRunCheckDigit(numberPart)
        val providedDigit = sanitized.substringAfter('-')
        return expectedDigit.equals(providedDigit, ignoreCase = true)
    }

    private fun sanitizeRun(raw: String): String? {
        val cleaned = raw.uppercase(Locale.getDefault())
            .replace(".", "")
            .replace(" ", "")
            .replace("-", "")
        if (cleaned.length < 2) return null
        val numberPart = cleaned.dropLast(1)
        if (numberPart.isEmpty() || !numberPart.all { it.isDigit() }) return null
        val checkDigit = cleaned.last()
        return "$numberPart-$checkDigit"
    }

    private fun calculateRunCheckDigit(numberPart: String): String {
        var multiplier = 2
        var sum = 0
        for (digitChar in numberPart.reversed()) {
            val digit = digitChar.digitToIntOrNull() ?: return ""
            sum += digit * multiplier
            multiplier = if (multiplier == 7) 2 else multiplier + 1
        }
        val remainder = 11 - (sum % 11)
        return when (remainder) {
            11 -> "0"
            10 -> "K"
            else -> remainder.toString()
        }
    }

    companion object {
        private const val MIN_AGE = 18
        private const val MAX_AGE = 120
    }
}
