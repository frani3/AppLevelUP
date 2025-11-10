package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.data.repository.AddressRepositoryImpl
import com.applevelup.levepupgamerapp.data.repository.PaymentRepositoryImpl
import com.applevelup.levepupgamerapp.domain.model.SessionState
import com.applevelup.levepupgamerapp.domain.repository.SessionRepository
import com.applevelup.levepupgamerapp.domain.repository.UserRepository

class ValidateUserLoginUseCase(
	private val userRepository: UserRepository,
	private val sessionRepository: SessionRepository,
	private val addressRepository: AddressRepositoryImpl = AddressRepositoryImpl(),
	private val paymentRepository: PaymentRepositoryImpl = PaymentRepositoryImpl()
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

		val candidate = userRepository.findUserByEmail(normalizedEmail)
			?: return Result.Error("Credenciales inválidas")

		val authenticatedUser = if (candidate.hasPassword) {
			if (password.isBlank()) {
				return Result.Error("Debes ingresar tu contraseña")
			}
			userRepository.authenticate(normalizedEmail, password)
				?: return Result.Error("Credenciales inválidas")
		} else {
			userRepository.authenticate(normalizedEmail, password) ?: candidate
		}

		sessionRepository.saveSession(
			SessionState(
				isLoggedIn = true,
				userId = authenticatedUser.id,
				email = if (rememberMe) authenticatedUser.email else null,
				fullName = authenticatedUser.fullName,
				rememberMe = rememberMe,
				profileRole = authenticatedUser.profileRole,
				isSuperAdmin = authenticatedUser.isSuperAdmin
			)
		)

		val profile = userRepository.getUserProfile()
		val primaryAddress = profile?.address?.trim().orEmpty()
		if (profile != null) {
			sessionRepository.updateSession { state ->
				state.copy(profileRole = profile.profileRole ?: state.profileRole)
			}
		}
		if (primaryAddress.isNotEmpty()) {
			addressRepository.setPrimaryAddress(primaryAddress)
		} else {
			addressRepository.clearAll()
		}
		paymentRepository.clearPaymentMethods()

		return Result.Success
	}
}