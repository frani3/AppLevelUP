package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.repository.ProductRepository

class CreateProductUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(product: Product): Product = repository.addProduct(product)
}
