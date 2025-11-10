package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.exceptions.EmailAlreadyInUseException
import com.applevelup.levepupgamerapp.domain.model.SessionState
import com.applevelup.levepupgamerapp.domain.model.User
import com.applevelup.levepupgamerapp.domain.repository.SessionRepository
import com.applevelup.levepupgamerapp.domain.repository.UserRepository

class RegisterUserUseCase(
	private val userRepository: UserRepository,
	private val sessionRepository: SessionRepository
) {

	sealed class Result {
		data class Success(val user: User) : Result()
		data class Error(val message: String) : Result()
	}

	suspend operator fun invoke(
		fullName: String,
		email: String,
		password: String,
		birthDate: String,
		address: String
	): Result {
		return try {
			val user = userRepository.register(fullName, email, password, birthDate, address)
			sessionRepository.saveSession(
				SessionState(
					isLoggedIn = true,
					userId = user.id,
					email = user.email,
					fullName = user.fullName,
					rememberMe = true
				)
			)
			Result.Success(user)
		} catch (emailInUse: EmailAlreadyInUseException) {
			Result.Error("El correo ya está registrado")
		} catch (invalid: IllegalArgumentException) {
			Result.Error(invalid.message ?: "Datos inválidos")
		} catch (_: Exception) {
			Result.Error("No pudimos crear tu cuenta. Intenta nuevamente")
		}
	}
}