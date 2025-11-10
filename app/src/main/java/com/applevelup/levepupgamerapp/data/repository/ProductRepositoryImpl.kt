package com.applevelup.levepupgamerapp.data.repository

import com.applevelup.levepupgamerapp.LevelUpApplication
import com.applevelup.levepupgamerapp.data.local.dao.ProductDao
import com.applevelup.levepupgamerapp.data.mapper.ProductMapper
import com.applevelup.levepupgamerapp.data.source.ProductRemoteDataSource
import com.applevelup.levepupgamerapp.domain.model.Product
import com.applevelup.levepupgamerapp.domain.model.ProductFilters
import com.applevelup.levepupgamerapp.domain.model.ProductSortOption
import com.applevelup.levepupgamerapp.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val productDao: ProductDao = LevelUpApplication.database.productDao(),
    private val remoteDataSource: ProductRemoteDataSource? = null
) : ProductRepository {

    override fun observeProducts(): Flow<List<Product>> {
        return productDao.observeProducts().map { list ->
            list.map(ProductMapper::toDomain)
        }
    }

    override suspend fun getProducts(filters: ProductFilters): List<Product> {
        val local = loadLocalProducts(filters)
        if (local.isNotEmpty() || remoteDataSource == null) {
            return local
        }

        fetchRemoteAndCache()
        return loadLocalProducts(filters)
    }

    override suspend fun getProductsByCategory(categoryName: String, filters: ProductFilters): List<Product> {
        val mergedFilters = filters.ensureCategory(categoryName)
        val local = loadLocalProducts(mergedFilters)
        if (local.isNotEmpty() || remoteDataSource == null) {
            return local
        }

        fetchRemoteAndCache()
        return loadLocalProducts(mergedFilters)
    }

    override suspend fun getProductById(id: Int): Product? {
        return productDao.getProductById(id)?.let(ProductMapper::toDomain)
    }

    override suspend fun searchProducts(query: String, filters: ProductFilters): List<Product> {
        val sanitized = query.trim()
        if (sanitized.isEmpty()) return emptyList()

        val local = loadLocalProducts(filters, sanitized)
        if (local.isNotEmpty() || remoteDataSource == null) {
            return local
        }

        val remoteProducts = fetchRemoteAndCache()
        return remoteProducts
            .filterByQuery(sanitized)
            .applyFilters(filters)
    }

    override suspend fun addProduct(product: Product): Product {
        val nextId = productDao.getMaxProductId()?.let { it + 1 } ?: 1
        val finalProduct = if (product.id == 0) product.copy(id = nextId) else product
        val entity = ProductMapper.toEntity(finalProduct)
        productDao.upsertProducts(listOf(entity))
        return ProductMapper.toDomain(entity)
    }

    private suspend fun loadLocalProducts(filters: ProductFilters, query: String? = null): List<Product> {
        val entities = when {
            query != null -> productDao.searchProducts(query)
            filters.categories.size == 1 -> productDao.getProductsByCategory(filters.categories.first())
            filters.categories.isNotEmpty() -> productDao.getProductsByCategories(filters.categories.toList())
            else -> productDao.getAllProducts()
        }

        val products = entities.map(ProductMapper::toDomain)
        val filtered = products.applyFilters(filters)
        return if (query != null) filtered.filterByQuery(query) else filtered
    }

    private suspend fun fetchRemoteAndCache(): List<Product> {
        val remoteProducts = remoteDataSource?.fetchProducts().orEmpty()
        if (remoteProducts.isNotEmpty()) {
            val entities = remoteProducts.map(ProductMapper::toEntity)
            productDao.upsertProducts(entities)
        }
        return remoteProducts
    }

    private fun List<Product>.applyFilters(filters: ProductFilters): List<Product> {
        val filtered = this
            .filter { filters.categories.isEmpty() || filters.categories.contains(it.category) }
            .filter { filters.minPrice?.let { min -> it.price >= min } ?: true }
            .filter { filters.maxPrice?.let { max -> it.price <= max } ?: true }
            .filter { filters.minRating?.let { rating -> it.rating >= rating } ?: true }
            .filter { if (filters.onSaleOnly) it.oldPrice != null else true }

        return if (filters.sortOption == ProductSortOption.RELEVANCE) {
            filtered
        } else {
            filtered.sortedWith(filters.sortOption.toComparator())
        }
    }

    private fun List<Product>.filterByQuery(query: String): List<Product> {
        val lowerQuery = query.lowercase()
        return filter { product ->
            product.name.lowercase().contains(lowerQuery) ||
                product.description.lowercase().contains(lowerQuery)
        }
    }

    private fun ProductFilters.ensureCategory(categoryName: String): ProductFilters {
        return if (categories.isEmpty()) copy(categories = setOf(categoryName)) else this
    }

    private fun ProductSortOption.toComparator(): Comparator<Product> {
        return when (this) {
            ProductSortOption.RELEVANCE -> Comparator { _, _ -> 0 }
            ProductSortOption.PRICE_ASC -> compareBy { it.price }
            ProductSortOption.PRICE_DESC -> compareByDescending { it.price }
            ProductSortOption.RATING_DESC -> compareByDescending<Product> { it.rating }
                .thenByDescending { it.reviews }
        }
    }
}
