package com.applevelup.levepupgamerapp

import com.applevelup.levepupgamerapp.domain.model.Product
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ProductModelTest {

    @Test
    fun `product should be created with required fields`() {
        val product = Product(
            id = 1,
            code = "PROD-001",
            name = "Teclado Mecánico RGB",
            price = 59990.0,
            rating = 4.5f,
            category = "Teclados"
        )
        
        assertEquals(1, product.id)
        assertEquals("PROD-001", product.code)
        assertEquals("Teclado Mecánico RGB", product.name)
        assertEquals(59990.0, product.price, 0.01)
        assertEquals(4.5f, product.rating, 0.01f)
        assertEquals("Teclados", product.category)
    }

    @Test
    fun `product oldPrice should be null by default`() {
        val product = Product(
            id = 1,
            code = "PROD-001",
            name = "Mouse Gamer",
            price = 29990.0,
            rating = 4.0f,
            category = "Mouse"
        )
        
        assertNull(product.oldPrice)
    }

    @Test
    fun `product should have correct discount when oldPrice is set`() {
        val product = Product(
            id = 1,
            code = "PROD-001",
            name = "Audífonos Gamer",
            price = 39990.0,
            oldPrice = 49990.0,
            rating = 4.2f,
            category = "Audio"
        )
        
        val discount = ((product.oldPrice!! - product.price) / product.oldPrice!!) * 100
        assertEquals(20.0, discount, 0.5)
    }

    @Test
    fun `product default stock should be zero`() {
        val product = Product(
            id = 1,
            code = "PROD-001",
            name = "Monitor 27 pulgadas",
            price = 199990.0,
            rating = 4.8f,
            category = "Monitores"
        )
        
        assertEquals(0, product.stock)
    }

    @Test
    fun `product reviews should default to zero`() {
        val product = Product(
            id = 1,
            code = "PROD-001",
            name = "Silla Gamer",
            price = 149990.0,
            rating = 4.6f,
            category = "Sillas"
        )
        
        assertEquals(0, product.reviews)
    }
}
