package com.applevelup.levepupgamerapp.domain.repository.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile
import kotlinx.coroutines.flow.Flow

interface LevelUpUserRepository {
    fun observeProfile(): Flow<LevelUpUserProfile?>
    suspend fun refreshProfile(): LevelUpResult<LevelUpUserProfile>
    suspend fun cacheProfile(profile: LevelUpUserProfile)
    suspend fun updateProfile(
        name: String,
        email: String,
        newPassword: String?
    ): LevelUpResult<LevelUpUserProfile>
}
