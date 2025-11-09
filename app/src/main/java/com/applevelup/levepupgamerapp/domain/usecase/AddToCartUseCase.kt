package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.repository.CartRepository
import com.applevelup.levepupgamerapp.domain.repository.SessionRepository

class AddToCartUseCase(
	private val cartRepository: CartRepository,
	private val sessionRepository: SessionRepository
) {
	suspend operator fun invoke(productId: Int, quantity: Int = 1) {
		val session = sessionRepository.getSession()
		val userId = session.userId ?: return
		cartRepository.addProduct(userId, productId, quantity)
	}
}