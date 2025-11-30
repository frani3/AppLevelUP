package com.applevelup.levepupgamerapp.data.repository.mobile

import com.applevelup.levepupgamerapp.data.mapper.LevelUpStatsMapper
import com.applevelup.levepupgamerapp.data.network.LevelUpMobileApi
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpStats
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpStatsRepository

class LevelUpStatsRepositoryImpl(
    private val api: LevelUpMobileApi,
    private val statsMapper: LevelUpStatsMapper
) : LevelUpStatsRepository {

    override suspend fun fetchStats(run: String): LevelUpResult<LevelUpStats> {
        return runCatching {
            val dto = api.getLevelUpStats(run)
            LevelUpResult.Success(statsMapper.fromDto(dto))
        }.getOrElse { LevelUpResult.Failure(it) }
    }
}
