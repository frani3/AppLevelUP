package com.applevelup.levepupgamerapp.domain.repository.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.AddressInput
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpAddress
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import kotlinx.coroutines.flow.Flow

interface LevelUpAddressRepository {
    fun observeAddresses(run: String): Flow<List<LevelUpAddress>>
    suspend fun refreshAddresses(run: String, force: Boolean = false): LevelUpResult<Unit>
    suspend fun createAddress(run: String, input: AddressInput): LevelUpResult<LevelUpAddress>
    suspend fun updateAddress(run: String, addressId: String, input: AddressInput): LevelUpResult<LevelUpAddress>
    suspend fun deleteAddress(run: String, addressId: String): LevelUpResult<Unit>
    suspend fun setPrimaryAddress(run: String, addressId: String): LevelUpResult<Unit>
}
