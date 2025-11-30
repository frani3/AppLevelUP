package com.applevelup.levepupgamerapp.domain.model.levelup

data class LevelUpProduct(
    val code: String,
    val name: String,
    val price: Double,
    val stock: Int,
    val category: String,
    val description: String?,
    val imageUrl: String?
)
