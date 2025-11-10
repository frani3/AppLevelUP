package com.applevelup.levepupgamerapp.domain.model

data class CartItem(
    val id: Int,
    val name: String,
    val price: Double,
    val imageRes: Int? = null,
    val imageUrl: String? = null,
    val imageUri: String? = null,
    val quantity: Int
)
