package com.applevelup.levepupgamerapp

import com.applevelup.levepupgamerapp.domain.model.Order
import org.junit.Assert.assertEquals
import org.junit.Test

class OrderModelTest {

    @Test
    fun `order should be created with all fields`() {
        val order = Order(
            id = "ORD-001",
            date = "2025-12-01",
            status = "Entregado",
            total = "$99.990",
            itemCount = 3
        )
        
        assertEquals("ORD-001", order.id)
        assertEquals("2025-12-01", order.date)
        assertEquals("Entregado", order.status)
        assertEquals("$99.990", order.total)
        assertEquals(3, order.itemCount)
    }

    @Test
    fun `order status should reflect pending state`() {
        val order = Order(
            id = "ORD-002",
            date = "2025-11-30",
            status = "Pendiente",
            total = "$49.990",
            itemCount = 1
        )
        
        assertEquals("Pendiente", order.status)
    }

    @Test
    fun `order with single item should have itemCount 1`() {
        val order = Order(
            id = "ORD-003",
            date = "2025-11-29",
            status = "En camino",
            total = "$29.990",
            itemCount = 1
        )
        
        assertEquals(1, order.itemCount)
    }

    @Test
    fun `order with multiple items should track correct itemCount`() {
        val order = Order(
            id = "ORD-004",
            date = "2025-11-28",
            status = "Procesando",
            total = "$299.990",
            itemCount = 10
        )
        
        assertEquals(10, order.itemCount)
    }
}
