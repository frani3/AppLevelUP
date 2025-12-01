package com.applevelup.levepupgamerapp

import com.applevelup.levepupgamerapp.utils.SecurityUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class SecurityUtilsTest {

    @Test
    fun `hashPassword should return consistent hash for same input`() {
        val password = "miContraseñaSegura123"
        val hash1 = SecurityUtils.hashPassword(password)
        val hash2 = SecurityUtils.hashPassword(password)
        assertEquals(hash1, hash2)
    }

    @Test
    fun `hashPassword should return different hashes for different passwords`() {
        val hash1 = SecurityUtils.hashPassword("password1")
        val hash2 = SecurityUtils.hashPassword("password2")
        assertNotEquals(hash1, hash2)
    }

    @Test
    fun `hashPassword should return 64 character hex string for SHA-256`() {
        val hash = SecurityUtils.hashPassword("test")
        assertEquals(64, hash.length)
    }

    @Test
    fun `hashPassword should handle empty string`() {
        val hash = SecurityUtils.hashPassword("")
        assertEquals(64, hash.length)
    }
}
