package com.applevelup.levepupgamerapp.data.mapper

import com.applevelup.levepupgamerapp.data.network.dto.CartDto
import com.applevelup.levepupgamerapp.data.network.dto.CartItemDto
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpCart
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpCartItem

class LevelUpCartMapper {

    fun fromDto(dto: CartDto): LevelUpCart = LevelUpCart(
        items = dto.items.map(::itemFromDto),
        subtotal = dto.subtotal,
        total = dto.total
    )

    private fun itemFromDto(dto: CartItemDto): LevelUpCartItem = LevelUpCartItem(
        productCode = dto.codigo,
        name = dto.nombre,
        quantity = dto.cantidad,
        unitPrice = dto.precioUnitario,
        imageUrl = dto.imageUrl
    )
}
