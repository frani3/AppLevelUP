package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.model.ProductFilters
import com.applevelup.levepupgamerapp.domain.repository.ProductRepository

class SearchProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(query: String, filters: ProductFilters = ProductFilters()): List<Product> {
        if (query.isBlank()) return emptyList()
        return repository.searchProducts(query.trim(), filters)
    }
}
