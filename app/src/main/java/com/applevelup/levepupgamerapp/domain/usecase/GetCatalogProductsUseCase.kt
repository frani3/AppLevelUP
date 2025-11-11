package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.model.ProductFilters
import com.applevelup.levepupgamerapp.domain.repository.ProductRepository

class GetCatalogProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(filters: ProductFilters = ProductFilters()): List<Product> {
        return repository.getProducts(filters)
    }
}
