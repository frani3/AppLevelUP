package com.applevelup.levepupgamerapp.domain.model.levelup

data class LevelUpCart(
    val items: List<LevelUpCartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val total: Double = 0.0
)

data class LevelUpCartItem(
    val productCode: String,
    val name: String,
    val quantity: Int,
    val unitPrice: Double,
    val imageUrl: String?
)
