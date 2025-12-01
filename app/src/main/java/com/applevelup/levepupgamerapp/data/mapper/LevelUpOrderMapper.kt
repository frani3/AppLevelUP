package com.applevelup.levepupgamerapp.data.mapper

import com.applevelup.levepupgamerapp.data.network.dto.OrderDto
import com.applevelup.levepupgamerapp.data.network.dto.OrderItemDto
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpOrder
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpOrderItem

class LevelUpOrderMapper {

    fun fromDto(dto: OrderDto): LevelUpOrder = LevelUpOrder(
        id = dto.id,
        items = dto.items.map(::itemFromDto),
        address = dto.direccion,
        region = dto.region,
        commune = dto.comuna,
        paymentMethod = dto.paymentMethod,
        status = dto.status,
        subtotal = dto.subtotal,
        shippingCost = dto.shippingCost,
        total = dto.total,
        createdAt = dto.createdAt,
        updatedAt = dto.updatedAt
    )

    private fun itemFromDto(dto: OrderItemDto): LevelUpOrderItem = LevelUpOrderItem(
        productCode = dto.codigo,
        name = dto.nombre,
        quantity = dto.cantidad,
        unitPrice = dto.precioUnitario,
        imageUrl = dto.imageUrl
    )
}
