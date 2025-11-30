package com.applevelup.levepupgamerapp.domain.repository.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile
import kotlinx.coroutines.flow.Flow

interface LevelUpUserRepository {
    fun observeProfile(): Flow<LevelUpUserProfile?>
    suspend fun refreshProfile(): LevelUpResult<LevelUpUserProfile>
    fun cacheProfile(profile: LevelUpUserProfile)
}
