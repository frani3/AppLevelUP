package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.repository.CartRepository
import com.applevelup.levepupgamerapp.domain.repository.SessionRepository

class RemoveCartItemUseCase(
    private val cartRepository: CartRepository,
    private val sessionRepository: SessionRepository
) {
    suspend operator fun invoke(productId: Int) {
        val session = sessionRepository.getSession()
        val userId = session.userId ?: return
        cartRepository.removeItem(userId, productId)
    }
}
