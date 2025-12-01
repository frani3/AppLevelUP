package com.applevelup.levepupgamerapp.domain.usecase.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpUserRepository

class UpdateLevelUpProfileUseCase(private val repository: LevelUpUserRepository) {
    suspend operator fun invoke(
        name: String,
        email: String,
        newPassword: String?
    ): LevelUpResult<LevelUpUserProfile> {
        return repository.updateProfile(name, email, newPassword)
    }
}
