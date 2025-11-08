package com.applevelup.levepupgamerapp.domain.usecase

data class AccountFormErrors(
    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmError: String? = null
)

class ValidateAccountFormUseCase {
    operator fun invoke(
        fullName: String,
        email: String,
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): AccountFormErrors {
        var nameErr: String? = null
        var emailErr: String? = null
        var passErr: String? = null
        var confErr: String? = null

        if (fullName.isBlank()) nameErr = "Ingresa tu nombre completo"
        if (!email.contains("@")) emailErr = "Correo no válido"

        if (newPassword.isNotBlank() && newPassword.length < 6)
            passErr = "La nueva contraseña debe tener al menos 6 caracteres"

        if (newPassword != confirmPassword)
            confErr = "Las contraseñas no coinciden"

        return AccountFormErrors(nameErr, emailErr, passErr, confErr)
    }
}
