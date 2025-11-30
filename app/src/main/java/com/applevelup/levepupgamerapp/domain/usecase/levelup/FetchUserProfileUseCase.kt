package com.applevelup.levepupgamerapp.domain.usecase.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResource
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class FetchUserProfileUseCase(private val repository: LevelUpUserRepository) {

    operator fun invoke(forceRefresh: Boolean = false): Flow<LevelUpResource<LevelUpUserProfile?>> {
        return repository.observeProfile()
            .map { LevelUpResource.Success(it) }
            .onStart {
                emit(LevelUpResource.Loading)
                val refresh = repository.refreshProfile()
                if (refresh is LevelUpResult.Failure) {
                    emit(LevelUpResource.Error(refresh.throwable))
                }
            }
    }
}
