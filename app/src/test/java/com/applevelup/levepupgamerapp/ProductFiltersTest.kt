package com.applevelup.levepupgamerapp

import com.applevelup.levepupgamerapp.domain.model.ProductFilters
import com.applevelup.levepupgamerapp.domain.model.ProductSortOption
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ProductFiltersTest {

    @Test
    fun `withCategory should return filters with only specified category`() {
        val filters = ProductFilters()
        val result = filters.withCategory("Teclados")
        
        assertEquals(setOf("Teclados"), result.categories)
    }

    @Test
    fun `activeFiltersCount should return 0 for default filters`() {
        val filters = ProductFilters()
        val count = filters.activeFiltersCount()
        
        assertEquals(0, count)
    }

    @Test
    fun `activeFiltersCount should count minPrice as active filter`() {
        val filters = ProductFilters(minPrice = 10000.0)
        val count = filters.activeFiltersCount()
        
        assertEquals(1, count)
    }

    @Test
    fun `activeFiltersCount should count multiple active filters`() {
        val filters = ProductFilters(
            minPrice = 10000.0,
            maxPrice = 50000.0,
            minRating = 4.0f,
            onSaleOnly = true,
            sortOption = ProductSortOption.PRICE_ASC
        )
        val count = filters.activeFiltersCount()
        
        assertEquals(5, count)
    }

    @Test
    fun `default sortOption should be RELEVANCE`() {
        val filters = ProductFilters()
        
        assertEquals(ProductSortOption.RELEVANCE, filters.sortOption)
    }

    @Test
    fun `activeFiltersCount should not count categories if equals to defaultCategories`() {
        val defaultCategories = setOf("All")
        val filters = ProductFilters(categories = setOf("All"))
        val count = filters.activeFiltersCount(defaultCategories = defaultCategories)
        
        assertEquals(0, count)
    }
}
