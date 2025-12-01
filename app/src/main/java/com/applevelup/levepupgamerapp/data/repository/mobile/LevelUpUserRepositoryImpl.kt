package com.applevelup.levepupgamerapp.data.repository.mobile

import com.applevelup.levepupgamerapp.data.mapper.LevelUpUserMapper
import com.applevelup.levepupgamerapp.data.network.LevelUpMobileApi
import com.applevelup.levepupgamerapp.data.network.dto.UpdateProfileRequestDto
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpUserRepository
import com.applevelup.levepupgamerapp.domain.sync.LegacyUserSyncer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LevelUpUserRepositoryImpl(
    private val api: LevelUpMobileApi,
    private val userMapper: LevelUpUserMapper,
    private val legacyUserSyncer: LegacyUserSyncer
) : LevelUpUserRepository {

    private val cachedProfile = MutableStateFlow<LevelUpUserProfile?>(null)

    override fun observeProfile(): Flow<LevelUpUserProfile?> = cachedProfile.asStateFlow()

    override suspend fun refreshProfile(): LevelUpResult<LevelUpUserProfile> {
        return runCatching {
            val dto = api.getProfile()
            val profile = userMapper.fromDto(dto)
            cachedProfile.emit(profile)
            legacyUserSyncer.replaceWith(profile)
            LevelUpResult.Success(profile)
        }.getOrElse { LevelUpResult.Failure(it) }
    }

    override suspend fun cacheProfile(profile: LevelUpUserProfile) {
        cachedProfile.emit(profile)
        legacyUserSyncer.replaceWith(profile)
    }

    override suspend fun updateProfile(
        name: String,
        email: String,
        newPassword: String?
    ): LevelUpResult<LevelUpUserProfile> {
        return runCatching {
            val dto = api.updateProfile(
                UpdateProfileRequestDto(
                    nombre = name,
                    correo = email,
                    password = newPassword
                )
            )
            val profile = userMapper.fromDto(dto)
            cacheProfile(profile)
            LevelUpResult.Success(profile)
        }.getOrElse { LevelUpResult.Failure(it) }
    }
}
