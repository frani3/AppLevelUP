package com.applevelup.levepupgamerapp

import com.applevelup.levepupgamerapp.domain.usecase.ValidateAccountFormUseCase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ValidateAccountFormUseCaseTest {

    private lateinit var validateAccountFormUseCase: ValidateAccountFormUseCase

    @Before
    fun setup() {
        validateAccountFormUseCase = ValidateAccountFormUseCase()
    }

    @Test
    fun `should return error when fullName is blank`() {
        val result = validateAccountFormUseCase(
            fullName = "",
            email = "test@email.com",
            currentPassword = "current",
            newPassword = "newpass123",
            confirmPassword = "newpass123"
        )
        assertNotNull(result.fullNameError)
        assertEquals("Ingresa tu nombre completo", result.fullNameError)
    }

    @Test
    fun `should return error when email does not contain at symbol`() {
        val result = validateAccountFormUseCase(
            fullName = "Juan Pérez",
            email = "emailinvalido",
            currentPassword = "current",
            newPassword = "newpass123",
            confirmPassword = "newpass123"
        )
        assertNotNull(result.emailError)
        assertEquals("Correo no válido", result.emailError)
    }

    @Test
    fun `should return error when new password is less than 6 characters`() {
        val result = validateAccountFormUseCase(
            fullName = "Juan Pérez",
            email = "test@email.com",
            currentPassword = "current",
            newPassword = "12345",
            confirmPassword = "12345"
        )
        assertNotNull(result.passwordError)
        assertEquals("La nueva contraseña debe tener al menos 6 caracteres", result.passwordError)
    }

    @Test
    fun `should return error when passwords do not match`() {
        val result = validateAccountFormUseCase(
            fullName = "Juan Pérez",
            email = "test@email.com",
            currentPassword = "current",
            newPassword = "newpass123",
            confirmPassword = "differentpass"
        )
        assertNotNull(result.confirmError)
        assertEquals("Las contraseñas no coinciden", result.confirmError)
    }

    @Test
    fun `should return no errors for valid form`() {
        val result = validateAccountFormUseCase(
            fullName = "Juan Pérez",
            email = "test@email.com",
            currentPassword = "current",
            newPassword = "newpass123",
            confirmPassword = "newpass123"
        )
        assertNull(result.fullNameError)
        assertNull(result.emailError)
        assertNull(result.passwordError)
        assertNull(result.confirmError)
    }
}
