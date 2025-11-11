package com.applevelup.levepupgamerapp.domain.model

/**
 * Representa los filtros disponibles para consultar el catálogo.
 */
data class ProductFilters(
    val categories: Set<String> = emptySet(),
    val minPrice: Double? = null,
    val maxPrice: Double? = null,
    val minRating: Float? = null,
    val onSaleOnly: Boolean = false,
    val sortOption: ProductSortOption = ProductSortOption.RELEVANCE
) {
    fun withCategory(category: String): ProductFilters {
        return copy(categories = setOf(category))
    }

    fun activeFiltersCount(
        defaultSort: ProductSortOption = ProductSortOption.RELEVANCE,
        defaultCategories: Set<String> = emptySet()
    ): Int {
        var count = 0
        if (minPrice != null) count++
        if (maxPrice != null) count++
        if (minRating != null) count++
        if (onSaleOnly) count++
        if (sortOption != defaultSort) count++
        if (categories.isNotEmpty() && categories != defaultCategories) count++
        return count
    }
}

enum class ProductSortOption {
    RELEVANCE,
    PRICE_ASC,
    PRICE_DESC,
    RATING_DESC
}
