package com.applevelup.levepupgamerapp.domain.repository.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpProduct
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import kotlinx.coroutines.flow.Flow

interface LevelUpProductRepository {
    fun observeProducts(): Flow<List<LevelUpProduct>>
    suspend fun refreshProducts(force: Boolean = false): LevelUpResult<Unit>
    suspend fun fetchProductDetail(code: String): LevelUpResult<LevelUpProduct>
}
