package com.applevelup.levepupgamerapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.applevelup.levepupgamerapp.data.local.levelup.LevelUpRegionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LevelUpRegionDao {

    @Query("SELECT * FROM levelup_regions ORDER BY name")
    fun observeRegions(): Flow<List<LevelUpRegionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRegions(regions: List<LevelUpRegionEntity>)

    @Query("DELETE FROM levelup_regions")
    suspend fun clearRegions()
}
