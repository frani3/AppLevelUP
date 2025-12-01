package com.applevelup.levepupgamerapp.domain.repository.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpCart
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpCartItem
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import kotlinx.coroutines.flow.Flow

interface LevelUpCartRepository {
    fun observeCart(): Flow<LevelUpCart>
    suspend fun refreshCart(): LevelUpResult<LevelUpCart>
    suspend fun addItem(productCode: String, quantity: Int): LevelUpResult<LevelUpCart>
    suspend fun updateItemQuantity(productCode: String, quantity: Int): LevelUpResult<LevelUpCart>
    suspend fun removeItem(productCode: String): LevelUpResult<LevelUpCart>
    suspend fun clearCart(): LevelUpResult<Unit>
}
