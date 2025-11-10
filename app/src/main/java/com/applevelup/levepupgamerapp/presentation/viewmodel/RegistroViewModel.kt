package com.applevelup.levepupgamerapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.applevelup.levepupgamerapp.data.repository.AddressRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.PaymentRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.SessionRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.UserRepositoryImpl
import com.applevelup.levepupgamerapp.domain.usecase.RegisterUserUseCase
import com.applevelup.levepupgamerapp.utils.RunUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

enum class RegistroField {
    FIRST_NAME,
    LAST_NAME,
    EMAIL,
    RUN,
    BIRTH_DATE,
    ADDRESS,
    REGION,
    COMUNA,
    PASSWORD,
    CONFIRM_PASSWORD,
    TERMS
}

data class RegistroUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val birthDate: String = "",
    val address: String = "",
    val runBody: String = "",
    val runCheckDigit: String = "",
    val runHasHyphen: Boolean = false,
    val region: String = "",
    val comuna: String = "",
    val referralCode: String = "",
    val termsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val isRegisterSuccessful: Boolean = false,
    val errors: FormErrors = FormErrors(),
    val generalError: String? = null,
    val qualifiesForDuocDiscount: Boolean = false,
    val touchedFields: Set<RegistroField> = emptySet()
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
        val sanitized = sanitizeName(value)
        _uiState.update { current ->
            val updated = current.copy(firstName = sanitized)
            val error = if (current.touchedFields.contains(RegistroField.FIRST_NAME)) {
                validateField(RegistroField.FIRST_NAME, updated)
            } else {
                null
            }
            updated.copy(errors = updated.errors.withField(RegistroField.FIRST_NAME, error))
        }
    }

    fun onLastNameChange(value: String) {
        val sanitized = sanitizeName(value)
        _uiState.update { current ->
            val updated = current.copy(lastName = sanitized)
            val error = if (current.touchedFields.contains(RegistroField.LAST_NAME)) {
                validateField(RegistroField.LAST_NAME, updated)
            } else {
                null
            }
            updated.copy(errors = updated.errors.withField(RegistroField.LAST_NAME, error))
        }
    }

    fun onEmailChange(value: String) {
        val sanitized = value.trimStart()
        _uiState.update { current ->
            val updated = current.copy(
                email = sanitized,
                qualifiesForDuocDiscount = isDuocEmail(sanitized)
            )
            val error = if (current.touchedFields.contains(RegistroField.EMAIL)) {
                validateField(RegistroField.EMAIL, updated)
            } else {
                null
            }
            updated.copy(errors = updated.errors.withField(RegistroField.EMAIL, error))
        }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { current ->
            val updated = current.copy(password = value)
            val passwordError = if (current.touchedFields.contains(RegistroField.PASSWORD)) {
                validateField(RegistroField.PASSWORD, updated)
            } else {
                null
            }
            val confirmError = if (current.touchedFields.contains(RegistroField.CONFIRM_PASSWORD)) {
                validateField(RegistroField.CONFIRM_PASSWORD, updated)
            } else {
                null
            }
            updated.copy(
                errors = updated.errors
                    .withField(RegistroField.PASSWORD, passwordError)
                    .withField(RegistroField.CONFIRM_PASSWORD, confirmError)
            )
        }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { current ->
            val updated = current.copy(confirmPassword = value)
            val error = if (current.touchedFields.contains(RegistroField.CONFIRM_PASSWORD)) {
                validateField(RegistroField.CONFIRM_PASSWORD, updated)
            } else {
                null
            }
            updated.copy(errors = updated.errors.withField(RegistroField.CONFIRM_PASSWORD, error))
        }
    }

    fun onBirthDateChange(value: String) {
        _uiState.update { current ->
            val touched = current.touchedFields + RegistroField.BIRTH_DATE
            val updated = current.copy(birthDate = value, touchedFields = touched)
            val error = validateField(RegistroField.BIRTH_DATE, updated)
            updated.copy(errors = updated.errors.withField(RegistroField.BIRTH_DATE, error))
        }
    }

    fun onAddressChange(value: String) {
        _uiState.update { current ->
            val updated = current.copy(address = value)
            val error = if (current.touchedFields.contains(RegistroField.ADDRESS)) {
                validateField(RegistroField.ADDRESS, updated)
            } else {
                null
            }
            updated.copy(errors = updated.errors.withField(RegistroField.ADDRESS, error))
        }
    }

    fun onRunChange(value: String) {
        val components = RunUtils.parseInput(value)
        _uiState.update { current ->
            val updated = current.copy(
                runBody = components.body,
                runCheckDigit = components.checkDigit,
                runHasHyphen = components.hasHyphen
            )
            val error = if (current.touchedFields.contains(RegistroField.RUN)) {
                validateField(RegistroField.RUN, updated)
            } else {
                null
            }
            updated.copy(errors = updated.errors.withField(RegistroField.RUN, error))
        }
    }

    fun onRegionChange(value: String) {
        _uiState.update { current ->
            val touched = current.touchedFields + RegistroField.REGION
            val shouldResetComuna = current.region != value
            val updated = current.copy(
                region = value,
                comuna = if (shouldResetComuna) "" else current.comuna,
                touchedFields = touched
            )
            var newErrors = updated.errors.withField(
                RegistroField.REGION,
                validateField(RegistroField.REGION, updated)
            )
            val comunaTouched = current.touchedFields.contains(RegistroField.COMUNA)
            if (comunaTouched || shouldResetComuna) {
                val comunaError = if (comunaTouched) {
                    validateField(RegistroField.COMUNA, updated)
                } else {
                    null
                }
                newErrors = newErrors.withField(RegistroField.COMUNA, comunaError)
            }
            updated.copy(errors = newErrors)
        }
    }

    fun onComunaChange(value: String) {
        _uiState.update { current ->
            val touched = current.touchedFields + RegistroField.COMUNA
            val updated = current.copy(comuna = value, touchedFields = touched)
            val error = validateField(RegistroField.COMUNA, updated)
            updated.copy(errors = updated.errors.withField(RegistroField.COMUNA, error))
        }
    }

    fun onReferralCodeChange(value: String) {
        _uiState.update { it.copy(referralCode = value) }
    }

    fun onTermsChange(value: Boolean) {
        _uiState.update { current ->
            val touched = current.touchedFields + RegistroField.TERMS
            val updated = current.copy(termsAccepted = value, touchedFields = touched)
            val error = validateField(RegistroField.TERMS, updated)
            updated.copy(errors = updated.errors.withField(RegistroField.TERMS, error))
        }
    }

    fun onFieldFocusLost(field: RegistroField) {
        _uiState.update { current ->
            val touched = current.touchedFields + field
            val updated = current.copy(touchedFields = touched)
            val error = validateField(field, updated)
            updated.copy(errors = updated.errors.withField(field, error))
        }
    }

    fun setRunCheckDigit(digit: String) {
        if (!RunUtils.isCheckDigitValid(digit)) return
        _uiState.update { current ->
            val updated = current.copy(runCheckDigit = digit.uppercase(), runHasHyphen = true)
            val error = if (current.touchedFields.contains(RegistroField.RUN)) {
                validateField(RegistroField.RUN, updated)
            } else {
                null
            }
            updated.copy(errors = updated.errors.withField(RegistroField.RUN, error))
        }
    }

    fun clearRunCheckDigit() {
        _uiState.update { current ->
            val updated = current.copy(runCheckDigit = "")
            val error = if (current.touchedFields.contains(RegistroField.RUN)) {
                validateField(RegistroField.RUN, updated)
            } else {
                null
            }
            updated.copy(errors = updated.errors.withField(RegistroField.RUN, error))
        }
    }

    fun register() {
        val state = _uiState.value
        val errors = validateForm(state)

        if (errors != FormErrors()) {
            _uiState.update {
                it.copy(
                    errors = errors,
                    generalError = null,
                    touchedFields = RegistroField.values().toSet()
                )
            }
            return
        }

        val sanitizedRun = RunUtils.buildFullRun(state.runBody, state.runCheckDigit)
        if (sanitizedRun == null) {
            _uiState.update {
                it.copy(
                    touchedFields = it.touchedFields + RegistroField.RUN,
                    errors = it.errors.withField(RegistroField.RUN, "RUN inválido")
                )
            }
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
                run = sanitizedRun,
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
        return FormErrors(
            firstNameError = validateField(RegistroField.FIRST_NAME, state),
            lastNameError = validateField(RegistroField.LAST_NAME, state),
            emailError = validateField(RegistroField.EMAIL, state),
            passwordError = validateField(RegistroField.PASSWORD, state),
            confirmPasswordError = validateField(RegistroField.CONFIRM_PASSWORD, state),
            birthDateError = validateField(RegistroField.BIRTH_DATE, state),
            addressError = validateField(RegistroField.ADDRESS, state),
            runError = validateField(RegistroField.RUN, state),
            regionError = validateField(RegistroField.REGION, state),
            comunaError = validateField(RegistroField.COMUNA, state),
            termsError = validateField(RegistroField.TERMS, state)
        )
    }

    private fun validateField(field: RegistroField, state: RegistroUiState): String? {
        return when (field) {
            RegistroField.FIRST_NAME -> when {
                state.firstName.isBlank() -> "El nombre es obligatorio"
                !state.firstName.all(::isValidNameChar) -> "Solo se permiten letras y espacios"
                else -> null
            }

            RegistroField.LAST_NAME -> when {
                state.lastName.isBlank() -> "Los apellidos son obligatorios"
                !state.lastName.all(::isValidNameChar) -> "Solo se permiten letras y espacios"
                else -> null
            }

            RegistroField.EMAIL -> when {
                state.email.isBlank() -> "El correo es obligatorio"
                !state.email.contains("@") -> "Correo inválido"
                else -> null
            }

            RegistroField.PASSWORD -> when {
                state.password.length < 6 -> "Contraseña muy corta"
                else -> null
            }

            RegistroField.CONFIRM_PASSWORD -> when {
                state.confirmPassword.isBlank() -> "Confirma tu contraseña"
                state.password != state.confirmPassword -> "Las contraseñas no coinciden"
                else -> null
            }

            RegistroField.BIRTH_DATE -> {
                if (state.birthDate.isBlank()) {
                    "La fecha de nacimiento es obligatoria"
                } else {
                    val parsed = parseBirthDate(state.birthDate)
                    if (parsed == null) {
                        "Formato inválido. Usa DD/MM/AAAA"
                    } else {
                        val age = calculateAge(parsed)
                        when {
                            age < MIN_AGE || age > MAX_AGE -> "La edad debe estar entre $MIN_AGE y $MAX_AGE años"
                            else -> null
                        }
                    }
                }
            }

            RegistroField.ADDRESS -> if (state.address.isBlank()) {
                "La dirección es obligatoria"
            } else {
                null
            }

            RegistroField.RUN -> when {
                state.runBody.isBlank() -> "El RUN es obligatorio"
                !RunUtils.isBodyLengthValid(state.runBody) -> "RUN incompleto"
                state.runCheckDigit.isBlank() -> "Debes ingresar el dígito verificador"
                !RunUtils.isCheckDigitValid(state.runCheckDigit) -> "Dígito verificador inválido"
                RunUtils.calculateCheckDigit(state.runBody) != state.runCheckDigit.uppercase() -> "RUN inválido"
                else -> null
            }

            RegistroField.REGION -> if (state.region.isBlank()) {
                "Selecciona una región"
            } else {
                null
            }

            RegistroField.COMUNA -> if (state.comuna.isBlank()) {
                "Selecciona una comuna"
            } else {
                null
            }

            RegistroField.TERMS -> if (!state.termsAccepted) {
                "Debes aceptar los términos"
            } else {
                null
            }
        }
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

    private fun sanitizeName(raw: String): String {
        if (raw.isEmpty()) return ""
        val filtered = raw.filter(::isValidNameChar)
        return filtered.replace(Regex("\\s+"), " ").trimStart()
    }

    private fun isValidNameChar(char: Char): Boolean {
        return char.isLetter() || char == ' ' || char == '-' || char == '\''
    }

    private fun FormErrors.withField(field: RegistroField, error: String?): FormErrors {
        return when (field) {
            RegistroField.FIRST_NAME -> copy(firstNameError = error)
            RegistroField.LAST_NAME -> copy(lastNameError = error)
            RegistroField.EMAIL -> copy(emailError = error)
            RegistroField.RUN -> copy(runError = error)
            RegistroField.BIRTH_DATE -> copy(birthDateError = error)
            RegistroField.ADDRESS -> copy(addressError = error)
            RegistroField.REGION -> copy(regionError = error)
            RegistroField.COMUNA -> copy(comunaError = error)
            RegistroField.PASSWORD -> copy(passwordError = error)
            RegistroField.CONFIRM_PASSWORD -> copy(confirmPasswordError = error)
            RegistroField.TERMS -> copy(termsError = error)
        }
    }

    companion object {
        private const val MIN_AGE = 18
        private const val MAX_AGE = 120
    }
}
