package com.applevelup.levepupgamerapp.domain.usecase.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile
import com.applevelup.levepupgamerapp.domain.model.levelup.RegistrationData
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpAuthRepository

class RegisterUseCase(private val repository: LevelUpAuthRepository) {

    suspend operator fun invoke(registrationData: RegistrationData): LevelUpResult<LevelUpUserProfile> {
        return repository.register(registrationData)
    }
}
