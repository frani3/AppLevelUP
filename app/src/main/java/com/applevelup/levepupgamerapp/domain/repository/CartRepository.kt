package com.applevelup.levepupgamerapp.domain.repository

import com.applevelup.levepupgamerapp.domain.model.CartItem

interface CartRepository {
    fun getCartItems(): List<CartItem>
    fun addItem(item: CartItem)
    fun updateQuantity(itemId: Int, quantity: Int)
    fun removeItem(itemId: Int)
    fun clearCart()
}
