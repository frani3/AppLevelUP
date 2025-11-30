package com.applevelup.levepupgamerapp.data.mapper

import com.applevelup.levepupgamerapp.data.local.levelup.LevelUpProductEntity
import com.applevelup.levepupgamerapp.data.network.dto.ProductDto
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpProduct

class LevelUpProductMapper {

    fun fromDto(dto: ProductDto): LevelUpProductEntity = LevelUpProductEntity(
        code = dto.codigo,
        name = dto.nombre,
        price = dto.precio,
        stock = dto.stock,
        category = dto.categoria,
        description = dto.descripcion,
        imageUrl = dto.imagenUrl
    )

    fun toDomain(entity: LevelUpProductEntity): LevelUpProduct = LevelUpProduct(
        code = entity.code,
        name = entity.name,
        price = entity.price,
        stock = entity.stock,
        category = entity.category,
        description = entity.description,
        imageUrl = entity.imageUrl
    )

    fun fromDtoToDomain(dto: ProductDto): LevelUpProduct = LevelUpProduct(
        code = dto.codigo,
        name = dto.nombre,
        price = dto.precio,
        stock = dto.stock,
        category = dto.categoria,
        description = dto.descripcion,
        imageUrl = dto.imagenUrl
    )
}
