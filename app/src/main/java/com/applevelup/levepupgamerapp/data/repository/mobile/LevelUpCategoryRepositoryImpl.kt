package com.applevelup.levepupgamerapp.data.repository.mobile

import com.applevelup.levepupgamerapp.data.local.dao.CacheMetadataDao
import com.applevelup.levepupgamerapp.data.local.dao.LevelUpCategoryDao
import com.applevelup.levepupgamerapp.data.local.cache.CacheMetadataEntity
import com.applevelup.levepupgamerapp.data.mapper.LevelUpCategoryMapper
import com.applevelup.levepupgamerapp.data.network.LevelUpMobileApi
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpCategory
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpCategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LevelUpCategoryRepositoryImpl(
    private val api: LevelUpMobileApi,
    private val categoryDao: LevelUpCategoryDao,
    private val cacheMetadataDao: CacheMetadataDao,
    private val mapper: LevelUpCategoryMapper
) : LevelUpCategoryRepository {

    override fun observeCategories(): Flow<List<LevelUpCategory>> =
        categoryDao.observeCategories().map { list -> list.map(mapper::toDomain) }

    override suspend fun refreshCategories(force: Boolean): LevelUpResult<Unit> {
        if (!shouldRefresh(LevelUpCachePolicy.CATEGORIES, force)) {
            return LevelUpResult.Success(Unit)
        }
        return runCatching {
            val dtos = api.getCategories()
            val entities = dtos.map(mapper::fromDto)
            categoryDao.upsertCategories(entities)
            cacheMetadataDao.upsert(
                CacheMetadataEntity(LevelUpCachePolicy.CATEGORIES, System.currentTimeMillis())
            )
            LevelUpResult.Success(Unit)
        }.getOrElse { LevelUpResult.Failure(it) }
    }

    private suspend fun shouldRefresh(key: String, force: Boolean): Boolean {
        if (force) return true
        val metadata = cacheMetadataDao.getMetadata(key) ?: return true
        return System.currentTimeMillis() - metadata.lastUpdated >= LevelUpCachePolicy.TTL_MILLIS
    }
}
