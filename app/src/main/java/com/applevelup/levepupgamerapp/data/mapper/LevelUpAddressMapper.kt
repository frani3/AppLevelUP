package com.applevelup.levepupgamerapp.data.mapper

import com.applevelup.levepupgamerapp.data.local.levelup.LevelUpAddressEntity
import com.applevelup.levepupgamerapp.data.network.dto.AddressDto
import com.applevelup.levepupgamerapp.data.network.dto.AddressRequestDto
import com.applevelup.levepupgamerapp.domain.model.levelup.AddressInput
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpAddress

class LevelUpAddressMapper {

    fun fromDto(dto: AddressDto): LevelUpAddressEntity = LevelUpAddressEntity(
        id = dto.id,
        run = dto.run,
        alias = dto.alias,
        street = dto.direccion,
        numero = dto.numero,
        comuna = dto.comuna,
        region = dto.region,
        complement = null,
        isPrimary = dto.esPrincipal
    )

    fun toDomain(entity: LevelUpAddressEntity): LevelUpAddress = LevelUpAddress(
        id = entity.id,
        run = entity.run,
        alias = entity.alias,
        street = entity.street,
        numero = entity.numero,
        comuna = entity.comuna,
        region = entity.region,
        complement = entity.complement,
        isPrimary = entity.isPrimary
    )

    fun toRequest(input: AddressInput): AddressRequestDto = AddressRequestDto(
        alias = input.alias,
        direccion = input.direccion,
        numero = input.numero,
        comuna = input.comuna,
        region = input.region,
        esPrincipal = input.isPrimary
    )
}
