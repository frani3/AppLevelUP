package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow

class ObserveFavoriteProductIdsUseCase(
    private val repository: FavoritesRepository
) {
    operator fun invoke(): Flow<Set<Int>> = repository.observeFavoriteIds()
}
