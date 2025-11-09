package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.model.ProductFilters
import com.applevelup.levepupgamerapp.domain.repository.ProductRepository

class GetProductsByCategoryUseCase(private val repo: ProductRepository) {
    suspend operator fun invoke(categoryName: String, filters: ProductFilters = ProductFilters()): List<Product> {
        val mergedFilters = if (filters.categories.isEmpty()) {
            filters.copy(categories = setOf(categoryName))
        } else {
            filters
        }
        return repo.getProductsByCategory(categoryName, mergedFilters)
    }
}
