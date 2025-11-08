package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.repository.ProductRepository

class GetProductByIdUseCase(private val repository: ProductRepository) {
    operator fun invoke(productId: Int): Product? {
        val allCategories = listOf(
            "Juegos de Mesa", "Accesorios", "Consolas", "Computadores Gamers",
            "Sillas Gamers", "Mouse", "Mousepad",
            "Poleras Personalizadas", "Polerones Gamers Personalizados"
        )

        for (category in allCategories) {
            val product = repository.getProductsByCategory(category).find { it.id == productId }
            if (product != null) return product
        }
        return null
    }
}
