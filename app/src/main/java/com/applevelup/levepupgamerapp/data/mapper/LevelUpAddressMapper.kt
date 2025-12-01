package com.applevelup.levepupgamerapp.data.mapper

import com.applevelup.levepupgamerapp.data.local.levelup.LevelUpAddressEntity
import com.applevelup.levepupgamerapp.data.network.dto.AddressDto
import com.applevelup.levepupgamerapp.data.network.dto.AddressRequestDto
import com.applevelup.levepupgamerapp.domain.model.levelup.AddressInput
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpAddress

class LevelUpAddressMapper {

    fun fromDto(dto: AddressDto, userRun: String): LevelUpAddressEntity = LevelUpAddressEntity(
        id = dto.id,
        userRun = userRun,
        fullName = dto.fullName,
        line1 = dto.line1,
        city = dto.city,
        region = dto.region,
        country = dto.country,
        isPrimary = dto.isPrimary
    )

    fun toDomain(entity: LevelUpAddressEntity): LevelUpAddress = LevelUpAddress(
        id = entity.id,
        fullName = entity.fullName,
        line1 = entity.line1,
        city = entity.city,
        region = entity.region,
        country = entity.country,
        isPrimary = entity.isPrimary
    )

    fun toRequest(input: AddressInput): AddressRequestDto = AddressRequestDto(
        fullName = input.fullName,
        line1 = input.line1,
        city = input.city,
        region = input.region,
        country = input.country,
        isPrimary = input.isPrimary
    )
}
