package com.applevelup.levepupgamerapp.domain.repository.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile
import com.applevelup.levepupgamerapp.domain.model.levelup.LoginCredentials
import com.applevelup.levepupgamerapp.domain.model.levelup.RegistrationData
import kotlinx.coroutines.flow.Flow

interface LevelUpAuthRepository {
    suspend fun login(credentials: LoginCredentials): LevelUpResult<LevelUpUserProfile>
    suspend fun register(registrationData: RegistrationData): LevelUpResult<LevelUpUserProfile>
    suspend fun logout()
    val sessionToken: Flow<String?>
}
