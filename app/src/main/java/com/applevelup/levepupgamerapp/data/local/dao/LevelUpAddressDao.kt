package com.applevelup.levepupgamerapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.applevelup.levepupgamerapp.data.local.levelup.LevelUpAddressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LevelUpAddressDao {

    @Query("SELECT * FROM levelup_addresses WHERE userRun = :run ORDER BY isPrimary DESC, fullName ASC")
    fun observeAddresses(run: String): Flow<List<LevelUpAddressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAddresses(addresses: List<LevelUpAddressEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAddress(address: LevelUpAddressEntity)

    @Query("DELETE FROM levelup_addresses WHERE userRun = :run AND id = :id")
    suspend fun deleteAddress(run: String, id: String)

    @Query("DELETE FROM levelup_addresses WHERE userRun = :run")
    suspend fun clearForRun(run: String)
}
