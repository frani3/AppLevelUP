package com.applevelup.levepupgamerapp

import com.applevelup.levepupgamerapp.domain.model.CartItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CartItemTest {

    @Test
    fun `cartItem should calculate total price correctly`() {
        val cartItem = CartItem(
            id = 1,
            name = "Teclado Mecánico",
            price = 59990.0,
            quantity = 2
        )
        
        val totalPrice = cartItem.price * cartItem.quantity
        assertEquals(119980.0, totalPrice, 0.01)
    }

    @Test
    fun `cartItem with quantity 1 should have total equal to unit price`() {
        val cartItem = CartItem(
            id = 1,
            name = "Mouse Gamer",
            price = 29990.0,
            quantity = 1
        )
        
        val totalPrice = cartItem.price * cartItem.quantity
        assertEquals(cartItem.price, totalPrice, 0.01)
    }

    @Test
    fun `cartItem imageRes should be null by default`() {
        val cartItem = CartItem(
            id = 1,
            name = "Producto Test",
            price = 10000.0,
            quantity = 1
        )
        
        assertNull(cartItem.imageRes)
    }

    @Test
    fun `cartItem imageUrl should be null by default`() {
        val cartItem = CartItem(
            id = 1,
            name = "Producto Test",
            price = 10000.0,
            quantity = 1
        )
        
        assertNull(cartItem.imageUrl)
    }

    @Test
    fun `cartItem should store all provided values`() {
        val cartItem = CartItem(
            id = 42,
            name = "Audífonos Premium",
            price = 89990.0,
            imageRes = 123,
            imageUrl = "https://example.com/image.jpg",
            imageUri = "content://media/image",
            quantity = 3
        )
        
        assertEquals(42, cartItem.id)
        assertEquals("Audífonos Premium", cartItem.name)
        assertEquals(89990.0, cartItem.price, 0.01)
        assertEquals(123, cartItem.imageRes)
        assertEquals("https://example.com/image.jpg", cartItem.imageUrl)
        assertEquals("content://media/image", cartItem.imageUri)
        assertEquals(3, cartItem.quantity)
    }
}
