package com.applevelup.levepupgamerapp.data.mapper

import com.applevelup.levepupgamerapp.data.network.dto.CartDto
import com.applevelup.levepupgamerapp.data.network.dto.CartItemDto
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpCart
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpCartItem

class LevelUpCartMapper {

    fun fromDto(dto: CartDto): LevelUpCart = LevelUpCart(
        userRun = dto.userRun,
        items = dto.items.map(::itemFromDto),
        totalQuantity = dto.totalQuantity,
        updatedAt = dto.updatedAt
    )

    private fun itemFromDto(dto: CartItemDto): LevelUpCartItem = LevelUpCartItem(
        productCode = dto.productCode,
        quantity = dto.quantity
    )
}
