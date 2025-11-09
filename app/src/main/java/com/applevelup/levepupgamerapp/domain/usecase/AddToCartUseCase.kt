package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.repository.CartRepository

class AddToCartUseCase(private val repository: CartRepository) {
	suspend operator fun invoke(productId: Int, quantity: Int = 1) {
		repository.addProduct(productId, quantity)
	}
}