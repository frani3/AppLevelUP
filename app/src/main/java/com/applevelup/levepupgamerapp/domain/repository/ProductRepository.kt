package com.applevelup.levepupgamerapp.domain.repository

import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.model.ProductFilters
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun observeProducts(): Flow<List<Product>>
    suspend fun getProducts(filters: ProductFilters = ProductFilters()): List<Product>
    suspend fun getProductsByCategory(categoryName: String, filters: ProductFilters = ProductFilters()): List<Product>
    suspend fun getProductById(id: Int): Product?
    suspend fun searchProducts(query: String, filters: ProductFilters = ProductFilters()): List<Product>
}
