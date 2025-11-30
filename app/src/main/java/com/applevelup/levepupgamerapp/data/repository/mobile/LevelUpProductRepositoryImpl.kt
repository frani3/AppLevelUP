package com.applevelup.levepupgamerapp.data.repository.mobile

import com.applevelup.levepupgamerapp.data.local.dao.CacheMetadataDao
import com.applevelup.levepupgamerapp.data.local.dao.LevelUpProductDao
import com.applevelup.levepupgamerapp.data.local.cache.CacheMetadataEntity
import com.applevelup.levepupgamerapp.data.mapper.LevelUpProductMapper
import com.applevelup.levepupgamerapp.data.network.LevelUpMobileApi
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpProduct
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LevelUpProductRepositoryImpl(
    private val api: LevelUpMobileApi,
    private val productDao: LevelUpProductDao,
    private val cacheMetadataDao: CacheMetadataDao,
    private val mapper: LevelUpProductMapper
) : LevelUpProductRepository {

    override fun observeProducts(): Flow<List<LevelUpProduct>> =
        productDao.observeProducts().map { list -> list.map(mapper::toDomain) }

    override suspend fun refreshProducts(force: Boolean): LevelUpResult<Unit> {
        if (!shouldRefresh(LevelUpCachePolicy.PRODUCTS, force)) {
            return LevelUpResult.Success(Unit)
        }
        return runCatching {
            val payload = api.getProducts()
            val entities = payload.map(mapper::fromDto)
            productDao.upsertProducts(entities)
            cacheMetadataDao.upsert(
                CacheMetadataEntity(LevelUpCachePolicy.PRODUCTS, System.currentTimeMillis())
            )
            LevelUpResult.Success(Unit)
        }.getOrElse { LevelUpResult.Failure(it) }
    }

    override suspend fun fetchProductDetail(code: String): LevelUpResult<LevelUpProduct> {
        return runCatching {
            val dto = api.getProductDetail(code)
            val entity = mapper.fromDto(dto)
            productDao.upsertProduct(entity)
            LevelUpResult.Success(mapper.toDomain(entity))
        }.getOrElse { LevelUpResult.Failure(it) }
    }

    private suspend fun shouldRefresh(key: String, force: Boolean): Boolean {
        if (force) return true
        val metadata = cacheMetadataDao.getMetadata(key) ?: return true
        return System.currentTimeMillis() - metadata.lastUpdated >= LevelUpCachePolicy.TTL_MILLIS
    }
}
