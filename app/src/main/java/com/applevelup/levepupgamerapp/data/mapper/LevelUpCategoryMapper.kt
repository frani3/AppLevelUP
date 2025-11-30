package com.applevelup.levepupgamerapp.data.mapper

import com.applevelup.levepupgamerapp.data.local.levelup.LevelUpCategoryEntity
import com.applevelup.levepupgamerapp.data.network.dto.CategoryDto
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpCategory

class LevelUpCategoryMapper {

    fun fromDto(dto: CategoryDto): LevelUpCategoryEntity = LevelUpCategoryEntity(
        id = dto.id,
        name = dto.nombre,
        description = dto.descripcion
    )

    fun toDomain(entity: LevelUpCategoryEntity): LevelUpCategory = LevelUpCategory(
        id = entity.id,
        name = entity.name,
        description = entity.description
    )
}
