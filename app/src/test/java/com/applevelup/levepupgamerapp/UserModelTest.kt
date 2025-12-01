package com.applevelup.levepupgamerapp

import com.applevelup.levepupgamerapp.domain.model.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class UserModelTest {

    @Test
    fun `user should be created with required fields`() {
        val user = User(
            id = 1L,
            fullName = "Juan Pérez",
            email = "juan@email.com",
            isSuperAdmin = false,
            avatarRes = null
        )
        
        assertEquals(1L, user.id)
        assertEquals("Juan Pérez", user.fullName)
        assertEquals("juan@email.com", user.email)
        assertFalse(user.isSuperAdmin)
    }

    @Test
    fun `user run should be null by default`() {
        val user = User(
            id = 1L,
            fullName = "María López",
            email = "maria@email.com",
            isSuperAdmin = false,
            avatarRes = null
        )
        
        assertNull(user.run)
    }

    @Test
    fun `user hasPassword should be true by default`() {
        val user = User(
            id = 1L,
            fullName = "Pedro García",
            email = "pedro@email.com",
            isSuperAdmin = false,
            avatarRes = null
        )
        
        assertTrue(user.hasPassword)
    }

    @Test
    fun `superAdmin user should have isSuperAdmin true`() {
        val admin = User(
            id = 1L,
            fullName = "Admin User",
            email = "admin@levelup.cl",
            isSuperAdmin = true,
            avatarRes = null
        )
        
        assertTrue(admin.isSuperAdmin)
    }

    @Test
    fun `user with lifetime discount should have hasLifetimeDiscount true`() {
        val premiumUser = User(
            id = 1L,
            fullName = "Cliente VIP",
            email = "vip@email.com",
            isSuperAdmin = false,
            avatarRes = null,
            hasLifetimeDiscount = true
        )
        
        assertTrue(premiumUser.hasLifetimeDiscount)
    }

    @Test
    fun `user isSystem should be false by default`() {
        val user = User(
            id = 1L,
            fullName = "Usuario Normal",
            email = "normal@email.com",
            isSuperAdmin = false,
            avatarRes = null
        )
        
        assertFalse(user.isSystem)
    }
}
