package com.applevelup.levepupgamerapp.domain.usecase.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpAddress
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResource
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpAddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class ListAddressesUseCase(private val repository: LevelUpAddressRepository) {

    operator fun invoke(run: String, forceRefresh: Boolean = false): Flow<LevelUpResource<List<LevelUpAddress>>> {
        return repository.observeAddresses(run)
            .map<List<LevelUpAddress>, LevelUpResource<List<LevelUpAddress>>> { LevelUpResource.Success(it) }
            .onStart {
                emit(LevelUpResource.Loading)
                val refresh = repository.refreshAddresses(run, forceRefresh)
                if (refresh is LevelUpResult.Failure) {
                    emit(LevelUpResource.Error(refresh.throwable))
                }
            }
    }
}
