package com.applevelup.levepupgamerapp.data.repository.mobile

import com.applevelup.levepupgamerapp.data.mapper.LevelUpOrderMapper
import com.applevelup.levepupgamerapp.data.network.LevelUpMobileApi
import com.applevelup.levepupgamerapp.data.network.dto.CreateOrderRequestDto
import com.applevelup.levepupgamerapp.data.network.dto.OrderItemRequestDto
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpOrder
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import com.applevelup.levepupgamerapp.domain.repository.levelup.CreateOrderInput
import com.applevelup.levepupgamerapp.domain.repository.levelup.LevelUpOrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LevelUpOrderRepositoryImpl(
    private val api: LevelUpMobileApi,
    private val mapper: LevelUpOrderMapper
) : LevelUpOrderRepository {

    private val cachedOrders = MutableStateFlow<List<LevelUpOrder>>(emptyList())

    override fun observeOrders(): Flow<List<LevelUpOrder>> = cachedOrders.asStateFlow()

    override suspend fun refreshOrders(): LevelUpResult<List<LevelUpOrder>> {
        return runCatching {
            val dtos = api.getOrders()
            val orders = dtos.map(mapper::fromDto)
            cachedOrders.emit(orders)
            LevelUpResult.Success(orders)
        }.getOrElse { LevelUpResult.Failure(it) }
    }

    override suspend fun createOrder(input: CreateOrderInput): LevelUpResult<LevelUpOrder> {
        return runCatching {
            val request = CreateOrderRequestDto(
                items = input.items.map {
                    OrderItemRequestDto(
                        codigo = it.productCode,
                        nombre = it.name,
                        cantidad = it.quantity,
                        precioUnitario = it.unitPrice
                    )
                },
                direccion = input.address,
                region = input.region,
                comuna = input.commune,
                paymentMethod = input.paymentMethod
            )
            val dto = api.createOrder(request)
            val order = mapper.fromDto(dto)
            val updated = listOf(order) + cachedOrders.value
            cachedOrders.emit(updated)
            LevelUpResult.Success(order)
        }.getOrElse { LevelUpResult.Failure(it) }
    }
}
