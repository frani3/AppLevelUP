package com.applevelup.levepupgamerapp.data.mapper

import com.applevelup.levepupgamerapp.data.local.levelup.LevelUpRegionEntity
import com.applevelup.levepupgamerapp.data.network.dto.RegionDto
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpRegion

class LevelUpRegionMapper {
    fun fromDto(dto: RegionDto): LevelUpRegionEntity = LevelUpRegionEntity(
        code = dto.codigo,
        name = dto.nombre
    )

    fun toDomain(entity: LevelUpRegionEntity): LevelUpRegion = LevelUpRegion(
        code = entity.code,
        name = entity.name
    )
}
