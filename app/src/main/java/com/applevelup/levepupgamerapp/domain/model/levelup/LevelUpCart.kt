package com.applevelup.levepupgamerapp.domain.model.levelup

data class LevelUpCart(
    val userRun: String = "",
    val items: List<LevelUpCartItem> = emptyList(),
    val totalQuantity: Int = 0,
    val updatedAt: String? = null
)

data class LevelUpCartItem(
    val productCode: String,
    val quantity: Int
)
