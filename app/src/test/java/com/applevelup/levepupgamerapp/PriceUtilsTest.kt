package com.applevelup.levepupgamerapp

import com.applevelup.levepupgamerapp.utils.PriceUtils
import org.junit.Assert.assertEquals
import org.junit.Test

class PriceUtilsTest {

    @Test
    fun `formatPriceCLP should format simple price correctly`() {
        val result = PriceUtils.formatPriceCLP(99990.0)
        assertEquals("$99.990", result)
    }

    @Test
    fun `formatPriceCLP should format large price with thousand separators`() {
        val result = PriceUtils.formatPriceCLP(1249990.0)
        assertEquals("$1.249.990", result)
    }

    @Test
    fun `formatPriceCLP should handle zero price`() {
        val result = PriceUtils.formatPriceCLP(0.0)
        assertEquals("$0", result)
    }

    @Test
    fun `formatPriceCLP should truncate decimal values`() {
        val result = PriceUtils.formatPriceCLP(1999.99)
        assertEquals("$1.999", result)
    }
}
