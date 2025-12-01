package com.applevelup.levepupgamerapp

import com.applevelup.levepupgamerapp.utils.RunUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RunUtilsTest {

    @Test
    fun `calculateCheckDigit should return correct digit for valid RUN`() {
        // RUN conocido: 12345678-5
        val checkDigit = RunUtils.calculateCheckDigit("12345678")
        assertEquals("5", checkDigit)
    }

    @Test
    fun `calculateCheckDigit should return valid digit or K`() {
        // Verifica que el resultado sea un dígito válido (0-9 o K)
        val checkDigit = RunUtils.calculateCheckDigit("12345678")
        assertTrue(checkDigit.matches(Regex("[0-9K]")))
        assertEquals(1, checkDigit.length)
    }

    @Test
    fun `isBodyLengthValid should return true for 7 digit body`() {
        assertTrue(RunUtils.isBodyLengthValid("1234567"))
    }

    @Test
    fun `isBodyLengthValid should return true for 8 digit body`() {
        assertTrue(RunUtils.isBodyLengthValid("12345678"))
    }

    @Test
    fun `isBodyLengthValid should return false for 6 digit body`() {
        assertFalse(RunUtils.isBodyLengthValid("123456"))
    }

    @Test
    fun `formatNumberPart should add dots as thousand separators`() {
        val formatted = RunUtils.formatNumberPart("12345678")
        assertEquals("12.345.678", formatted)
    }

    @Test
    fun `parseInput should correctly parse RUN with hyphen`() {
        val result = RunUtils.parseInput("12345678-5")
        assertEquals("12345678", result.body)
        assertEquals("5", result.checkDigit)
        assertTrue(result.hasHyphen)
    }

    @Test
    fun `buildFullRun should return formatted RUN with hyphen`() {
        val fullRun = RunUtils.buildFullRun("12345678", "5")
        assertEquals("12345678-5", fullRun)
    }

    @Test
    fun `isCheckDigitValid should accept K as valid`() {
        assertTrue(RunUtils.isCheckDigitValid("K"))
    }

    @Test
    fun `formatForDisplay should format body with dots and include check digit`() {
        val display = RunUtils.formatForDisplay("12345678", "5", true)
        assertEquals("12.345.678-5", display)
    }
}
