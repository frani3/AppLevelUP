package com.applevelup.levepupgamerapp.domain.usecase.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpStats
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpStatsRepository

class FetchLevelUpStatsUseCase(private val repository: LevelUpStatsRepository) {

    suspend operator fun invoke(run: String): LevelUpResult<LevelUpStats> {
        return repository.fetchStats(run)
    }
}
