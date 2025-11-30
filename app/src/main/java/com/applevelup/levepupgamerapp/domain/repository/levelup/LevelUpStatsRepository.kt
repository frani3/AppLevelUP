package com.applevelup.levepupgamerapp.domain.repository.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpStats

interface LevelUpStatsRepository {
    suspend fun fetchStats(run: String): LevelUpResult<LevelUpStats>
}
