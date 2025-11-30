package com.applevelup.levepupgamerapp.domain.repository.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpCategory
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import kotlinx.coroutines.flow.Flow

interface LevelUpCategoryRepository {
    fun observeCategories(): Flow<List<LevelUpCategory>>
    suspend fun refreshCategories(force: Boolean = false): LevelUpResult<Unit>
}
