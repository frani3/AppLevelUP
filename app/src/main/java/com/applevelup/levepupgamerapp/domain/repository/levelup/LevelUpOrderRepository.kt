package com.applevelup.levepupgamerapp.domain.repository.levelup

import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpOrder
import com.applevelup.levepupgamerapp.domain.model.levelup.LevelUpResult
import kotlinx.coroutines.flow.Flow

data class CreateOrderInput(
    val items: List<OrderItemInput>,
    val address: String,
    val region: String,
    val commune: String,
    val paymentMethod: String
)

data class OrderItemInput(
    val productCode: String,
    val name: String,
    val quantity: Int,
    val unitPrice: Double
)

interface LevelUpOrderRepository {
    fun observeOrders(): Flow<List<LevelUpOrder>>
    suspend fun refreshOrders(): LevelUpResult<List<LevelUpOrder>>
    suspend fun createOrder(input: CreateOrderInput): LevelUpResult<LevelUpOrder>
}
