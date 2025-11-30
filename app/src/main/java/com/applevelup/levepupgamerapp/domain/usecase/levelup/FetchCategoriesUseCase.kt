package com.applevelup.levepupgamerapp.domain.usecase.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpCategory
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResource
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class FetchCategoriesUseCase(private val repository: LevelUpCategoryRepository) {

    operator fun invoke(forceRefresh: Boolean = false): Flow<LevelUpResource<List<LevelUpCategory>>> {
        return repository.observeCategories()
            .map<List<LevelUpCategory>, LevelUpResource<List<LevelUpCategory>>> { LevelUpResource.Success(it) }
            .onStart {
                emit(LevelUpResource.Loading)
                val refresh = repository.refreshCategories(forceRefresh)
                if (refresh is LevelUpResult.Failure) {
                    emit(LevelUpResource.Error(refresh.throwable))
                }
            }
    }
}
