package com.applevelup.levepupgamerapp.domain.model.levelup

data class LevelUpOrder(
    val id: String,
    val items: List<LevelUpOrderItem>,
    val address: String,
    val region: String,
    val commune: String,
    val paymentMethod: String,
    val status: String,
    val subtotal: Double,
    val shippingCost: Double,
    val total: Double,
    val createdAt: String,
    val updatedAt: String
)

data class LevelUpOrderItem(
    val productCode: String,
    val name: String,
    val quantity: Int,
    val unitPrice: Double,
    val imageUrl: String?
)
