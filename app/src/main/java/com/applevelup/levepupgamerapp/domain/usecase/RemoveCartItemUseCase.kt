package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.repository.CartRepository

class RemoveCartItemUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(productId: Int) {
        repository.removeItem(productId)
    }
}
