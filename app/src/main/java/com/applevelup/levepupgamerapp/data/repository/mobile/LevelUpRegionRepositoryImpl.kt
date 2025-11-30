package com.applevelup.levepupgamerapp.data.repository.mobile

import com.applevelup.levepupgamerapp.data.local.dao.CacheMetadataDao
import com.applevelup.levepupgamerapp.data.local.dao.LevelUpRegionDao
import com.applevelup.levepupgamerapp.data.local.cache.CacheMetadataEntity
import com.applevelup.levepupgamerapp.data.mapper.LevelUpRegionMapper
import com.applevelup.levepupgamerapp.data.network.LevelUpMobileApi
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpRegion
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpRegionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LevelUpRegionRepositoryImpl(
    private val api: LevelUpMobileApi,
    private val regionDao: LevelUpRegionDao,
    private val cacheMetadataDao: CacheMetadataDao,
    private val mapper: LevelUpRegionMapper
) : LevelUpRegionRepository {

    override fun observeRegions(): Flow<List<LevelUpRegion>> =
        regionDao.observeRegions().map { list -> list.map(mapper::toDomain) }

    override suspend fun refreshRegions(force: Boolean): LevelUpResult<Unit> {
        if (!shouldRefresh(LevelUpCachePolicy.REGIONS, force)) {
            return LevelUpResult.Success(Unit)
        }
        return runCatching {
            val dtos = api.getRegions()
            val entities = dtos.map(mapper::fromDto)
            regionDao.upsertRegions(entities)
            cacheMetadataDao.upsert(
                CacheMetadataEntity(LevelUpCachePolicy.REGIONS, System.currentTimeMillis())
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
