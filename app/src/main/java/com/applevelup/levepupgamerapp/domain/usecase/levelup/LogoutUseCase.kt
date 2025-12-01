package com.applevelup.levepupgamerapp.domain.usecase.levelup

import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpAuthRepository

/**
 * Use case para cerrar sesión.
 * El token se elimina automáticamente en LevelUpAuthRepository.
 */
class LogoutUseCase(
    private val authRepository: LevelUpAuthRepository
) {

    suspend operator fun invoke() {
        authRepository.logout()
    }
}
