package com.applevelup.levepupgamerapp.data.mapper

import com.applevelup.levepupgamerapp.data.network.dto.UserProfileDto
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpUserProfile

class LevelUpUserMapper(private val statsMapper: LevelUpStatsMapper) {

    fun fromDto(dto: UserProfileDto): LevelUpUserProfile = LevelUpUserProfile(
        run = dto.run,
        name = dto.nombre,
        email = dto.correo,
        address = dto.direccion,
        commune = dto.comuna,
        region = dto.region,
        stats = dto.levelUpStats?.let(statsMapper::fromDto)
    )
}
