package com.applevelup.levepupgamerapp.domain.model

data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val oldPrice: Double? = null,
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val imageRes: Int
)
