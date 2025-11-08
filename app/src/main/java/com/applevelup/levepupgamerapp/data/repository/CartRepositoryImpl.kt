package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.R
import com.applevelup.levepupgamerapp.domain.model.CartItem
import com.applevelup.levepupgamerapp.domain.repository.CartRepository

class CartRepositoryImpl : CartRepository {

    private val cartItems = mutableListOf(
        CartItem(1, "Teclado Mecánico RGB", 99.99, R.drawable.teclado_product, 1),
        CartItem(2, "Mouse Gamer Inalámbrico", 64.99, R.drawable.mouse_product, 2)
    )

    override fun getCartItems(): List<CartItem> = cartItems.toList()

    override fun addItem(item: CartItem) {
        cartItems.add(item)
    }

    override fun updateQuantity(itemId: Int, quantity: Int) {
        val index = cartItems.indexOfFirst { it.id == itemId }
        if (index != -1) {
            cartItems[index] = cartItems[index].copy(quantity = quantity)
        }
    }

    override fun removeItem(itemId: Int) {
        cartItems.removeAll { it.id == itemId }
    }

    override fun clearCart() {
        cartItems.clear()
    }
}
