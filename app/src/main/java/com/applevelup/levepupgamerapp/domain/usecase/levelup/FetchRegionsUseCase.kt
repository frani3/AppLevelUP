package com.applevelup.levepupgamerapp.domain.usecase.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpRegion
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResource
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpRegionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class FetchRegionsUseCase(private val repository: LevelUpRegionRepository) {

    operator fun invoke(forceRefresh: Boolean = false): Flow<LevelUpResource<List<LevelUpRegion>>> {
        return repository.observeRegions()
            .map { LevelUpResource.Success(it) }
            .onStart {
                emit(LevelUpResource.Loading)
                val refresh = repository.refreshRegions(forceRefresh)
                if (refresh is LevelUpResult.Failure) {
                    emit(LevelUpResource.Error(refresh.throwable))
                }
            }
    }
}
