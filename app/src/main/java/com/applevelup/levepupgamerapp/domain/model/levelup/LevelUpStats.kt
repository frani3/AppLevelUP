package com.applevelup.levepupgamerapp.domain.model.levelup

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

data class LevelUpStats(
    val points: Int,
    val experience: LevelUpExperienceBreakdown,
    val referralCode: String?,
    val updatedAt: OffsetDateTime
)

data class LevelUpExperienceBreakdown(
    val compras: Int,
    val torneos: Int,
    val referidos: Int
)
