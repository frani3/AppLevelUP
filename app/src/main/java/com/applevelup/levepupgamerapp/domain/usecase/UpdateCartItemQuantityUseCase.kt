package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.repository.CartRepository

class UpdateCartItemQuantityUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(productId: Int, quantity: Int) {
        repository.updateQuantity(productId, quantity)
    }
}
