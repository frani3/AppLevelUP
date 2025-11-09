package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.repository.ProductRepository

class GetProductsByCategoryUseCase(private val repo: ProductRepository) {
    suspend operator fun invoke(categoryName: String): List<Product> {
        return repo.getProductsByCategory(categoryName)
    }
}
