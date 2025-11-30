package com.applevelup.levepupgamerapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.applevelup.levepupgamerapp.data.local.levelup.LevelUpProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LevelUpProductDao {

    @Query("SELECT * FROM levelup_products ORDER BY name")
    fun observeProducts(): Flow<List<LevelUpProductEntity>>

    @Query("SELECT * FROM levelup_products WHERE code = :code")
    suspend fun getProduct(code: String): LevelUpProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProducts(products: List<LevelUpProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProduct(product: LevelUpProductEntity)
}
