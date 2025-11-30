package com.applevelup.levepupgamerapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.applevelup.levepupgamerapp.data.local.levelup.LevelUpCategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LevelUpCategoryDao {

    @Query("SELECT * FROM levelup_categories ORDER BY name")
    fun observeCategories(): Flow<List<LevelUpCategoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCategories(categories: List<LevelUpCategoryEntity>)

    @Query("DELETE FROM levelup_categories")
    suspend fun clearCategories()
}
