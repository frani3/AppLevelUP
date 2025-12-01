package com.applevelup.levepupgamerapp.data.repository.mobile

import com.applevelup.levepupgamerapp.data.local.dao.CacheMetadataDao
import com.applevelup.levepupgamerapp.data.local.dao.LevelUpAddressDao
import com.applevelup.levepupgamerapp.data.local.cache.CacheMetadataEntity
import com.applevelup.levepupgamerapp.data.mapper.LevelUpAddressMapper
import com.applevelup.levepupgamerapp.data.network.LevelUpMobileApi
import com.applevelup.levepupgamerapp.data.network.dto.AddressDto
import com.applevelup.levepupgamerapp.domain.model.levelup.AddressInput
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpAddress
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpAddressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LevelUpAddressRepositoryImpl(
    private val api: LevelUpMobileApi,
    private val addressDao: LevelUpAddressDao,
    private val cacheMetadataDao: CacheMetadataDao,
    private val mapper: LevelUpAddressMapper
) : LevelUpAddressRepository {

    override fun observeAddresses(run: String): Flow<List<LevelUpAddress>> =
        addressDao.observeAddresses(run).map { list -> list.map(mapper::toDomain) }

    override suspend fun refreshAddresses(run: String, force: Boolean): LevelUpResult<Unit> {
        val key = LevelUpCachePolicy.ADDRESSES_PREFIX + run
        if (!shouldRefresh(key, force)) {
            return LevelUpResult.Success(Unit)
        }
        return runCatching {
            val payload = api.getAddresses(run)
            persistRun(run, payload, clearBeforeInsert = true)
            cacheMetadataDao.upsert(CacheMetadataEntity(key, System.currentTimeMillis()))
            LevelUpResult.Success(Unit)
        }.getOrElse { LevelUpResult.Failure(it) }
    }

    override suspend fun createAddress(run: String, input: AddressInput): LevelUpResult<LevelUpAddress> {
        return runCatching {
            val payload = api.addAddress(run, mapper.toRequest(input))
            val entity = mapper.fromDto(payload, run)
            persistRun(run, listOf(payload))
            LevelUpResult.Success(mapper.toDomain(entity))
        }.getOrElse { LevelUpResult.Failure(it) }
    }

    override suspend fun updateAddress(
        run: String,
        addressId: String,
        input: AddressInput
    ): LevelUpResult<LevelUpAddress> {
        return runCatching {
            val payload = api.updateAddress(run, addressId, mapper.toRequest(input))
            val entity = mapper.fromDto(payload, run)
            persistRun(run, listOf(payload))
            LevelUpResult.Success(mapper.toDomain(entity))
        }.getOrElse { LevelUpResult.Failure(it) }
    }

    override suspend fun deleteAddress(run: String, addressId: String): LevelUpResult<Unit> {
        return runCatching {
            val payload = api.deleteAddress(run, addressId)
            persistRun(run, payload, clearBeforeInsert = true)
            LevelUpResult.Success(Unit)
        }.getOrElse { LevelUpResult.Failure(it) }
    }

    override suspend fun setPrimaryAddress(run: String, addressId: String): LevelUpResult<Unit> {
        return runCatching {
            // El endpoint devuelve la dirección actualizada
            val response = api.setPrimaryAddress(run, addressId)
            android.util.Log.d("AddressRepo", "setPrimary response: id=${response.id}, isPrimary=${response.isPrimary}")
            
            // Obtener las direcciones actualizadas desde la API
            val updatedAddresses = api.getAddresses(run)
            android.util.Log.d("AddressRepo", "getAddresses after setPrimary: ${updatedAddresses.map { "${it.id}:${it.isPrimary}" }}")
            
            // Limpiar y guardar las direcciones actualizadas
            persistRun(run, updatedAddresses, clearBeforeInsert = true)
            
            LevelUpResult.Success(Unit)
        }.getOrElse { 
            android.util.Log.e("AddressRepo", "setPrimary error", it)
            LevelUpResult.Failure(it) 
        }
    }

    private suspend fun persistRun(run: String, addresses: List<AddressDto>, clearBeforeInsert: Boolean = false) {
        if (clearBeforeInsert) {
            addressDao.clearForRun(run)
        }
        val entities = addresses.map { mapper.fromDto(it, run) }
        addressDao.upsertAddresses(entities)
    }

    private suspend fun shouldRefresh(key: String, force: Boolean): Boolean {
        if (force) return true
        val metadata = cacheMetadataDao.getMetadata(key) ?: return true
        return System.currentTimeMillis() - metadata.lastUpdated >= LevelUpCachePolicy.TTL_MILLIS
    }
}
