package com.applevelup.levepupgamerapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class CartDto(
    val items: List<CartItemDto>,
    val subtotal: Double,
    val total: Double
)

data class CartItemDto(
    val codigo: String,
    val nombre: String,
    val cantidad: Int,
    val precioUnitario: Double,
    val imageUrl: String?
)

data class UpdateCartRequestDto(
    val items: List<CartItemRequestDto>
)

data class CartItemRequestDto(
    val codigo: String,
    val cantidad: Int
)
