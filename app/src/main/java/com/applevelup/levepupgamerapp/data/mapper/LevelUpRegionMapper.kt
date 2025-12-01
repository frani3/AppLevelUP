package com.applevelup.levepupgamerapp.data.mapper

import com.applevelup.levepupgamerapp.data.local.levelup.LevelUpRegionEntity
import com.applevelup.levepupgamerapp.data.network.dto.RegionDto
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpRegion

class LevelUpRegionMapper {
    fun fromDto(dto: RegionDto): LevelUpRegionEntity = LevelUpRegionEntity(
        id = dto.id,
        name = dto.nombre,
        comunas = dto.comunas
    )

    fun toDomain(entity: LevelUpRegionEntity): LevelUpRegion = LevelUpRegion(
        id = entity.id,
        name = entity.name,
        comunas = entity.comunas
    )
}
