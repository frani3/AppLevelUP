package com.applevelup.levepupgamerapp.domain.usecase.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile
import com.applevelup.levepupgamerapp.domain.model.levelup.LoginCredentials
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpAuthRepository

class LoginUseCase(private val repository: LevelUpAuthRepository) {

    suspend operator fun invoke(credentials: LoginCredentials): LevelUpResult<LevelUpUserProfile> {
        return repository.login(credentials)
    }
}
