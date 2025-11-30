package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.SessionState
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LoginCredentials
import com.applevelup.levepupgamerapp.domain.repository.SessionRepository
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpAuthRepository

class ValidateUserLoginUseCase(
	private val authRepository: LevelUpAuthRepository,
	private val sessionRepository: SessionRepository
) {

	sealed class Result {
		object Success : Result()
		data class Error(val message: String) : Result()
	}

	suspend operator fun invoke(email: String, password: String, rememberMe: Boolean): Result {
		val normalizedEmail = email.trim()
		if (normalizedEmail.isBlank()) {
			return Result.Error("El correo es obligatorio")
		}

		if (password.isBlank()) {
			return Result.Error("Debes ingresar tu contraseña")
		}

		return when (val result = authRepository.login(LoginCredentials(normalizedEmail, password))) {
			is LevelUpResult.Success -> {
				sessionRepository.saveSession(
					SessionState(
						isLoggedIn = true,
						userId = null,
						email = if (rememberMe) normalizedEmail else null,
						fullName = result.data.name,
						rememberMe = rememberMe,
						profileRole = null,
						isSuperAdmin = false
					)
				)
				Result.Success
			}
			is LevelUpResult.Failure -> Result.Error(result.throwable.message ?: "Credenciales inválidas")
		}
	}
}