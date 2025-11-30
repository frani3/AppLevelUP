package com.applevelup.levepupgamerapp.data.repository.mobile

import com.applevelup.levepupgamerapp.data.mapper.LevelUpUserMapper
import com.applevelup.levepupgamerapp.data.network.LevelUpMobileApi
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LevelUpUserRepositoryImpl(
    private val api: LevelUpMobileApi,
    private val userMapper: LevelUpUserMapper
) : LevelUpUserRepository {

    private val cachedProfile = MutableStateFlow<LevelUpUserProfile?>(null)

    override fun observeProfile(): Flow<LevelUpUserProfile?> = cachedProfile.asStateFlow()

    override suspend fun refreshProfile(): LevelUpResult<LevelUpUserProfile> {
        return runCatching {
            val dto = api.getProfile()
            val profile = userMapper.fromDto(dto)
            cachedProfile.emit(profile)
            LevelUpResult.Success(profile)
        }.getOrElse { LevelUpResult.Failure(it) }
    }

    override fun cacheProfile(profile: LevelUpUserProfile) {
        cachedProfile.tryEmit(profile)
    }
}
