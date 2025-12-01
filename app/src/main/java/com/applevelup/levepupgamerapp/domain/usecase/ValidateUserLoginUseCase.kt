package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile
import com.applevelup.levepupgamerapp.domain.model.levelup.LoginCredentials
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpAuthRepository
import com.applevelup.levepupgamerapp.domain.sync.LegacyUserSyncer

/**
 * Use case para validar y ejecutar login de usuario con LevelUp API.
 * El token JWT se persiste automáticamente en LevelUpAuthRepository.
 */
class ValidateUserLoginUseCase(
	private val authRepository: LevelUpAuthRepository,
	private val legacyUserSyncer: LegacyUserSyncer
) {

	sealed class Result {
		data class Success(val profile: LevelUpUserProfile) : Result()
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
				// Sincronizar con cache local para UI legacy
				legacyUserSyncer.replaceWith(result.data)
				Result.Success(result.data)
			}
			is LevelUpResult.Failure -> Result.Error(result.throwable.message ?: "Credenciales inválidas")
		}
	}
}