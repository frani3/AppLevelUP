package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.LevelUpApplication
import com.applevelup.levepupgamerapp.data.local.dao.ProductDao
import com.applevelup.levepupgamerapp.data.mapper.ProductMapper
import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val productDao: ProductDao = LevelUpApplication.database.productDao()
) : ProductRepository {

    override fun observeProducts(): Flow<List<Product>> {
        return productDao.observeProducts().map { list ->
            list.map(ProductMapper::toDomain)
        }
    }

    override suspend fun getProductsByCategory(categoryName: String): List<Product> {
        return productDao.getProductsByCategory(categoryName).map(ProductMapper::toDomain)
    }

    override suspend fun getProductById(id: Int): Product? {
        return productDao.getProductById(id)?.let(ProductMapper::toDomain)
    }

    override suspend fun searchProducts(query: String): List<Product> {
        return productDao.searchProducts(query).map(ProductMapper::toDomain)
    }
}
