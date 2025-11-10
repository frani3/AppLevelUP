package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.repository.FavoritesRepository
import com.applevelup.levepupgamerapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ObserveFavoriteProductsUseCase(
    private val favoritesRepository: FavoritesRepository,
    private val productRepository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return combine(
            favoritesRepository.observeFavoriteIds(),
            productRepository.observeProducts()
        ) { favoriteIds, products ->
            if (favoriteIds.isEmpty()) {
                emptyList()
            } else {
                val ids = favoriteIds.toSet()
                products.filter { it.id in ids }.sortedBy { it.name }
            }
        }
    }
}
