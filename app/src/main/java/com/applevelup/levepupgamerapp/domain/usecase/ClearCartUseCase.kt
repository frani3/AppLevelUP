package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.repository.CartRepository
import com.applevelup.levepupgamerapp.domain.repository.SessionRepository

class ClearCartUseCase(
    private val cartRepository: CartRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke() {
        val session = sessionRepository.getSession()
        val userId = session.userId ?: return
        cartRepository.clearCart(userId)
    }
}
