package com.applevelup.levepupgamerapp.domain.repository

import com.applevelup.levepupgamerapp.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun observeProducts(): Flow<List<Product>>
    suspend fun getProductsByCategory(categoryName: String): List<Product>
    suspend fun getProductById(id: Int): Product?
    suspend fun searchProducts(query: String): List<Product>
}
