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
		return try {
			val user = userRepository.register(
				firstName = firstName,
				lastName = lastName,
				run = run,
				email = email,
				password = password,
				birthDate = birthDate,
				region = region,
				comuna = comuna,
				address = address,
				referralCode = referralCode
			)
			sessionRepository.saveSession(
				SessionState(
					isLoggedIn = true,
					userId = user.id,
					email = user.email,
					fullName = user.fullName,
					rememberMe = true,
					profileRole = user.profileRole,
					isSuperAdmin = user.isSuperAdmin
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