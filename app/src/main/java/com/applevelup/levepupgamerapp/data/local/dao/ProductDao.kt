package com.applevelup.levepupgamerapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.applevelup.levepupgamerapp.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

	@Query("SELECT * FROM products ORDER BY name")
	fun observeProducts(): Flow<List<ProductEntity>>

	@Query("SELECT * FROM products ORDER BY name")
	suspend fun getAllProducts(): List<ProductEntity>

	@Query("SELECT * FROM products WHERE category = :category ORDER BY name")
	suspend fun getProductsByCategory(category: String): List<ProductEntity>

	@Query("SELECT * FROM products WHERE category IN (:categories) ORDER BY name")
	suspend fun getProductsByCategories(categories: List<String>): List<ProductEntity>

	@Query("SELECT * FROM products WHERE id = :productId")
	suspend fun getProductById(productId: Int): ProductEntity?

	@Query("SELECT * FROM products WHERE (LOWER(name) LIKE '%' || LOWER(:query) || '%') OR (description IS NOT NULL AND LOWER(description) LIKE '%' || LOWER(:query) || '%') ORDER BY name")
	suspend fun searchProducts(query: String): List<ProductEntity>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun upsertProducts(products: List<ProductEntity>)

	@Query("SELECT COUNT(*) FROM products")
	suspend fun countProducts(): Int
}