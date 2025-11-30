package com.applevelup.levepupgamerapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.applevelup.levepupgamerapp.data.local.cache.CacheMetadataEntity

@Dao
interface CacheMetadataDao {

    @Query("SELECT * FROM cache_metadata WHERE `key` = :key")
    suspend fun getMetadata(key: String): CacheMetadataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(metadata: CacheMetadataEntity)
}
