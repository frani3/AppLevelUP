package com.applevelup.levepupgamerapp.domain.repository.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpRegion
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import kotlinx.coroutines.flow.Flow

interface LevelUpRegionRepository {
    fun observeRegions(): Flow<List<LevelUpRegion>>
    suspend fun refreshRegions(force: Boolean = false): LevelUpResult<Unit>
}
