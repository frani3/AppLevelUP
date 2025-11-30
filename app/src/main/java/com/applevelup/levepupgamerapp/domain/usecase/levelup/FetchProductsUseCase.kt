package com.applevelup.levepupgamerapp.domain.usecase.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpProduct
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResource
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class FetchProductsUseCase(private val repository: LevelUpProductRepository) {

    operator fun invoke(forceRefresh: Boolean = false): Flow<LevelUpResource<List<LevelUpProduct>>> {
        return repository.observeProducts()
            .map<List<LevelUpProduct>, LevelUpResource<List<LevelUpProduct>>> { LevelUpResource.Success(it) }
            .onStart {
                emit(LevelUpResource.Loading)
                val refresh = repository.refreshProducts(forceRefresh)
                if (refresh is LevelUpResult.Failure) {
                    emit(LevelUpResource.Error(refresh.throwable))
                }
            }
    }
}
