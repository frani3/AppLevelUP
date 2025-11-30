package com.applevelup.levepupgamerapp.data.repository.mobile

import com.applevelup.levepupgamerapp.data.mapper.LevelUpUserMapper
import com.applevelup.levepupgamerapp.data.network.LevelUpMobileApi
import com.applevelup.levepupgamerapp.data.network.dto.LoginRequestDto
import com.applevelup.levepupgamerapp.data.network.dto.RegisterRequestDto
import com.applevelup.levepupgamerapp.data.network.session.TokenProvider
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile
import com.applevelup.levepupgamerapp.domain.model.levelup.LoginCredentials
import com.applevelup.levepupgamerapp.domain.model.levelup.RegistrationData
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpAuthRepository
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpUserRepository
import kotlinx.coroutines.flow.Flow

class LevelUpAuthRepositoryImpl(
    private val api: LevelUpMobileApi,
    private val tokenProvider: TokenProvider,
    private val userRepository: LevelUpUserRepository,
    private val userMapper: LevelUpUserMapper
) : LevelUpAuthRepository {

    override suspend fun login(credentials: LoginCredentials): LevelUpResult<LevelUpUserProfile> {
        return runCatching {
            val dto = LoginRequestDto(credentials.correo, credentials.password)
            val response = api.login(dto)
            val profile = userMapper.fromDto(response.user)
            userRepository.cacheProfile(profile)
            tokenProvider.persistToken(response.token)
            LevelUpResult.Success(profile)
        }.getOrElse { LevelUpResult.Failure(it) }
    }

    override suspend fun register(registrationData: RegistrationData): LevelUpResult<LevelUpUserProfile> {
        return runCatching {
            val dto = RegisterRequestDto(
                run = registrationData.run,
                nombre = registrationData.nombre,
                correo = registrationData.correo,
                password = registrationData.password,
                direccion = registrationData.direccion,
                region = registrationData.region,
                comuna = registrationData.comuna,
                referralCode = registrationData.referralCode
            )
            val response = api.register(dto)
            val profile = userMapper.fromDto(response.user)
            userRepository.cacheProfile(profile)
            tokenProvider.persistToken(response.token)
            LevelUpResult.Success(profile)
        }.getOrElse { LevelUpResult.Failure(it) }
    }

    override suspend fun logout() {
        tokenProvider.persistToken(null)
    }

    override val sessionToken: Flow<String?>
        get() = tokenProvider.tokenFlow
}
