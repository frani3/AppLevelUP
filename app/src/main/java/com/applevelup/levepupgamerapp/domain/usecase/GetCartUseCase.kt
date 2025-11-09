package com.applevelup.levepupgamerapp.domain.usecase

import com.applevelup.levepupgamerapp.domain.model.CartItem
import com.applevelup.levepupgamerapp.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow

class GetCartUseCase(private val repository: CartRepository) {
	operator fun invoke(): Flow<List<CartItem>> = repository.observeCartItems()
}