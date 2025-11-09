package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.SessionState
import com.applevelup.levepupgamerapp.domain.repository.SessionRepository
import com.applevelup.levepupgamerapp.domain.repository.UserRepository

class ValidateUserLoginUseCase(
	private val userRepository: UserRepository,
	private val sessionRepository: SessionRepository
) {

	sealed class Result {
		object Success : Result()
		data class Error(val message: String) : Result()
	}

	suspend operator fun invoke(email: String, password: String, rememberMe: Boolean): Result {
		if (email.isBlank() || password.isBlank()) {
			return Result.Error("Todos los campos son obligatorios")
		}

		val user = userRepository.authenticate(email, password) ?: return Result.Error("Credenciales inválidas")

		sessionRepository.saveSession(
			SessionState(
				isLoggedIn = true,
				userId = user.id,
				email = if (rememberMe) user.email else null,
				fullName = user.fullName,
				rememberMe = rememberMe
			)
		)

		return Result.Success
	}
}