package com.applevelup.levepupgamerapp.domain.usecase.levelup

import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpAuthRepository

class LogoutUseCase(private val repository: LevelUpAuthRepository) {

    suspend operator fun invoke() {
        repository.logout()
    }
}
