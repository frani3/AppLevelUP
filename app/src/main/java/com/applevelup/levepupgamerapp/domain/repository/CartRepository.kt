package com.applevelup.levepupgamerapp.domain.repository

import com.applevelup.levepupgamerapp.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun observeCartItems(): Flow<List<CartItem>>
    suspend fun addProduct(productId: Int, quantity: Int = 1)
    suspend fun updateQuantity(productId: Int, quantity: Int)
    suspend fun removeItem(productId: Int)
    suspend fun clearCart()
}
