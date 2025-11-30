package com.applevelup.levepupgamerapp.data.mapper

import com.applevelup.levepupgamerapp.data.network.dto.LevelUpStatsDto
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpExperienceBreakdown
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpStats
import java.time.OffsetDateTime

class LevelUpStatsMapper {

    fun fromDto(dto: LevelUpStatsDto): LevelUpStats {
        val updatedAt = runCatching {
            OffsetDateTime.parse(dto.updatedAt)
        }.getOrElse { OffsetDateTime.now() }
        return LevelUpStats(
            points = dto.points,
            experience = LevelUpExperienceBreakdown(
                compras = dto.exp.compras,
                torneos = dto.exp.torneos,
                referidos = dto.exp.referidos
            ),
            referralCode = dto.referralCode,
            updatedAt = updatedAt
        )
    }
}
