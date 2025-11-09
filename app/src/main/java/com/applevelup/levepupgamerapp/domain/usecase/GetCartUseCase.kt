package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.CartItem
import com.applevelup.levepupgamerapp.domain.repository.CartRepository
import com.applevelup.levepupgamerapp.domain.repository.SessionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
class GetCartUseCase(
	private val cartRepository: CartRepository,
	private val sessionRepository: SessionRepository
) {
	operator fun invoke(): Flow<List<CartItem>> =
		sessionRepository.observeSession().flatMapLatest { session ->
			val userId = session.userId
			if (!session.isLoggedIn || userId == null) {
				flowOf(emptyList())
			} else {
				cartRepository.observeCartItems(userId)
			}
		}
}