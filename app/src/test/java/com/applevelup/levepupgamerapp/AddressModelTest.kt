package com.applevelup.levepupgamerapp

import com.applevelup.levepupgamerapp.domain.model.Address
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AddressModelTest {

    @Test
    fun `address should be created with all fields`() {
        val address = Address(
            id = 1,
            alias = "Casa",
            street = "Av. Principal 123",
            city = "Santiago",
            details = "Depto 45",
            isDefault = true
        )
        
        assertEquals(1, address.id)
        assertEquals("Casa", address.alias)
        assertEquals("Av. Principal 123", address.street)
        assertEquals("Santiago", address.city)
        assertEquals("Depto 45", address.details)
        assertTrue(address.isDefault)
    }

    @Test
    fun `non-default address should have isDefault false`() {
        val address = Address(
            id = 2,
            alias = "Oficina",
            street = "Calle Comercio 456",
            city = "Providencia",
            details = "Piso 3",
            isDefault = false
        )
        
        assertFalse(address.isDefault)
    }

    @Test
    fun `address with empty details should be valid`() {
        val address = Address(
            id = 3,
            alias = "Bodega",
            street = "Zona Industrial 789",
            city = "Maipú",
            details = "",
            isDefault = false
        )
        
        assertEquals("", address.details)
    }
}
