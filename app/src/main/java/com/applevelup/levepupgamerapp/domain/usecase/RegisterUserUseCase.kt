package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile
import com.applevelup.levepupgamerapp.domain.model.levelup.RegistrationData
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpAuthRepository
import com.applevelup.levepupgamerapp.domain.sync.LegacyUserSyncer

/**
 * Use case para registro de usuario con LevelUp API.
 * El token JWT se persiste automáticamente en LevelUpAuthRepository.
 */
class RegisterUserUseCase(
	private val authRepository: LevelUpAuthRepository,
	private val legacyUserSyncer: LegacyUserSyncer
) {

	sealed class Result {
		data class Success(val profile: LevelUpUserProfile) : Result()
		data class Error(val message: String) : Result()
	}

	suspend operator fun invoke(
		firstName: String,
		lastName: String,
		run: String,
		email: String,
		password: String,
		birthDate: String,
		region: String,
		comuna: String,
		address: String,
		referralCode: String?
	): Result {
		val trimmedName = listOf(firstName, lastName)
			.map { it.trim() }
			.filter { it.isNotEmpty() }
			.joinToString(" ")

		val registrationData = RegistrationData(
			run = run,
			nombre = trimmedName,
			correo = email,
			password = password,
			direccion = address,
			region = region,
			comuna = comuna,
			referralCode = referralCode
		)

		return when (val result = authRepository.register(registrationData)) {
			is LevelUpResult.Success -> {
				// Sincronizar con cache local para UI legacy
				legacyUserSyncer.replaceWith(result.data)
				Result.Success(result.data)
			}
			is LevelUpResult.Failure -> Result.Error(result.throwable.message ?: "No pudimos crear tu cuenta. Intenta nuevamente")
		}
	}
}